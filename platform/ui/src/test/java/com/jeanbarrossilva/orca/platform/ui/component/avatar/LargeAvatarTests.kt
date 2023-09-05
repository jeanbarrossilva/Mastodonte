package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.platform.ui.component.AVATAR_TAG
import com.jeanbarrossilva.orca.platform.ui.component.Avatar
import com.jeanbarrossilva.orca.platform.ui.component.LargeAvatar
import com.jeanbarrossilva.orca.platform.ui.core.image.test.rememberTestImageProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LargeAvatarTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isTaggedWhenEmpty() {
        composeRule.setContent { LargeAvatar() }
        composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
    }

    @Test
    fun isLoadingWhenEmpty() {
        composeRule.setContent { LargeAvatar() }
        composeRule.onNodeWithTag(AVATAR_TAG).assertIsLoading()
    }

    @Test
    fun isTaggedWhenPopulated() {
        composeRule.setContent {
            LargeAvatar(
                Avatar.sample.name,
                Avatar.sample.url,
                imageProvider = rememberTestImageProvider()
            )
        }
        composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
    }

    @Test
    fun isLoadedWhenPopulated() {
        composeRule.setContent {
            LargeAvatar(
                Avatar.sample.name,
                Avatar.sample.url,
                imageProvider = rememberTestImageProvider()
            )
        }
        composeRule.onNodeWithTag(AVATAR_TAG).assertIsNotLoading()
    }
}
