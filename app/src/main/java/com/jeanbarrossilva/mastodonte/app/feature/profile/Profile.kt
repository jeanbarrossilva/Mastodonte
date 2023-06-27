package com.jeanbarrossilva.mastodonte.app.feature.profile

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeanbarrossilva.mastodon.feature.profile.Profile
import com.jeanbarrossilva.mastodon.feature.profile.ProfileNavigator
import com.jeanbarrossilva.mastodon.feature.profile.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodon.feature.profile.viewmodel.ProfileViewModel
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.sample
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.compose.koinInject

@Composable
@Destination
@RootNavGraph(start = true)
internal fun Profile(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
    modifier: Modifier = Modifier
) {
    val application = koinInject<Application>()
    val repository = koinInject<ProfileRepository>()
    val viewModelFactory =
        ProfileViewModel.createFactory(application, repository, Profile.sample.id)
    val viewModel = viewModel<ProfileViewModel>(factory = viewModelFactory)
    val navigator = koinInject<ProfileNavigator>()

    Profile(
        viewModel,
        navigator,
        BackwardsNavigationState.Unavailable,
        onBottomAreaAvailabilityChangeListener,
        modifier
    )
}
