package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.platform.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * [PostProvider] that either requests [Post]s to the API or retrieves cached ones if they're
 * available.
 *
 * @param cache [Cache] of [Post]s by which [Post]s will be obtained.
 */
class MastodonPostProvider internal constructor(private val cache: Cache<Post>) : PostProvider {
  override suspend fun provide(id: String): Flow<Post> {
    val post = cache.get(id)
    return flowOf(post)
  }
}