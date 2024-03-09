/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.stat.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.platform.core.withSample
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class PostExtensionsTests {
  @Test
  fun favoriteCountDependsOnEnableabilityWhenConvertingPostIntoStatDetailsFlow() {
    val post = Posts.withSample.single()
    val previousFavoriteCount = post.favorite.count
    runTest {
      post.asStatsDetailsFlow().test {
        awaitItem()
        post.favorite.enable()
        awaitItem().let {
          assertThat(it.isFavorite).isTrue()
          assertThat(it.favoriteCount).isEqualTo(previousFavoriteCount.inc())
        }
      }
    }
  }

  @Test
  fun repostCountDependsOnEnableabilityWhenConvertingPostIntoStatDetailsFlow() {
    val post = Posts.withSample.single()
    val previousRepostCount = post.repost.count
    runTest {
      post.asStatsDetailsFlow().test {
        awaitItem()
        post.repost.enable()
        awaitItem().let {
          assertThat(it.isReposted).isTrue()
          assertThat(it.repostCount).isEqualTo(previousRepostCount.inc())
        }
      }
    }
  }
}
