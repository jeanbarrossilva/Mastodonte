package com.jeanbarrossilva.orca.core.mastodon.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/**
 * [MastodonProfile] that can be edited.
 *
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through the [MastodonProfile]'s [MastodonPost]s
 *   will be provided.
 */
internal data class MastodonEditableProfile(
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  override val id: String,
  override val account: Account,
  override val avatarLoader: SomeImageLoader,
  override val name: String,
  override val bio: StyledString,
  override val followerCount: Int,
  override val followingCount: Int,
  override val url: URL
) :
  Profile by MastodonProfile(
    postPaginatorProvider,
    id,
    account,
    avatarLoader,
    name,
    bio,
    followerCount,
    followingCount,
    url
  ),
  EditableProfile() {
  override val editor = MastodonEditor()
}
