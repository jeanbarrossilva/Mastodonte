/*
 * Copyright © 2023–2024 Orcinus
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

import br.com.orcinus.orca.namespaceFor

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  namespace = namespaceFor("feature.profiledetails")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(project(":core:sample-test"))
  androidTestImplementation(project(":feature:profile-details-test"))
  androidTestImplementation(project(":platform:navigation-test"))
  androidTestImplementation(project(":platform:testing"))
  androidTestImplementation(project(":std:injector-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.compose.ui.test.manifest)
  androidTestImplementation(libs.kotlin.test)
  androidTestImplementation(libs.loadable.placeholder.test)

  api(project(":core"))
  api(project(":composite:composable"))
  api(project(":platform:navigation"))
  api(libs.android.compose.ui)

  ksp(project(":std:injector-processor"))

  implementation(project(":composite:timeline"))
  implementation(project(":core"))
  implementation(project(":ext:coroutines"))
  implementation(project(":platform:intents"))
  implementation(project(":std:injector"))
  implementation(libs.android.fragment.ktx)
  implementation(libs.android.lifecycle.viewmodel)
  implementation(libs.loadable.list)
  implementation(libs.loadable.placeholder)

  testImplementation(project(":composite:timeline-test"))
  testImplementation(project(":core:sample-test"))
  testImplementation(project(":feature:profile-details-test"))
  testImplementation(project(":platform:navigation-test"))
  testImplementation(project(":platform:autos-test"))
  testImplementation(project(":platform:testing"))
  testImplementation(project(":std:injector-test"))
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.robolectric)
}
