package com.example.ph_data01221240053.movies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**********************************************************************************************************************************************/

class MoviesAdapter extends BaseAdapter {
    private Context adapterContext;
    private ArrayList<Movie> moviesList;
    private int listCounter;
    private ImageView myImageForAdapter;
    /**********************************************************************************************************************************************/

    private void fillImage(String path, ImageView image) {
        Picasso.with(getAdapterContext()).load(Data.imageBase + path).into(image);

    }

    private ImageView convertImage(View view, int id) {
        return (ImageView) view.findViewById(id);
    }

    public void setMyImageForAdapter(ImageView myImageForAdapter) {
        this.myImageForAdapter = myImageForAdapter;
    }

    public ImageView getMyImageForAdapter() {
        return myImageForAdapter;
    }

    public void setListCounter(int listCounter) {
        this.listCounter = listCounter;
    }

    public int getListCounter() {
        return listCounter;
    }

    public void setAdapterContext(Context adapterContext) {
        this.adapterContext = adapterContext;
    }

    public Context getAdapterContext() {
        return adapterContext;
    }

    public void setMoviesList(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public ArrayList<Movie> getMoviesList() {
        return moviesList;
    }

    public MoviesAdapter(Activity context, ArrayList<Movie> moviesList) {
        setAdapterContext(context);
        setMoviesList(moviesList);
    }

    @Override
    public int getCount() {
        setListCounter(getMoviesList().size());
        return getListCounter();
    }

    @Override
    public Movie getItem(int i) {
        return getMoviesList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View rootView, ViewGroup parent) {

        if (rootView == null) {
            LayoutInflater inf = (LayoutInflater) getAdapterContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inf.inflate(R.layout.my_image_to_show, parent, false);
        }

        String path = getItem(position).getPosterPath();
        setMyImageForAdapter(convertImage(rootView, R.id.iv_movie_poster));
        fillImage(path, getMyImageForAdapter());
        //iconView.setImageResource(R.drawable.mn);
         // Picasso.with(activity).load("http://image.tmdb.org/t/p/w185//9HE9xiNMEFJnCzndlkWD7oPfAOx.jpg").into(iconView);
        return rootView;
    }
}
/**********************************************************************************************************************************************/
