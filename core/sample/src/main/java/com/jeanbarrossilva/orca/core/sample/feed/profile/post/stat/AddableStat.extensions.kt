/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post.stat

import com.jeanbarrossilva.orca.core.feed.profile.post.stat.addable.AddableStat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Create a sample [AddableStat].
 *
 * @param T Element to be either added or removed.
 * @param count Initial amount of elements.
 */
internal fun <T> createSampleAddableStat(count: Int = 0): AddableStat<T> {
  val elementsFlow = MutableStateFlow(emptyList<T>())
  return AddableStat(count) {
    get { elementsFlow }
    onAdd { element -> elementsFlow.update { elements -> elements + element } }
    onRemove { element -> elementsFlow.update { elements -> elements - element } }
  }
}
