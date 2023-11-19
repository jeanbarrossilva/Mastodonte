package com.jeanbarrossilva.orca.platform.autos.forms

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.jeanbarrossilva.orca.autos.forms.Forms
import com.jeanbarrossilva.orca.platform.autos.noLocalProvidedFor

/** [CompositionLocal] that provides [Forms]. */
internal val LocalForms = compositionLocalOf<Forms> { noLocalProvidedFor("LocalForms") }