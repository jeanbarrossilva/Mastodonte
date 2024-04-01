/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.injector

import com.jeanbarrossilva.orca.ext.reflection.access
import com.jeanbarrossilva.orca.std.injector.module.Module
import com.jeanbarrossilva.orca.std.injector.module.binding.Binding
import com.jeanbarrossilva.orca.std.injector.module.binding.SomeBinding
import com.jeanbarrossilva.orca.std.injector.module.binding.boundTo
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/** [Module] that enables global [Module] and dependency injection. */
object Injector : Module() {
  /** [Binding]s that have been registered. */
  @PublishedApi internal val bindings = HashSet<SomeBinding>()

  /** [IllegalArgumentException] thrown if the [Injector] registers itself. */
  class SelfRegistrationException @PublishedApi internal constructor() :
    IllegalArgumentException("Injector cannot register itself.")

  /** [IllegalArgumentException] thrown if the [Injector] gets itself. */
  class SelfRetrievalException @PublishedApi internal constructor() :
    IllegalArgumentException("Injector cannot get itself.")

  /**
   * [NoSuchElementException] thrown if a [Module] that hasn't been registered is requested to be
   * obtained or unregistered.
   *
   * @param moduleClass [KClass] of the requested [Module].
   */
  class ModuleNotRegisteredException
  @PublishedApi
  internal constructor(moduleClass: KClass<out Module>) :
    NoSuchElementException("No module of type ${moduleClass.qualifiedName} has been injected.")

  /**
   * Registers the given [module].
   *
   * @param T [Module] to be associated to the given one.
   * @param module [Module] to be registered.
   * @throws SelfRegistrationException If the [module] is this [Injector].
   */
  inline fun <reified T : Module> register(module: T) {
    register(module.boundTo())
  }

  /**
   * Registers the given [binding].
   *
   * @param B Base [Module] to which the [Module] is bound to.
   * @param A [Module] at the utmost bottom of the inheritance tree to which the [Module] is bound
   *   to.
   * @param binding [Binding] that associates the [Module] to its [A] and [B] types.
   * @throws SelfRegistrationException If the [Module] is this [Injector].
   */
  inline fun <reified B : Module, reified A : B> register(binding: Binding<B, A>) {
    if (binding.target != this) {
      registerWithoutSelfRegistrationInsurance(binding)
    } else {
      throw SelfRegistrationException()
    }
  }

  /**
   * Gets the registered [Module] of type [T].
   *
   * @param T [Module] to be obtained.
   * @throws SelfRetrievalException If the [Module] is this [Injector].
   * @throws ModuleNotRegisteredException If no [Module] of type [T] has been registered.
   */
  @Throws(ModuleNotRegisteredException::class)
  inline fun <reified T : Module> from(): T {
    return if (T::class != Injector::class) {
      bindings.find { T::class in it }?.target as T? ?: throw ModuleNotRegisteredException(T::class)
    } else {
      throw SelfRetrievalException()
    }
  }

  /**
   * Unregisters the given [Module].
   *
   * @param T [Module] to be unregistered.
   * @throws ModuleNotRegisteredException If no [Module] of type [T] has been injected.
   */
  @Throws(ModuleNotRegisteredException::class)
  inline fun <reified T : Module> unregister() {
    bindings.find { T::class in it }?.let(bindings::remove)
      ?: throw ModuleNotRegisteredException(T::class)
  }

  override fun onClear() {
    bindings.map(SomeBinding::target).forEach(Module::clear)
    bindings.clear()
  }

  /**
   * Registers the given [binding] without ensuring that this [Module] isn't injecting itself.
   *
   * @param B Base [Module] to which the [Module] is bound to.
   * @param A [Module] at the utmost bottom of the inheritance tree to which the [Module] is bound
   *   to.
   * @param binding [Binding] that associates the [Module] to its [A] and [B] types.
   */
  @PublishedApi
  internal inline fun <reified B : Module, reified A : B> registerWithoutSelfRegistrationInsurance(
    binding: Binding<B, A>
  ) {
    bindings.add(binding)
    injectDeclaredDependenciesOf(binding.target)
  }

  /**
   * Injects the dependencies declared within the given [module].
   *
   * @param T [Module] into which its dependencies will be injected.
   * @param module [Module] whose dependencies will be injected into it.
   */
  @PublishedApi
  internal inline fun <reified T : Module> injectDeclaredDependenciesOf(module: T) {
    T::class
      .memberProperties
      .filterIsInjection()
      .map { it.access { get(module) } }
      .forEach(module::inject)
  }
}
