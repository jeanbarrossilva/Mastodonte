/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.platform.core

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSamples
import com.jeanbarrossilva.orca.platform.core.image.sample
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/** [Posts] returned by [withSample]. */
private val postsWithSample =
  Posts(AuthenticationLock.sample, ComposableImageLoader.Provider.sample) {
    add { Post.createSample(ComposableImageLoader.Provider.sample) }
  }

/** [Posts] returned by [withSamples]. */
private val postsWithSamples =
  Posts(AuthenticationLock.sample, ComposableImageLoader.Provider.sample) {
    addAll { Post.createSamples(ComposableImageLoader.Provider.sample) }
  }

/** [Posts] whose sample's images are loaded by a sample [ComposableImageLoader]. */
val Posts.Companion.withSample
  get() = postsWithSample

/** Sample [Posts] whose images are loaded by a [ComposableImageLoader]. */
val Posts.Companion.withSamples
  get() = postsWithSamples
