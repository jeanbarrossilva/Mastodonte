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

package br.com.orcinus.orca.composite.timeline.text.annotated.span

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.URLSpan
import assertk.assertThat
import assertk.assertions.containsExactly
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.std.markdown.style.Style
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AnyextensionsKtTests {
  private val uri = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").build()

  @Test
  fun convertsBoldStyleSpanIntoStyle() {
    assertThat(StyleSpan(Typeface.BOLD).toStyles(0..8)).containsExactly(Style.Bold(0..8))
  }

  @Test
  fun convertsBoldItalicStyleSpanIntoStyles() {
    assertThat(StyleSpan(Typeface.BOLD_ITALIC).toStyles(0..8))
      .containsExactly(Style.Bold(0..8), Style.Italic(0..8))
  }

  @Test
  fun convertsItalicStyleSpanIntoStyle() {
    assertThat(StyleSpan(Typeface.ITALIC).toStyles(0..8)).containsExactly(Style.Italic(0..8))
  }

  @Test
  fun convertsURLSpanIntoStyle() {
    assertThat(URLSpan("$uri").toStyles(0..31)).containsExactly(Style.Link(uri, 0..31))
  }
}
