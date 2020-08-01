package com.jewong.popularmovies;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        SortBy.MOST_POPULAR,
        SortBy.HIGHEST_RATED,
        SortBy.FAVORITES})
@Retention(RetentionPolicy.SOURCE)
public @interface SortBy {

    String MOST_POPULAR = "popular";

    String HIGHEST_RATED = "top_rated";

    String FAVORITES = "favorites";

}
