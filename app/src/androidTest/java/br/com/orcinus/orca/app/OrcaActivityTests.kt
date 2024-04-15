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

package br.com.orcinus.orca.app

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runAndroidComposeUiTest
import assertk.assertThat
import assertk.assertions.isNotNull
import br.com.orcinus.orca.app.activity.OrcaActivity
import br.com.orcinus.orca.core.mastodon.test.auth.authorization.onRegisterButton
import br.com.orcinus.orca.feature.registration.RegistrationFragment
import br.com.orcinus.orca.platform.navigation.test.isAt
import kotlin.test.Test

internal class OrcaActivityTests {
  @Test
  fun navigatesToRegistrationWhenRequested() {
    @OptIn(ExperimentalTestApi::class)
    runAndroidComposeUiTest<OrcaActivity> {
      onRegisterButton().performClick()
      assertThat(activity).isNotNull().isAt(RegistrationFragment.ROUTE)
    }
  }
}
