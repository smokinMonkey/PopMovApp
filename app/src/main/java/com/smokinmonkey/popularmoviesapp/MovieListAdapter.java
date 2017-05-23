package com.smokinmonkey.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by smokinMonkey on 4/22/2017.
 */
public class MovieListAdapter extends BaseAdapter {

    public Context mContext;
    public Cursor cursorMovie;

    public MovieListAdapter(Context context) {
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

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.movie_list_item, parent, false);
        }

        ImageView mMoviePoster = (ImageView) convertView.findViewById(R.id.ivMoviePoster);

        //TextView mMovieTitle = (TextView) convertView.findViewById(R.id.tvMovieTitle);
        //mMovieTitle.setText(mlaMovieList[position].getOriginalTitle());

        /*
        Picasso
                .with(mContext)
                .load(cMovieList[position].getString())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(mMoviePoster);
        */

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(mContext, destinationClass);

                Bundle b = new Bundle();
                //b.putSerializable("MovieDetails", cMovieList[position]);
                intentToStartDetailActivity.putExtras(b);

                mContext.startActivity(intentToStartDetailActivity);

                /*  display toast message testing purpose
                Toast.makeText(mContext, "Movie Title: " + mlaMovieList[position]
                        .getOriginalTitle(), Toast.LENGTH_LONG).show();
                */
            }
        });

        return convertView;
    }

    public void swapCursor(Cursor data) {
        cursorMovie = data;
        notifyDataSetChanged();
    }
}