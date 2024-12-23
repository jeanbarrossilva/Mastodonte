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

package br.com.orcinus.orca.composite.timeline.search.field

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.orca.composite.timeline.test.search.field.onDismissButton
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ResultSearchTextFieldPopupTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun shows() {
    composeRule.activity.setContent { ResultSearchTextFieldPopup() }
    composeRule.onSearchTextField().assertIsDisplayed()
  }

  @Test
  fun recomposesWhenQueryChanges() {
    composeRule.setContent {
      var query by remember { mutableStateOf("") }
      ResultSearchTextFieldPopup(query = query, onQueryChange = { query = it })
    }
    composeRule.onSearchTextField().apply { performTextInput("❤️‍🩹") }.assertTextEquals("❤️‍🩹")
  }

  @Test
  fun dismisses() {
    composeRule.activity.setContent { ResultSearchTextFieldPopup() }
    composeRule.activity.setContent {}
    composeRule.onSearchTextField().assertDoesNotExist()
  }

  @Test
  fun dismissesWhenBackPressing() {
    composeRule.activity.setContent { ResultSearchTextFieldPopup() }
    pressBack()
    composeRule.onSearchTextField().assertDoesNotExist()
  }

  @Test
  fun dismissesWhenClickingDismissalButton() {
    composeRule.activity.setContent { ResultSearchTextFieldPopup() }
    composeRule.onDismissButton().performClick()
    composeRule.onSearchTextField().assertDoesNotExist()
  }

  @Test
  fun deconfiguresViewTreeOwnershipWhenDismissed() {
    val originalOwnedTreeView = View(composeRule.activity)
    composeRule.activity.setContentView(originalOwnedTreeView)
    val originalViewTreeOwner = ViewTreeOwner.of(originalOwnedTreeView)
    composeRule.activity.setContent { ResultSearchTextFieldPopup() }
    composeRule.onDismissButton().performClick()
    assertThat(ViewTreeOwner)
      .prop("from") { it.from(composeRule.activity) }
      .isEqualTo(originalViewTreeOwner)
  }

  @Test
  fun listensToDismissal() {
    var didDismiss = false
    composeRule.activity.setContent {
      ResultSearchTextFieldPopup(onDismissal = { didDismiss = true })
    }
    composeRule.onDismissButton().performClick()
    composeRule.waitForIdle()
    assertThat(didDismiss, "hasNotified").isTrue()
  }
}
