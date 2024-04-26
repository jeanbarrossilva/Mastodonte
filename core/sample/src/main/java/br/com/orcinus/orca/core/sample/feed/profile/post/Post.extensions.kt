/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.post.content.highlight.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.repost.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.createSampleToggleableStat
import br.com.orcinus.orca.core.sample.image.CoverImageSource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.markdown.buildMarkdown
import br.com.orcinus.orca.std.uri.URIBuilder
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of the third [Post] in the [List] returned by [createSamples]. */
private val thirdPostID = UUID.randomUUID().toString()

/** ID of the fourth [Post] in the [List] returned by [createSamples]. */
private val fourthPostID = UUID.randomUUID().toString()

/**
 * Creates sample [Post]s.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
context(Posts.Builder.AdditionScope)

fun Post.Companion.createSamples(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): List<Post> {
  return listOf(
    Repost.createSample(imageLoaderProvider),
    createSample(imageLoaderProvider),
    SampleDeletablePost(
      SamplePost(
        thirdPostID,
        Author.createSample(imageLoaderProvider),
        Content.from(
          Domain.sample,
          text =
            buildMarkdown {
              +"Great talk about the importance of introverts in an extroversion-seeking world."
              +"\n".repeat(2)
              +"https://www.ted.com/talks/susan_cain_the_power_of_introverts"
            }
        ) {
          Headline(
            title = "The power of introverts",
            subtitle =
              "In a culture where being social and outgoing are prized above all else, " +
                "it can be difficult, even shameful, to be an introvert. But, as Susan Cain " +
                "argues in this passionate talk, introverts bring extraordinary talents and " +
                "abilities to the world, and should be encouraged and celebrated.",
            imageLoaderProvider.provide(CoverImageSource.ThePowerOfIntroverts)
          )
        },
        publicationDateTime = ZonedDateTime.of(2_024, 4, 5, 9, 32, 0, 0, ZoneId.of("GMT-3")),
        favorite = createSampleToggleableStat(imageLoaderProvider),
        repost = createSampleToggleableStat(imageLoaderProvider),
        URIBuilder.url()
          .scheme("https")
          .host("mastodon.social")
          .path("@jeanbarrossilva")
          .path("111665399868682952")
          .build(),
        writerProvider
      )
    ),
    SamplePost(
      fourthPostID,
      Author.createChristianSample(imageLoaderProvider),
      Content.from(
        Domain.sample,
        text =
          buildMarkdown {
            +("Also, last day to get Pixel Pals premium at a discount and last day for the " +
              "lifetime unlock to be available!")
            +"\n".repeat(2)
            +Highlight.createSample(imageLoaderProvider).uri.toString()
          }
      ) {
        Headline.createSample(imageLoaderProvider)
      },
      publicationDateTime =
        ZonedDateTime.of(2_023, 11, 27, 18, 26, 0, 0, ZoneId.of("America/Halifax")),
      favorite = createSampleToggleableStat(imageLoaderProvider),
      repost = createSampleToggleableStat(imageLoaderProvider),
      URIBuilder.url()
        .scheme("https")
        .host("mastodon.social")
        .path("@christianselig")
        .path("111484624066823391")
        .build(),
      writerProvider
    )
  )
}

/**
 * Creates a sample [Post].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
context(Posts.Builder.AdditionScope)

fun Post.Companion.createSample(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Post {
  return DeletablePost.createSample(imageLoaderProvider)
}
