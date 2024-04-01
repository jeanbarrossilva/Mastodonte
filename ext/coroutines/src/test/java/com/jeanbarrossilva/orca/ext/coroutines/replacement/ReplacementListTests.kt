/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.ext.coroutines.replacement

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ReplacementListTests {
  @Test
  fun adds() {
    assertEquals(replacementListOf(0), emptyReplacementList<Int>().apply { add(0) })
  }

  @Test
  fun replaces() {
    assertEquals(
      replacementListOf("Hello,", "world!", selector = String::first),
      replacementListOf("Hey,", "world!", selector = String::first).apply { add("Hello,") }
    )
  }
}
