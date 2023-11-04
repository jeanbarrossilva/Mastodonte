package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.feature.settings.termmuting.test.TestTermMuting
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.test.kit.input.text.onTextFieldErrors
import org.junit.Rule
import org.junit.Test

internal class TermMutingTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsErrorWhenTermIsEmptyAndMuteButtonIsClicked() {
    composeRule.setContent { OrcaTheme { TestTermMuting() } }
    composeRule.onNodeWithTag(SETTINGS_TERM_MUTING_MUTE_BUTTON).performClick()
    composeRule.onTextFieldErrors().assertIsDisplayed()
  }
}