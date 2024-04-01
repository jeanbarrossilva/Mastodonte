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

package com.jeanbarrossilva.orca.platform.navigation.test.activity

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.jeanbarrossilva.orca.platform.navigation.navigator
import kotlin.test.Test
import org.junit.Rule

internal class FragmentActivityExtensionsTests {
  @get:Rule val activityScenarioRule = ActivityScenarioRule(NavigationActivity::class.java)

  @Test
  fun getsNavigatorWhenMadeNavigable() {
    activityScenarioRule.scenario.onActivity {
      it.makeNavigable()
      it.navigator
    }
  }
}
