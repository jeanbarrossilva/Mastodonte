package com.jeanbarrossilva.orca.platform.ui.component.timeline.post

import android.content.Context
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.test.Loading
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSamples
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.IgnoringMutableInteractionSource
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.AccountFormatter
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.avatar.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline.HeadlineCard
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat.FavoriteStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat.ReblogStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time.RelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.time.rememberRelativeTimeProvider
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

/** Tag that identifies a [PostPreview]'s name for testing purposes. */
internal const val POST_PREVIEW_NAME_TAG = "post-preview-name"

/** Tag that identifies [PostPreview]'s metadata for testing purposes. */
internal const val POST_PREVIEW_METADATA_TAG = "post-preview-metadata"

/** Tag that identifies a [PostPreview]'s body for testing purposes. */
internal const val POST_PREVIEW_BODY_TAG = "post-preview-body"

/** Tag that identifies a [PostPreview]'s comment count stat for testing purposes. */
internal const val POST_PREVIEW_COMMENT_COUNT_STAT_TAG = "post-preview-comments-stat"

/** Tag that identifies a [PostPreview]'s reblog count stat for testing purposes. */
internal const val POST_PREVIEW_REBLOG_COUNT_STAT_TAG = "post-preview-reblogs-stat"

/** Tag that identifies a [PostPreview]'s reblog metadata for testing purposes. */
internal const val POST_PREVIEW_REBLOG_METADATA_TAG = "post-preview-reblog-metadata"

/** Tag that identifies a [PostPreview]'s share action for testing purposes. */
internal const val POST_PREVIEW_SHARE_ACTION_TAG = "post-preview-share-action"

/** Tag that identifies a [PostPreview] for testing purposes. */
const val POST_PREVIEW_TAG = "post-preview"

/** [Modifier] to be applied to a [PostPreview]'s name. */
private val nameModifier = Modifier.testTag(POST_PREVIEW_NAME_TAG)

/** [Modifier] to be applied to [PostPreview]'s metadata. */
private val metadataModifier = Modifier.testTag(POST_PREVIEW_METADATA_TAG)

/** [Modifier] to be applied to a [PostPreview]'s body. */
private val bodyModifier = Modifier.testTag(POST_PREVIEW_BODY_TAG)

/**
 * Information to be displayed on a [Post]'s preview.
 *
 * @param id Unique identifier.
 * @param avatarLoader [ImageLoader] that loads the author's avatar.
 * @param name Name of the author.
 * @param account [Account] of the author.
 * @param rebloggerName Name of the [Author] that reblogged the [Post].
 * @param text Content written by the author.
 * @param highlight [Highlight] from the [text].
 * @param publicationDateTime Zoned moment in time in which it was published.
 * @param commentCount Amount of comments.
 * @param isFavorite Whether it's marked as favorite.
 * @param favoriteCount Amount of times it's been marked as favorite.
 * @param isReblogged Whether it's reblogged.
 * @param reblogCount Amount of times it's been reblogged.
 * @param url [URL] that leads to the [Post].
 */
@Immutable
data class PostPreview(
  val id: String,
  val avatarLoader: SomeImageLoader,
  val name: String,
  private val account: Account,
  val rebloggerName: String?,
  val text: AnnotatedString,
  val highlight: Highlight?,
  private val publicationDateTime: ZonedDateTime,
  private val commentCount: Int,
  val isFavorite: Boolean,
  private val favoriteCount: Int,
  val isReblogged: Boolean,
  private val reblogCount: Int,
  internal val url: URL
) : Serializable {
  /** Formatted, displayable version of [commentCount]. */
  val formattedCommentCount = commentCount.formatted

  /** Formatted, displayable version of [favoriteCount]. */
  val formattedFavoriteCount = favoriteCount.formatted

  /** Formatted, displayable version of [reblogCount]. */
  val formattedReblogCount = reblogCount.formatted

  /**
   * Gets information about the author and how much time it's been since it was published.
   *
   * @param relativeTimeProvider [RelativeTimeProvider] for providing relative time of publication.
   */
  fun getMetadata(relativeTimeProvider: RelativeTimeProvider): String {
    val username = AccountFormatter.username(account)
    val timeSincePublication = relativeTimeProvider.provide(publicationDateTime)
    return "$username • $timeSincePublication"
  }

  companion object {
    /** [PostPreview] sample. */
    val sample
      @Composable get() = getSample(LocalContext.current, AutosTheme.colors)

    /** [PostPreview] samples. */
    val samples
      @Composable
      get() = Post.createSamples(ImageLoader.Provider.createSample()).map { it.toPostPreview() }

    /**
     * Gets a sample [PostPreview].
     *
     * @param context [Context] through which a sample [ImageLoader.Provider] will be created.
     * @param colors [Colors] by which the resulting [PostPreview]'s [text][PostPreview.text] can be
     *   colored.
     */
    fun getSample(context: Context, colors: Colors): PostPreview {
      return Post.createSample(ImageLoader.Provider.createSample(context)).toPostPreview(colors)
    }
  }
}

/**
 * Loading preview of a [Post].
 *
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
fun PostPreview(modifier: Modifier = Modifier) {
  PostPreview(
    avatar = { SmallAvatar() },
    name = { SmallTextualPlaceholder(nameModifier) },
    metadata = { MediumTextualPlaceholder(metadataModifier) },
    content = {
      Column(
        bodyModifier.semantics { set(SemanticsProperties.Loading, true) },
        Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)
      ) {
        repeat(3) { LargeTextualPlaceholder() }
        MediumTextualPlaceholder()
      }
    },
    stats = {},
    onClick = null,
    modifier
  )
}

/**
 * Preview of a [Post].
 *
 * @param preview [PostPreview] that holds the overall data to be displayed.
 * @param onHighlightClick Callback run whenever the [HeadlineCard] (if displayed) is clicked.
 * @param onFavorite Callback run whenever the [Post] is requested to be favorited.
 * @param onRepost Callback run whenever the [Post] is requested to be reblogged.
 * @param onShare Callback run whenever the [Post] is requested to be externally shared.
 * @param onClick Callback run whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since the
 *   [Post] was published.
 */
