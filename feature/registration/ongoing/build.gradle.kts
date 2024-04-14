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

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  androidTestImplementation(project(":platform:navigation-test"))
  androidTestImplementation(project(":std:injector-test"))
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.kotlin.test)

  api(project(":composite:composable"))
  api(project(":platform:navigation"))

  implementation(project(":composite:status"))
  implementation(project(":core:sample"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:stack"))
  implementation(libs.android.fragment.ktx)
  implementation(libs.loadable)

  ksp(project(":std:injector-processor"))

  testImplementation(libs.assertk)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.turbine)
}
