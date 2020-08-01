package com.jewong.popularmovies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jewong.popularmovies.R;
import com.jewong.popularmovies.SortBy;
import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.databinding.ActivityMovieListBinding;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterCallback {

    private static final int COLUMN_COUNT = 3;
    private static final String EXTRA_MOVIE = "com.jewong.popularmovies.MOVIE";
    ActivityMovieListBinding mBinding;
    MovieListViewModel mMovieListViewModel;
    MovieAdapter mMovieAdapter = new MovieAdapter(null, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);
        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        initializeRecyclerView();
        initializeSpinner();
        initializeObservers();
    }

    /**
     * Sets up the recycler view.
     */
    private void initializeRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));
        mBinding.recyclerView.setAdapter(mMovieAdapter);
    }

    /**
     * Sets up the spinner view.
     */
    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.discovery_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinner.setAdapter(adapter);
        mBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(view instanceof TextView)) return;
                String label = ((TextView) view).getText().toString();
                String sortBy = getSortByValue(label);
                if (!TextUtils.isEmpty(sortBy)) {
                    if (sortBy.equals(SortBy.FAVORITES)) mMovieListViewModel.getFavorites();
                    else mMovieListViewModel.loadData(sortBy);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showErrorToast();
            }
        });
    }

    /**
     * Checks spinner label and returns the appropriate sortBy query.
     * @param label String to check.
     * @return sortBy query.
     */
    private String getSortByValue(String label) {
        if (label.equals(getString(R.string.highest_rated_label))) {
            return SortBy.HIGHEST_RATED;
        } else if (label.equals(getString(R.string.popular_label))) {
            return SortBy.MOST_POPULAR;
        } else if (label.equals(getString(R.string.favorites_label))) {
            return SortBy.FAVORITES;
        } else {
            return "";
        }
    }

    private void initializeObservers() {
        mMovieListViewModel.mMovieList.observe(this, this::UpdateAdapter);
        mMovieListViewModel.mIsLoading.observe(this, isLoading -> {
            if (isLoading) return;
            showProgressBar(false);
        });
        mMovieListViewModel.mHasFailedToLoad.observe(this, hasFailedToLoad -> {
            if (!hasFailedToLoad) return;
            showErrorToast();
            showProgressBar(false);
        });
    }

    private void UpdateAdapter(List<Movie> movies) {
        MovieAdapter adapter = (MovieAdapter) mBinding.recyclerView.getAdapter();
        adapter.setData(movies);
    }

    private void showProgressBar(Boolean showProgressBar) {
        int visibility = showProgressBar ? View.VISIBLE : View.GONE;
        mBinding.progressBarContainer.setVisibility(visibility);
    }

    /**
     * Shows an error toast. Default cases for handling errors and failures.
     */
    private void showErrorToast() {
        Toast.makeText(getBaseContext(), getString(R.string.data_error), Toast.LENGTH_LONG).show();
    }

    /**
     * Launches the details activity.
     * @param movie Movie to show in the details activity.
     */
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

}