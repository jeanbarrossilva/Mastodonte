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

/** Authenticates a user through [authenticate]. */
abstract class Authenticator {
  /** [Authorizer] with which the user will be authorized. */
  protected abstract val authorizer: Authorizer

  /**
   * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
   * remembered when authentication occurs.
   */
  protected abstract val actorProvider: ActorProvider

  /** Authorizes the user with the [authorizer] and then tries to authenticates them. */
  suspend fun authenticate(): Actor {
    val authorizationCode = authorizer._authorize()
    val actor = onAuthenticate(authorizationCode)
    actorProvider._remember(actor)
    return actor
  }

  /**
   * Tries to authenticate the user.
   *
   * @param authorizationCode Code that resulted from authorizing the user.
   */
  protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor
}
