/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sample.auth

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.sample.auth.actor.SampleActorProvider
import com.jeanbarrossilva.orca.core.sample.auth.actor.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider

/**
 * [Authenticator] that provides a sample [Actor].
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [authenticated][Actor.Authenticated] [Actor]'s avatar will be loaded from a
 *   [SampleImageSource].
 * @see Actor.Authenticated.avatarLoader
 */
internal class SampleAuthenticator(
  private val avatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
) : Authenticator() {
  override val authorizer: Authorizer = Authorizer.sample
  override val actorProvider = SampleActorProvider(avatarLoaderProvider)

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    return Actor.Authenticated.createSample(avatarLoaderProvider)
  }
}
