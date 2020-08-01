package com.jewong.popularmovies;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        State.FAILED,
        State.INITIALIZING,
        State.LOADING})
@Retention(RetentionPolicy.SOURCE)
public @interface State {

    int FAILED = -1;

    int INITIALIZING = 0;

    int LOADING = 1;

    int IDLE = 2;


}
