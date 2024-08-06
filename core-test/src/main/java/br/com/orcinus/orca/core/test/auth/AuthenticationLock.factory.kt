/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.test.auth

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.test.auth.Authenticator as createAuthenticator

/**
 * Creates an [AuthenticationLock].
 *
 * @param actorProvider [ActorProvider] whose provided [Actor] will be ensured to be authenticated.
 */
fun AuthenticationLock(actorProvider: ActorProvider): AuthenticationLock<Authenticator> {
  return AuthenticationLock(createAuthenticator(actorProvider = actorProvider), actorProvider)
}

/**
 * Creates an [AuthenticationLock].
 *
 * @param T [Authenticator] to authenticate the [Actor] with.
 * @param authenticator [Authenticator] through which the [Actor] will be requested to be
 *   authenticated.
 * @param actorProvider [ActorProvider] whose provided [Actor] will be ensured to be authenticated.
 */
fun <T : Authenticator> AuthenticationLock(
  authenticator: T,
  actorProvider: ActorProvider
): AuthenticationLock<T> {
  return object : AuthenticationLock<T>() {
    override val authenticator = authenticator
    override val actorProvider = actorProvider

    override fun createFailedAuthenticationException(): FailedAuthenticationException {
      return FailedAuthenticationException(cause = null)
    }
  }
}
