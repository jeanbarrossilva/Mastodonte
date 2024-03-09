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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.stat.createSampleAddableStat
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import kotlinx.coroutines.flow.update

/**
 * Performs [SamplePost]-related writing operations.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [authenticated][Actor.Authenticated] [Actor]'s avatar will be loaded from a
 *   [SampleImageSource].
 * @param postProvider [SamplePostProvider] by which [SamplePost]s will be provided.
 * @see Actor.Authenticated.avatarLoader
 */
class SamplePostWriter
internal constructor(
  private val avatarLoaderProvider: SomeImageLoaderProvider<SampleImageSource>,
  internal val postProvider: SamplePostProvider = SamplePostProvider.from(avatarLoaderProvider)
) {
  /**
   * Default [Post]s provided by the [postProvider] ab incunabulis, as they have been provided
   * originally, without the modifications made to them by this [SamplePostWriter].
   */
  private val abIncunabulisPosts =
    postProvider.defaultPosts.map {
      it.clone(
        comment = createSampleAddableStat(it.comment.count),
        favorite = ToggleableStat(it.favorite.count),
        repost = ToggleableStat(it.repost.count)
      )
    }

  /** Provides a [SamplePostWriter] through [provide]. */
  class Provider internal constructor() {
    /** [SamplePostWriter] to be provided. */
    private var writer: SamplePostWriter? = null

    /**
     * [IllegalStateException] to be thrown if a [SamplePostWriter] is requested to be provided but
     * none has been specified.
     */
    internal class UnspecifiedWriterException :
      IllegalStateException("A post writer to be provided hasn't been specified.")

    /**
     * Provides the specified [SamplePostWriter].
     *
     * @throws UnspecifiedWriterException If a [SamplePostWriter] to be provided hasn't been
     *   specified.
     */
    @Throws(UnspecifiedWriterException::class)
    fun provide(): SamplePostWriter {
      return writer ?: throw UnspecifiedWriterException()
    }

    /** Defines the given [SamplePostWriter] as the one to be provided. */
    internal fun provide(writer: SamplePostWriter) {
      this.writer = writer
    }
  }

  /**
   * Adds the [post].
   *
   * @param post [Post] to be added.
   * @throws IllegalArgumentException If a [Post] with the same ID as the given one's is already
   *   present.
   */
  fun add(post: Post) {
    val isUnique = post.id !in postProvider.postsFlow.value.map(Post::id)
    if (isUnique) {
      postProvider.postsFlow.update { it + post }
    } else {
      throw IllegalArgumentException("A post with the same ID (${post.id}) already exists.")
    }
  }

  /**
   * Deletes the [SamplePost] identified by the [id].
   *
   * @param id ID of the [SamplePost] to be deleted.
   * @see SamplePost.id
   */
  fun delete(id: String) {
    postProvider.postsFlow.update { posts -> posts - posts.single { post -> post.id == id } }
  }

  /** Resets this [SamplePostWriter] to its default state. */
  fun reset() {
    postProvider.postsFlow.update {
      Posts(postProvider.getAuthenticationLock(), avatarLoaderProvider) {
        addAll { abIncunabulisPosts }
      }
    }
  }
}
