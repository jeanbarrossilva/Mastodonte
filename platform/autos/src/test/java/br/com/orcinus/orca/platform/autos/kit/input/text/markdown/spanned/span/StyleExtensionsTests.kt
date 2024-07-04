/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span

import android.graphics.Typeface
import android.text.style.StyleSpan
import assertk.assertThat
import assertk.assertions.isTrue
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class StyleExtensionsTests {
  @Test
  fun convertsBoldStyleIntoStyleSpanWithBoldTypeface() {
    assertThat(
        Style.Bold(indices = 0..1).toSpan().isStructurallyEqual(context, StyleSpan(Typeface.BOLD))
      )
      .isTrue()
  }
}