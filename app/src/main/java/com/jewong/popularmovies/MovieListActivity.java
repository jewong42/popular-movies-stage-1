package com.jewong.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.data.MovieList;
import com.jewong.popularmovies.rest.MovieAPIClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterCallback {

    private static final int COLUMN_COUNT = 3;
    private static final String EXTRA_MOVIE = "com.jewong.popularmovies.MOVIE";
    RecyclerView mRecyclerView;
    Spinner mSpinner;
    View mProgressBar;
    MovieAPIClient mMovieAPIClient = new MovieAPIClient();
    MovieAdapter mMovieAdapter = new MovieAdapter(null, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        initializeSpinner();
        initializeRecyclerView();
    }

    private void bindViews() {
        mSpinner = findViewById(R.id.spinner);
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar_container);
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
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(view instanceof TextView)) return;
                String label = ((TextView) view).getText().toString();
                String sortBy = getSortBy(label);
                if (!TextUtils.isEmpty(sortBy)) getData(sortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showErrorToast();
            }
        });
    }

    /**
     * Sets up the recycler view.
     */
    private void initializeRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    /**
     * Checks spinner label and returns the appropriate sortBy query.
     * @param label String to check.
     * @return sortBy query.
     */
    private String getSortBy(String label) {
        if (label.equals(getString(R.string.highest_rated))) {
            return getString(R.string.vote_average_desc);
        } else if (label.equals(getString(R.string.most_popular))) {
            return getString(R.string.popularity_desc);
        } else {
            return "";
        }
    }

    /**
     * Makes a call to fetch the appropriate list of movies.
     * @param sortBy sortBy query.
     */
    private void getData(String sortBy) {
        mProgressBar.setVisibility(View.VISIBLE);
        mMovieAPIClient.getMovies(sortBy, new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.body() != null && response.body().getResults() != null) {
                    final ArrayList<Movie> movieList = new ArrayList<>();
                    movieList.addAll(response.body().getResults());
                    mMovieAdapter.setData(response.body().getResults());
                } else {
                    showErrorToast();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                showErrorToast();
                mProgressBar.setVisibility(View.GONE);
            }
        });
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
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }

}