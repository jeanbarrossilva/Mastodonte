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

package br.com.orcinus.orca.feature.registration.ongoing

import assertk.assertThat
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.core.sample.instance.registration.SampleRegistrar
import br.com.orcinus.orca.core.sample.instance.registration.sample
import br.com.orcinus.orca.platform.navigation.navigator
import br.com.orcinus.orca.platform.navigation.test.activity.launchNavigationActivity
import br.com.orcinus.orca.platform.navigation.test.isAt
import br.com.orcinus.orca.std.injector.module.injection.injectionOf
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import kotlin.test.Test
import org.junit.Rule

internal class OngoingFragmentTests {
  @get:Rule
  val injectorRule = InjectorTestRule { register(OngoingModule(injectionOf { SampleRegistrar })) }

  @Test
  fun navigates() {
    launchNavigationActivity().use { scenario ->
      scenario.onActivity { activity ->
        OngoingFragment.navigate(
          activity.navigator,
          Credentials.sample.email,
          Credentials.sample.password
        )
        assertThat(activity).isAt(OngoingFragment.ROUTE)
      }
    }
  }
}
