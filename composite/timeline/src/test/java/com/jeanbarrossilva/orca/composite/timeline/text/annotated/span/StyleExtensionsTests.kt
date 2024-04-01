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

package com.jeanbarrossilva.orca.composite.timeline.text.annotated.span

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import kotlin.test.Test

internal class StyleExtensionsTests {
  @Test
  fun convertsBoldIntoSpanStyle() {
    assertThat(Bold(0..1).toSpanStyle(Colors.LIGHT)).isEqualTo(BoldSpanStyle)
  }

  @Test
  fun convertsItalicIntoSpanStyle() {
    assertThat(Italic(0..1).toSpanStyle(Colors.LIGHT)).isEqualTo(ItalicSpanStyle)
  }

  @Test
  fun convertsEmailIntoSpanStyle() {
    assertThat(StyleExtractor.EMAIL.isExtractable(Email(0..1).toSpanStyle(Colors.LIGHT))).isTrue()
  }
}
