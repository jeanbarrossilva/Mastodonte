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

package com.jeanbarrossilva.orca.core.feed.profile.post.repost

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.addable.AddableStat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import java.net.URL
import java.time.ZonedDateTime
import java.util.Objects

/** [Post] that has been reposted by someone else. */
abstract class Repost internal constructor() : Post() {
  /** [Author] by which this [Repost] has been created. */
  abstract val reposter: Author

  override fun equals(other: Any?): Boolean {
    return other is Repost &&
      id == other.id &&
      author == other.author &&
      reposter == other.reposter &&
      content == other.content &&
      publicationDateTime == other.publicationDateTime &&
      comment == other.comment &&
      favorite == other.favorite &&
      repost == other.repost &&
      url == other.url
  }

  override fun hashCode(): Int {
    return Objects.hash(
      author,
      reposter,
      content,
      publicationDateTime,
      comment,
      favorite,
      repost,
      url
    )
  }

  override fun toString(): String {
    return "Repost(id=$id, author=$author, reposter=$reposter, content=$content, " +
      "publicationDateTime=$publicationDateTime, comment=$comment, favorite=$favorite," +
      "repost=$repost, url=$url)"
  }

  final override fun clone(
    id: String,
    author: Author,
    content: Content,
    publicationDateTime: ZonedDateTime,
    comment: AddableStat<Post>,
    favorite: ToggleableStat<Profile>,
    repost: ToggleableStat<Profile>,
    url: URL
  ): Repost {
    return clone(id, author, reposter, content, publicationDateTime, comment, favorite, repost, url)
  }

  /**
   * Creates a clone of this [Repost].
   *
   * @param id Unique identifier.
   * @param author [Author] that has authored this [Post].
   * @param reposter [Author] by which this [Repost] has been created.
   * @param content [Content] that's been composed by the [author].
   * @param publicationDateTime Zoned moment in time in which this [Post] was published.
   * @param comment [Stat] for comments.
   * @param favorite [Stat] for favorites.
   * @param repost [Stat] for reposts.
   * @param url [URL] that leads to this [Post].
   */
  abstract fun clone(
    id: String = this.id,
    author: Author = this.author,
    reposter: Author = this.reposter,
    content: Content = this.content,
    publicationDateTime: ZonedDateTime = this.publicationDateTime,
    comment: AddableStat<Post> = this.comment,
    favorite: ToggleableStat<Profile> = this.favorite,
    repost: ToggleableStat<Profile> = this.repost,
    url: URL = this.url
  ): Repost

  companion object
}
