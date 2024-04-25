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

package br.com.orcinus.orca.std.markdown

import assertk.assertThat
import assertk.assertions.containsExactly
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.test.Test

internal class StringExtensionsTests {
  @Test
  fun transformsEachRegexMatchingPortion() {
    assertThat(
        "me@jeanbarrossilva.com, john@appleseed.com"
          .map(Style.Email.regex) { indices, _ -> indices }
      )
      .containsExactly(0..21, 24..41)
  }
}
