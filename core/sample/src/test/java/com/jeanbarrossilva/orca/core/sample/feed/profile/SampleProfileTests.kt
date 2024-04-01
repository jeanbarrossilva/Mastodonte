/*
 * Copyright © 2023–2024 Orcinus
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

package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow
import com.jeanbarrossilva.orca.core.sample.test.assertTogglingEquals
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class SampleProfileTests {
  @Test
  fun `GIVEN a public unfollowed profile WHEN toggling its follow status THEN it's followed`() {
    runTest { assertTogglingEquals(Follow.Public.following(), Follow.Public.unfollowed()) }
  }

  @Test
  fun `GIVEN a public followed profile WHEN toggling its follow status THEN it's not followed`() {
    runTest { assertTogglingEquals(Follow.Public.unfollowed(), Follow.Public.following()) }
  }

  @Test
  fun `GIVEN a private unfollowed profile WHEN toggling its follow status THEN it's requested`() {
    runTest { assertTogglingEquals(Follow.Private.requested(), Follow.Private.unfollowed()) }
  }

  @Test
  fun `GIVEN a private requested profile WHEN toggling its follow status THEN it's unfollowed`() {
    runTest { assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.requested()) }
  }

  @Test
  fun `GIVEN a private followed profile WHEN toggling its follow status THEN it's unfollowed`() {
    runTest { assertTogglingEquals(Follow.Private.unfollowed(), Follow.Private.following()) }
  }
}
