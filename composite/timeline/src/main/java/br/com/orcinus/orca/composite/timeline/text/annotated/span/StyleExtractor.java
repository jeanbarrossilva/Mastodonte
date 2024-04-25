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

package br.com.orcinus.orca.composite.timeline.text.annotated.span;

import static br.com.orcinus.orca.composite.timeline.text.annotated.span.SpanStyles.ItalicSpanStyle;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.SpanStyles.contains;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.category.SpanStyles.getMention;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.category.SpanStyles.getUrl;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.category.SpanStyles.isForEmail;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.category.SpanStyles.isForHashtag;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.category.SpanStyles.isForLink;
import static br.com.orcinus.orca.composite.timeline.text.annotated.span.category.SpanStyles.isForMention;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.text.AnnotatedString;
import androidx.compose.ui.text.SpanStyle;
import br.com.orcinus.orca.std.markdown.Markdown;
import br.com.orcinus.orca.std.markdown.style.Style;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kotlin.ranges.IntRange;

/**
 * Extracts a {@link Markdown}'s {@link Style} from the {@link SpanStyle} of an {@link
 * AnnotatedString}.
 *
 * @see StyleExtractor#extract(SpanStyle, IntRange)
 */
public enum StyleExtractor {
  /** Extracts a {@link Style.Bold} {@link Style} from a {@link SpanStyle}. */
  BOLD {
    @Override
    boolean isExtractable(@NonNull SpanStyle spanStyle) {
      return contains(spanStyle, SpanStyles.BoldSpanStyle);
    }

    @NonNull
    @Override
    protected Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices) {
      return new Style.Bold(indices);
    }
  },

  /** Extracts an {@link Style.Email} {@link Style} from a {@link SpanStyle}. */
  EMAIL {
    @Override
    boolean isExtractable(@NonNull SpanStyle spanStyle) {
      return isForEmail(spanStyle);
    }

    @NonNull
    @Override
    protected Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices) {
      return new Style.Email(indices);
    }
  },

  /** Extracts a {@link Style.Hashtag} from a {@link SpanStyle}. */
  HASHTAG {
    @Override
    boolean isExtractable(@NonNull SpanStyle spanStyle) {
      return isForHashtag(spanStyle);
    }

    @NonNull
    @Override
    protected Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices) {
      return new Style.Hashtag(indices);
    }
  },

  /** Extracts an {@link Style.Italic} {@link Style} from a {@link SpanStyle}. */
  ITALIC {
    @Override
    boolean isExtractable(@NonNull SpanStyle spanStyle) {
      return contains(spanStyle, ItalicSpanStyle);
    }

    @NonNull
    @Override
    protected Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices) {
      return new Style.Italic(indices);
    }
  },

  /** Extracts a {@link Style.Link} from a {@link SpanStyle}. */
  LINK {
    @Override
    boolean isExtractable(@NonNull SpanStyle spanStyle) {
      return isForLink(spanStyle);
    }

    @NonNull
    @Override
    protected Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices)
        throws MalformedURLException {
      @NonNull URL url = getUrl(spanStyle);
      return Style.Link.to(url, indices);
    }
  },

  /** Extracts a {@link Style.Mention} from a {@link SpanStyle}. */
  MENTION {
    @Override
    boolean isExtractable(@NonNull SpanStyle spanStyle) {
      return isForMention(spanStyle);
    }

    @NonNull
    @Override
    protected Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices)
        throws MalformedURLException {
      return new Style.Mention(indices, getMention(spanStyle));
    }
  };

  /**
   * Extract all {@link Style}s that match the existing {@link StyleExtractor}s from the given
   * {@link SpanStyle}.
   *
   * @param spanStyle {@link SpanStyle} to extract the {@link Style}s from.
   * @param indices Indices at which the {@link SpanStyle} is applied.
   */
  public static List<Style> extractAll(@NonNull SpanStyle spanStyle, @NonNull IntRange indices)
      throws RuntimeException {
    return Arrays.stream(values())
        .map(
            styleExtractor -> {
              try {
                return styleExtractor.extract(spanStyle, indices);
              } catch (MalformedURLException exception) {
                throw new RuntimeException(exception);
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Returns whether a {@link Style} can be extracted from the {@link SpanStyle}.
   *
   * @param spanStyle {@link SpanStyle} from which a {@link Style} may be extracted.
   */
  abstract boolean isExtractable(@NonNull SpanStyle spanStyle);

  /**
   * Extracts a {@link Style} that matches this {@link StyleExtractor} from the given {@link
   * SpanStyle}.
   *
   * @param spanStyle {@link SpanStyle} to extract a {@link Style} from.
   * @param indices Indices at which the {@link SpanStyle} is applied.
   */
  @Nullable
  final Style extract(SpanStyle spanStyle, IntRange indices) throws MalformedURLException {
    if (isExtractable(spanStyle)) {
      return onExtract(spanStyle, indices);
    } else {
      return null;
    }
  }

  /**
   * Callback for when a {@link Style} that matches this {@link StyleExtractor} is requested to be
   * extracted from the given {@link SpanStyle}.
   *
   * @param spanStyle {@link SpanStyle} to extract a {@link Style} from.
   * @param indices Indices at which the {@link SpanStyle} is applied.
   */
  @NonNull
  protected abstract Style onExtract(@NonNull SpanStyle spanStyle, @NonNull IntRange indices)
      throws MalformedURLException;
}
