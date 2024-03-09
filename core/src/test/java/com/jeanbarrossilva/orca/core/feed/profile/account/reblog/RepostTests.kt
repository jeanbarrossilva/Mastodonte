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

package com.jeanbarrossilva.orca.core.feed.profile.account.reblog

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.DeletablePost
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.addable.AddableStat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSamples
import java.net.URL
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RepostTests {
  private val sampleRepost
    get() = Posts.withSamples.filterIsInstance<Repost>().first()

  @Test
  fun createsRepost() {
    assertEquals(
      object : Repost() {
        override val id = sampleRepost.id
        override val author = sampleRepost.author
        override val reposter = sampleRepost.reposter
        override val content = sampleRepost.content
        override val publicationDateTime = sampleRepost.publicationDateTime
        override val comment = sampleRepost.comment
        override val favorite = sampleRepost.favorite
        override val repost = sampleRepost.repost
        override val url = sampleRepost.url

        override fun asDeletable(): DeletablePost {
          return let {
            object : DeletablePost(it) {
              override suspend fun delete() {}

              override fun clone(
                id: String,
                author: Author,
                content: Content,
                publicationDateTime: ZonedDateTime,
                comment: AddableStat<Post>,
                favorite: ToggleableStat<Profile>,
                repost: ToggleableStat<Profile>,
                url: URL
              ): Post {
                return this
              }
            }
          }
        }

        override fun clone(
          id: String,
          author: Author,
          reposter: Author,
          content: Content,
          publicationDateTime: ZonedDateTime,
          comment: AddableStat<Post>,
          favorite: ToggleableStat<Profile>,
          repost: ToggleableStat<Profile>,
          url: URL
        ): Repost {
          return this
        }
      },
      Repost(
        sampleRepost.id,
        sampleRepost.author,
        sampleRepost.reposter,
        sampleRepost.content,
        sampleRepost.publicationDateTime,
        sampleRepost.comment,
        sampleRepost.favorite,
        sampleRepost.repost,
        sampleRepost.url,
        asDeletable = repost@{
            object : DeletablePost(this@repost) {
              override suspend fun delete() {}

              override fun clone(
                id: String,
                author: Author,
                content: Content,
                publicationDateTime: ZonedDateTime,
                comment: AddableStat<Post>,
                favorite: ToggleableStat<Profile>,
                repost: ToggleableStat<Profile>,
                url: URL
              ): Post {
                return this
              }
            }
          }
      ) { _, _, _, _, _, _, _, _, _ ->
        this
      }
    )
  }

  @Test
  fun createsRepostFromOriginalPost() {
    assertEquals(
      sampleRepost,
      Repost(sampleRepost, sampleRepost.reposter) { _, _, _, _, _, _, _, _, _ -> this }
    )
  }
}
