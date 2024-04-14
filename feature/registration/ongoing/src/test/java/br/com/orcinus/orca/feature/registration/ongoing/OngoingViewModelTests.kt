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

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import br.com.orcinus.orca.core.instance.registration.Credentials
import br.com.orcinus.orca.core.instance.registration.Registration
import br.com.orcinus.orca.core.sample.instance.registration.SampleRegistrar
import br.com.orcinus.orca.core.sample.instance.registration.sample
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.flow.filterIsLoaded
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

internal class OngoingViewModelTests {
  private lateinit var viewModel: OngoingViewModel

  @BeforeTest
  fun setUp() {
    @OptIn(ExperimentalCoroutinesApi::class) Dispatchers.setMain(StandardTestDispatcher())
    viewModel = with(Credentials.sample) { OngoingViewModel(SampleRegistrar, email, password) }
  }

  @Test
  fun registers() {
    viewModel.register()
    runTest {
      viewModel.registrationLoadableFlow
        .filterIsLoaded()
        .test(validate = TurbineTestContext<Loadable<Registration>>::awaitItem)
    }
  }
}