@Composable
fun PostPreview(
  preview: PostPreview,
  onHighlightClick: () -> Unit,
  onFavorite: () -> Unit,
  onRepost: () -> Unit,
  onShare: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  val metadata =
    remember(preview, relativeTimeProvider) { preview.getMetadata(relativeTimeProvider) }

  PostPreview(
    avatar = { SmallAvatar(preview.avatarLoader, preview.name) },
    name = { Text(preview.name, nameModifier) },
    metadata = {
      Text(metadata, metadataModifier)

      preview.rebloggerName?.let {
        Row(
          Modifier.testTag(POST_PREVIEW_REBLOG_METADATA_TAG),
          Arrangement.spacedBy(AutosTheme.spacings.small.dp),
          Alignment.CenterVertically
        ) {
          Icon(
            AutosTheme.iconography.repost.asImageVector,
            contentDescription = stringResource(R.string.platform_ui_repost_stat),
            Modifier.size(14.dp)
          )

          Text(stringResource(R.string.platform_ui_post_preview_reposted, it))
        }
      }
    },
    content = {
      Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.medium.dp)) {
        Text(preview.text, bodyModifier)
        preview.highlight?.headline?.let { HeadlineCard(it, onHighlightClick) }
      }
    },
    stats = {
      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Stat(
          StatPosition.LEADING,
          AutosTheme.iconography.comment.outlined.asImageVector,
          contentDescription = stringResource(R.string.platform_ui_post_preview_comments),
          onClick = {},
          Modifier.testTag(POST_PREVIEW_COMMENT_COUNT_STAT_TAG)
        ) {
          Text(preview.formattedCommentCount)
        }

        FavoriteStat(StatPosition.SUBSEQUENT, preview, onClick = onFavorite)
        ReblogStat(StatPosition.SUBSEQUENT, preview, onClick = onRepost)

        Stat(
          StatPosition.TRAILING,
          AutosTheme.iconography.share.outlined.asImageVector,
          contentDescription = stringResource(R.string.platform_ui_post_preview_share),
          onClick = onShare,
          Modifier.testTag(POST_PREVIEW_SHARE_ACTION_TAG)
        )
      }
    },
    onClick,
    modifier
  )
}

/**
 * Preview of a [Post].
 *
 * @param modifier [Modifier] to be applied to the underlying [Card].
 * @param preview [PostPreview] that holds the overall data to be displayed.
 * @param relativeTimeProvider [RelativeTimeProvider] that provides the time that's passed since the
 *   [Post] was published.
 */
@Composable
internal fun SamplePostPreview(
  modifier: Modifier = Modifier,
  preview: PostPreview = PostPreview.sample,
  relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
  PostPreview(
    preview,
    onHighlightClick = {},
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onClick = {},
    modifier,
    relativeTimeProvider
  )
}

/**
 * Skeleton of the preview of a [Post].
 *
 * @param avatar Slot in which the avatar of the [Author] should be placed.
 * @param name Slot in which the name of the [Author] should be placed.
 * @param metadata Slot in which the username and the time that's passed since the [Post] was
 *   published should be placed.
 * @param content Actual content written by the [Author].
 * @param stats [Stat]s for data and actions such as comments, favorites, reblogs and external
 *   sharing.
 * @param onClick Callback run whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [Card].
 */
@Composable
private fun PostPreview(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  metadata: @Composable ColumnScope.() -> Unit,
  content: @Composable () -> Unit,
  stats: @Composable () -> Unit,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier
) {
  val interactionSource =
    remember(onClick) {
      onClick?.let { IgnoringMutableInteractionSource(HoverInteraction::class) }
        ?: EmptyMutableInteractionSource()
    }
  val spacing = AutosTheme.spacings.medium.dp
  val metadataTextStyle = AutosTheme.typography.bodySmall

  @OptIn(ExperimentalMaterial3Api::class)
  Card(
    onClick ?: {},
    modifier.testTag(POST_PREVIEW_TAG),
    shape = RectangleShape,
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    interactionSource = interactionSource
  ) {
    Column(Modifier.padding(spacing), Arrangement.spacedBy(spacing)) {
      Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
        avatar()

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
          Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)) {
            ProvideTextStyle(AutosTheme.typography.bodyLarge, name)

            CompositionLocalProvider(
              LocalContentColor provides metadataTextStyle.color,
              LocalTextStyle provides metadataTextStyle
            ) {
              metadata()
            }
          }

          content()
          stats()
        }
      }
    }
  }
}

/** Preview of a loading [PostPreview]. */
@Composable
@MultiThemePreview
private fun LoadingPostPreviewPreview() {
  AutosTheme { Surface(color = AutosTheme.colors.background.container.asColor) { PostPreview() } }
}

/** Preview of a loaded [PostPreview] with disabled [Stat]s. */
@Composable
@MultiThemePreview
private fun LoadedPostPreviewWithDisabledStatsPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      SamplePostPreview(preview = PostPreview.sample.copy(isFavorite = false, isReblogged = false))
    }
  }
}

/** Preview of a loaded [PostPreview] with enabled [Stat]s. */
@Composable
@MultiThemePreview
private fun LoadedPostPreviewWithEnabledStatsPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      SamplePostPreview(preview = PostPreview.sample.copy(isFavorite = true, isReblogged = true))
    }
  }
}