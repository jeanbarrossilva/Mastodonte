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

package com.jeanbarrossilva.orca.core.auth

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.flow.MutableStateFlow

/** An [AuthenticationLock.OnUnlockListener] with a generic return type. */
private typealias SomeOnUnlockListener = AuthenticationLock.OnUnlockListener<*>

/** An [AuthenticationLock] with a generic [Authenticator]. */
typealias SomeAuthenticationLock = AuthenticationLock<*>

/**
 * Ensures that operations are only performed by an [authenticated][Actor.Authenticated] [Actor].
 *
 * @param A [Authenticator] to authenticate the [Actor] with.
 * @param authenticator [Authenticator] through which the [Actor] will be requested to be
 *   [authenticated][Actor.Authenticated].
 * @param actorProvider [ActorProvider] whose provided [Actor] will be ensured to be
 *   [authenticated][Actor.Authenticated].
 * @see scheduleUnlock
 * @see scheduleUnlock
 */
class AuthenticationLock<A : Authenticator>(
  private val authenticator: A,
  private val actorProvider: ActorProvider
) {
  /**
   * [MutableStateFlow] to which [Boolean]s that indicate whether this [AuthenticationLock] has
   * ongoing unlocks are emitted.
   */
  private val activenessFlow = MutableStateFlow(false)

  /**
   * [Continuation]s associated to their respective [OnUnlockListener]s of unlocks that are awaiting
   * the one being currently performed.
   */
  private val schedule = hashMapOf<SomeOnUnlockListener, Continuation<*>>()

  /**
   * Result of a successful unlock.
   *
   * @param R Type of the [value].
   * @param actor [Actor] that has been [authenticated][Actor.Authenticated].
   * @param value [R] that's been returned by the [OnUnlockListener].
   */
  private class Unlock<R>(val actor: Actor.Authenticated, val value: R)

  /** [IllegalStateException] thrown if authentication fails. */
  class FailedAuthenticationException internal constructor() :
    IllegalStateException("Could not authenticate properly.")

  /**
   * Listens to an unlock.
   *
   * @param R Value returned by [onUnlock].
   */
  fun interface OnUnlockListener<R> {
    /**
     * Callback run when the [Actor] provided by the [actorProvider] is
     * [authenticated][Actor.Authenticated].
     *
     * @param actor Provided [authenticated][Actor.Authenticated] [Actor].
     */
    suspend fun onUnlock(actor: Actor.Authenticated): R
  }

  /**
   * Ensures that the operation in the [listener]'s [onUnlock][OnUnlockListener.onUnlock] callback
   * is only performed when the [Actor] is [authenticated][Actor.Authenticated]; if it isn't, then
   * authentication is requested and, if it succeeds, the operation is performed.
   *
   * If an unlock has already been requested and is still ongoing, this one is queued for it to be
   * run as soon as the current finishes (suspending this method's execution flow until then),
   * reusing the [Actor] that's been first obtained from the [actorProvider] by the preceding
   * unlock.
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is
   *   [authenticated][Actor.Authenticated].
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  suspend fun <T> scheduleUnlock(listener: OnUnlockListener<T>): T {
    val isActive = activenessFlow.value
    return if (isActive) awaitUnlock(listener) else requestUnlock(listener)
  }

  /**
   * Suspends until the [Continuation] associated to the given [listener] is resumed with the value
   * returned by its [onUnlock][OnUnlockListener.onUnlock] callback.
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] whose returned value will be awaited.
   */
  private suspend fun <T> awaitUnlock(listener: OnUnlockListener<T>): T {
    return suspendCoroutine { schedule[listener] = it }
  }

  /**
   * Ensures that the operation in the [listener]'s [onUnlock][OnUnlockListener.onUnlock] callback
   * is only performed when the [Actor] is [authenticated][Actor.Authenticated]; if it isn't, then
   * authentication is requested and, if it succeeds, the operation is performed.
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is
   *   [authenticated][Actor.Authenticated].
   * @return Result of the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  private suspend fun <T> requestUnlock(listener: OnUnlockListener<T>): T {
    val unlock = activate { requestUnlockWithProvidedActor(listener) }
    requestScheduledUnlocks(unlock.actor)
    return unlock.value
  }

  /**
   * Suspends until the [Actor] provided by the [actorProvider] is
   * [authenticated][Actor.Authenticated], requesting the authentication process to be performed if
   * it currently isn't. After it's finished, the [listener] is notified.
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is
   *   [authenticated][Actor.Authenticated].
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  private suspend fun <T> requestUnlockWithProvidedActor(listener: OnUnlockListener<T>): Unlock<T> {
    return when (val actor = actorProvider.provide()) {
      is Actor.Unauthenticated -> authenticateAndUnlock(listener)
      is Actor.Authenticated -> Unlock(actor, listener.onUnlock(actor))
    }
  }

  /**
   * Authenticates and notifies the [listener] if the resulting [Actor] is
   * [authenticated][Actor.Authenticated].
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is
   *   [authenticated][Actor.Authenticated].
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  private suspend fun <T> authenticateAndUnlock(listener: OnUnlockListener<T>): Unlock<T> {
    val actor = authenticator.authenticate()
    val returned =
      if (actor is Actor.Authenticated) {
        listener.onUnlock(actor)
      } else {
        throw FailedAuthenticationException()
      }
    return Unlock(actor, returned)
  }

  /**
   * Requests scheduled unlocks to be performed, resuming their associated [Continuation]s with the
   * value returned by their [OnUnlockListener].
   *
   * Also prevents an [Actor] from having to be retrieved multiple times by propagating the one
   * obtained from the first ongoing unlock to those that have been scheduled to be performed after
   * it consecutively.
   *
   * @param actor [Actor] to be provided to the scheduled unlocks.
   */
  private suspend fun requestScheduledUnlocks(actor: Actor.Authenticated) {
    schedule.forEach { (listener, continuation) ->
      val value = activate { listener.onUnlock(actor) }
      @Suppress("UNCHECKED_CAST") (continuation as Continuation<Any?>).resume(value)
      schedule.remove(listener)
    }
  }

  /**
   * Considers this [AuthenticationLock] to be active while the given [action] is being performed.
   *
   * @param action Operation to be performed while in an active state.
   * @see activenessFlow
   */
  private inline fun <T> activate(action: () -> T): T {
    activenessFlow.value = true
    return action().also { activenessFlow.value = false }
  }

  companion object
}
