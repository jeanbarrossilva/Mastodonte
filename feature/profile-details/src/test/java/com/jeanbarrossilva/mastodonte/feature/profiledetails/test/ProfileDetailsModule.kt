package com.jeanbarrossilva.mastodonte.feature.profiledetails.test

import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun ProfileDetailsModule(): Module {
    return module {
        single<ProfileProvider> { SampleProfileProvider }
        single<TootProvider> { SampleTootProvider }
        single<ProfileDetailsBoundary> { TestProfileDetailsBoundary() }
        single { OnBottomAreaAvailabilityChangeListener.empty }
    }
}
