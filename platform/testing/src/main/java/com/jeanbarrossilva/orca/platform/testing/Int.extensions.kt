/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.testing

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Gets the [String] from this resource ID.
 *
 * @param format [String] by which arguments in the [String] will be replaced.
 */
fun @receiver:StringRes Int.asString(vararg format: String): String {
  return try {
    context.getString(this, *format)
  } catch (_: Resources.NotFoundException) {
    InstrumentationRegistry.getInstrumentation().targetContext.getString(this, *format)
  }
}
