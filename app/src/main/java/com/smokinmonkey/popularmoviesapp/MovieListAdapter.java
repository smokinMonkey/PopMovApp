package com.smokinmonkey.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.smokinmonkey.popularmoviesapp.data.MovieDbContract;
import com.squareup.picasso.Picasso;

/**
 * Created by smokinMonkey on 4/22/2017.
 */
public class MovieListAdapter extends BaseAdapter {

    private Context mContext;
    private Cursor cursorMovie;

    public MovieListAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    public void setMovieData(Cursor movieData) {
        cursorMovie = movieData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (cursorMovie == null) return 0;
        return cursorMovie.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursorMovie.getPosition();
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        cursorMovie.moveToPosition(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.movie_list_item, parent, false);
        }

        ImageView mMoviePoster = (ImageView) convertView.findViewById(R.id.ivMoviePoster);

        Picasso
                .with(mContext)
                .load(cursorMovie.getString(MainActivity.INDEX_POSTER_STR))
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(mMoviePoster);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cursorMovie.moveToPosition(position);

                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(mContext, destinationClass);
                // build URI with movie id selected and set data with intent and start intent
                Uri uriForMovieClicked = MovieDbContract.MovieEntry
                        .buildMovieUriWithId(cursorMovie.getInt(MainActivity.INDEX_MOVIE_ID));

                Log.d("IMPORTANT", "URI for selected movie: " + uriForMovieClicked.toString());

                intentToStartDetailActivity.setData(uriForMovieClicked);

                mContext.startActivity(intentToStartDetailActivity);
            }
        });

        return convertView;
    }

    public void swapCursor(Cursor data) {
        cursorMovie = data;
        notifyDataSetChanged();
    }
}