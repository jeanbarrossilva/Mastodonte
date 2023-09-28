package com.jeanbarrossilva.orca.app.demo

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.app.demo.module.core.DemoCoreModule
import com.jeanbarrossilva.orca.app.module.AppModule
import com.jeanbarrossilva.orca.core.http.get

internal class DemoOrcaActivity : OrcaActivity() {
    override val appModule = AppModule(this)
    override val coreModule = DemoCoreModule()
}
