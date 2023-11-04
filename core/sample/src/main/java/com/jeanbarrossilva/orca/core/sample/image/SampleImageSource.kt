package com.jeanbarrossilva.orca.core.sample.image

import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Source from which [Image]s will be loaded via an [ImageLoader] within the sample core variant.
 */
sealed class SampleImageSource {
  /** [SampleImageSource] that indicates a nonexistent source. */
  data object None : SampleImageSource()
}