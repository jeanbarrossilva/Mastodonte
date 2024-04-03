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

package br.com.orcinus.orca.platform.ime.state

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.ime.Ime
import br.com.orcinus.orca.platform.ime.test.scope.close
import br.com.orcinus.orca.platform.ime.test.scope.ime
import br.com.orcinus.orca.platform.ime.test.scope.open
import br.com.orcinus.orca.platform.ime.test.scope.runImeTest
import kotlin.test.Test

internal class OnImeVisibilityChangeListenerTests {
  @Test
  fun listensToOpening() {
    runImeTest {
      open()
      assertThat(ime).isEqualTo(Ime.Open)
    }
  }

  @Test
  fun listensToClosing() {
    runImeTest {
      open()
      close()
      assertThat(ime).isEqualTo(Ime.Closed)
    }
  }
}
