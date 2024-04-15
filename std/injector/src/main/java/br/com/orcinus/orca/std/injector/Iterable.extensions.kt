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

package br.com.orcinus.orca.std.injector

import br.com.orcinus.orca.std.injector.module.Inject
import br.com.orcinus.orca.std.injector.module.Module
import br.com.orcinus.orca.std.injector.module.injection.Injection
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

/**
 * Returns a [List] containing only [KProperty1]s that characterize an injection into a [Module].
 * Those...
 * - are annotated with [Inject], denoting that the dependencies provided by their values should be
 *   automatically injected into the [Module] in which they were declared; and
 * - have a return type of [Injection], which allows for operations to be performed within the
 *   [Module] and provides a dependency lazily.
 */
@PublishedApi
internal fun Iterable<KProperty1<out Module, *>>.filterIsInjection():
  List<KProperty1<Module, Injection<Any>>> {
  @Suppress("UNCHECKED_CAST")
  return filter { it.hasAnnotation<Inject>() && it.returnType.jvmErasure == Injection::class }
    as List<KProperty1<Module, Injection<Any>>>
}
