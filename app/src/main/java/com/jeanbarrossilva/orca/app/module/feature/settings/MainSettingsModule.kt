package com.jeanbarrossilva.orca.app.module.feature.settings

import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.termMuter
import com.jeanbarrossilva.orca.feature.settings.SettingsModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainSettingsModule(private val navigator: Navigator) :
  SettingsModule(
    { Injector.from<CoreModule>().termMuter() },
    { NavigatorSettingsBoundary(navigator) }
  )
