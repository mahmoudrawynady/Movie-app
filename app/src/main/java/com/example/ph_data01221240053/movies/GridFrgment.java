package com.example.ph_data01221240053.movies;

import java.io.Serializable;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**********************************************************************************************************************************************/

public class GridFrgment extends Fragment implements Serializable{
    private SQLiteDatabase mydata;
    private boolean towPane = false;
    private boolean topRatedEmpty = true;
    private boolean popularEmpty = true;
    private Movie movieToFillWithReviews;
    private ArrayList<Movie> popularList = new ArrayList<>();
    private ArrayList<Movie> top_ratedList = new ArrayList<>();
    private ArrayList<Movie> favoritList = new ArrayList<>();
    private GridView movieGrid;
    private MoviesAdapter mAdapter;
    private String showTyp = "popular";
    private ArrayList<String> popularIds = new ArrayList<>();
    private boolean befor = false;
    int index;
    Fragment f2;

    Menu men;
MenuItem mitem;



    /**********************************************************************************************************************************************/

    public void setBefor(boolean befor) {
        this.befor = befor;
    }

    /**********************************************************************************************************************************************/


    public boolean isBefor() {
        return befor;
    }

    /**********************************************************************************************************************************************/
    @Override
   public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = movieGrid.getFirstVisiblePosition();
        editor.putInt("index",index);
        editor.commit();
    }

    /**********************************************************************************************************************************************/


    public void fillPopularWithIds() {
        for (int i = 0; i < getPopularList().size(); i++) {
            String id = Integer.toString(getPopularList().get(i).getId());
            popularIds.add(id);

        }
    }
    /**********************************************************************************************************************************************/

    public void fillPrefrence() {
        if (isBefor() == false) {
            SharedPreferences pref = getActivity().getSharedPreferences("MyStore", 0);
            SharedPreferences.Editor save = pref.edit();
            save.putString("popular", "popular");
            int size = getPopularIds().size();
            save.putInt("size", size);

            for (int i = 0; i < getPopularList().size(); i++) {
                ArrayList<String> myData = new ArrayList<>();
                Movie popMovie = getPopularList().get(i);
                String popularId = getPopularIds().get(i);
                myData.add(0, popMovie.getTitle());
                myData.add(1, popMovie.getPosterPath());
                myData.add(2, popMovie.getOverView());
                myData.add(3, popMovie.getDate());
                myData.add(4, Double.toString(popMovie.getVote()));
                myData.add(5, Boolean.toString(popMovie.isFavorir()));
                myData.add(6, popMovie.getVideoPath());
                myData.add(7, popMovie.getReviews());
                save.putString(Integer.toString(i), concateData(myData));
            }
            save.putBoolean("before", true);

            save.commit();

        }


    }

    /**********************************************************************************************************************************************/

    public void addMovie(String data) {
        Movie movie = new Movie();
        String[] details = data.split("&");
        movie.setTitle(details[0]);
        movie.setPosterPath(details[1]);
        movie.setOverView(details[2]);
        movie.setDate(details[3]);
        movie.setVote(Double.valueOf(details[4]));
        movie.setFavorir(Boolean.getBoolean(details[5]));
        movie.setVideoPath(details[6]);
        movie.setReviews(details[7]);
        popularList.add(movie);


    }



    /**********************************************************************************************************************************************/

    public String concateData(ArrayList<String> data) {
        String concated = "";
        char falg = '&';
        for (int i = 0; i < data.size(); i++) {
            concated += data.get(i) + falg;


        }
        return concated;
    }

    /**********************************************************************************************************************************************/


    public void setPopularEmpty(boolean popularEmpty) {
        this.popularEmpty = popularEmpty;
    }
    /**********************************************************************************************************************************************/

    public ArrayList<String> getPopularIds() {
        return popularIds;
    }
    /**********************************************************************************************************************************************/


    public void fillIntent(Intent i) {
        String[] projection = {
               MyProvider._ID,
        };
        Cursor c=  getActivity().getContentResolver().query(MyProvider.myUrl, projection, null, null, null);
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(MyProvider._ID));
            if (getMovieToFillWithReviews().getId() == id) {
                getMovieToFillWithReviews().setFavorir(true);
            }
        }
        c.close();
        i.putExtra(Data.idKey, getMovieToFillWithReviews().getId());
        i.putExtra(Data.titleKey, getMovieToFillWithReviews().getTitle());
        i.putExtra(Data.dateKey, getMovieToFillWithReviews().getDate());
        i.putExtra(Data.overViewKey, getMovieToFillWithReviews().getOverView());
        i.putExtra(Data.pathKey, getMovieToFillWithReviews().getPosterPath());
        i.putExtra(Data.reviewKey, getMovieToFillWithReviews().getReviews());
        i.putExtra(Data.voteKey, getMovieToFillWithReviews().getVote());
        i.putExtra(Data.favorKey, getMovieToFillWithReviews().isFavorir());
        i.putExtra(Data.videoBase, getMovieToFillWithReviews().getVideoPath());
    }
    /**********************************************************************************************************************************************/

    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;

    }
    /**********************************************************************************************************************************************/

    public boolean checkTowPane() {
        if (null != getActivity().findViewById(R.id.details)) {
            towPane = true;
        }
        return isTowPane();

    }

    /**********************************************************************************************************************************************/

    public boolean isTowPane() {
        return towPane;
    }
    /**********************************************************************************************************************************************/

    public boolean isPopularEmpty() {
        return popularEmpty;
    }
    /**********************************************************************************************************************************************/

    public void setMovieToFillWithReviews(Movie movieToFillWithReviews) {
        this.movieToFillWithReviews = movieToFillWithReviews;
    }
    /**********************************************************************************************************************************************/

    public Movie getMovieToFillWithReviews() {
        return movieToFillWithReviews;
    }

    public void setTopRatedEmpty(boolean topRatedEmpty) {
        this.topRatedEmpty = topRatedEmpty;
    }

    public void setTopListEmpty() {
        if (getTop_ratedList().isEmpty())
            setTopRatedEmpty(true);


    }
    /**********************************************************************************************************************************************/


    public void setFavoritIfTowPane(boolean fav) {
        if (getMovieToFillWithReviews().isFavorir() == false && fav == true) {
            getMovieToFillWithReviews().setFavorir(true);
            if (getFavoritLiat().contains(getMovieToFillWithReviews()))
                return;
            getFavoritLiat().add(getMovieToFillWithReviews());
        } else if (getMovieToFillWithReviews().isFavorir() == true && fav == false) {
            getMovieToFillWithReviews().setFavorir(false);
            getFavoritLiat().remove(getMovieToFillWithReviews());
        }

    }

    /**********************************************************************************************************************************************/

    public boolean isTopRatedEmptyEmpty() {
        return topRatedEmpty;
    }

    public void setShowTyp(String showTyp) {
        this.showTyp = showTyp;
    }

    public String getShowTyp() {
        return showTyp;
    }

    public void setmAdapter(MoviesAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public MoviesAdapter getmAdapter() {
        return mAdapter;
    }

    public void setMovieGrid(GridView movieGrid) {
        this.movieGrid = movieGrid;
    }

    public GridView getMovieGrid() {
        return movieGrid;
    }

    public void setPopularList(ArrayList<Movie> popularList) {
        this.popularList = popularList;
    }

    public ArrayList<Movie> getPopularList() {
        return popularList;
    }

    public void setFavoritLiat(ArrayList<Movie> favoritList) {
        this.favoritList = favoritList;
    }

    public ArrayList<Movie> getFavoritLiat() {
        return favoritList;
    }

    public void setTop_ratedList(ArrayList<Movie> top_ratedList) {
        this.top_ratedList = top_ratedList;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    */

    public void filMovieWithReviews(String Data) {
        try {

            JSONObject reviewObject = new JSONObject(Data);
            JSONArray reviewArray = reviewObject.getJSONArray("results");
            String review = reviewArray.getJSONObject(0).getString("content");
            getMovieToFillWithReviews().setReviews(review);
        } catch (JSONException e) {
            System.err.println("Error1");
        }
    }
    /**********************************************************************************************************************************************/

    public ArrayList<Movie> getTop_ratedList() {
        return top_ratedList;
    }


    public ArrayList<Movie> fillMovies(String Data) {
        try {
            JSONObject movieObject = new JSONObject(Data);
            ArrayList<Movie> movies = new ArrayList<>();
            JSONArray movieArray = movieObject.getJSONArray("results");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject jsonObject = movieArray.getJSONObject(i);

                String mPath = jsonObject.getString("poster_path");
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String release_date = jsonObject.getString("release_date");
                String overview = jsonObject.getString("overview");
                double vote_average = jsonObject.getDouble("vote_average");

                Movie movie = new Movie();
                movie.setTitle(title);
                movie.setPosterPath(mPath);
                movie.setDate(release_date);
                movie.setId(id);
                movie.setOverView(overview);
                movie.setVote(vote_average);
                // m.setMovie_vote_average(vote_average);
                // m.setMovie_id(id);
                movies.add(movie);

            }
            if (getShowTyp() == "popular")
                setPopularList(movies);
            else
                setTop_ratedList(movies);

            return movies;
        } catch (JSONException e) {
            System.err.println("Error1");
            // to parse it.
            return null;
        }
    }







    /**********************************************************************************************************************************************/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridlayout, container, false);
        setMovieGrid((GridView) view.findViewById(R.id.gridView));
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int typ = sharedPref.getInt("data", 2);
        movieRequest myRequest = new movieRequest();

        /**********************************************************************************************************************************************/
           index=sharedPref.getInt("index", 1);
        movieGrid.setSelection(index);






        if (typ == 2) {
            setShowTyp("popular");
            myRequest.execute("http://api.themoviedb.org/3/movie/popular?api_key=fa7e0aeaf6a6db5397d72702703cae47");
        }
         else if (typ == 1) {

            setShowTyp("top_rated");
            myRequest.execute("http://api.themoviedb.org/3/movie/top_rated?api_key=fa7e0aeaf6a6db5397d72702703cae47");
        }
        else
        {
            setShowTyp("favorit");
            String[] projection = {
                   MyProvider._ID,
                   MyProvider.title,
                   MyProvider.posterPath,
                    MyProvider.overView,
                    MyProvider.date,
                    MyProvider.vote,
                    MyProvider.reviews
            };
            Cursor c=  getActivity().getContentResolver().query(MyProvider.myUrl, projection, null, null, null);

            while(c.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(c.getInt(c.getColumnIndex(MyProvider._ID)));
                movie.setTitle(c.getString(c.getColumnIndex(MyProvider.title)));
                movie.setPosterPath(c.getString(c.getColumnIndex(MyProvider.posterPath)));
                movie.setOverView(c.getString(c.getColumnIndex(MyProvider.overView)));
                movie.setDate(c.getString(c.getColumnIndex(MyProvider.date)));
                movie.setVote(c.getDouble(c.getColumnIndex(MyProvider.vote)));
                movie.setReviews(c.getString(c.getColumnIndex(MyProvider.reviews)));

                movie.setFavorir(true);
                favoritList.add(movie);


            }
           c.close();

            getMovieGrid().setAdapter(new MoviesAdapter(getActivity(), getFavoritLiat()));

        }








        /**********************************************************************************************************************************************/

        getMovieGrid().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Movie item = (Movie) adapterView.getItemAtPosition(i);
                if (getShowTyp() == "popular") {
                    setMovieToFillWithReviews(getPopularList().get(i));
                    if (getMovieToFillWithReviews().getReviews() == null)
                        new reviewsTask().execute(Data.baseRequest + getMovieToFillWithReviews().getId() + Data.review + Data.appiKey);
                    else {
                        setMovieToFillWithReviews(getPopularList().get(i));
                        Intent i2 = new Intent(getActivity(), Details.class);
                        fillIntent(i2);


                        if (!checkTowPane())
                            startActivityForResult(i2, 0);
                        else {

                            getActivity().setIntent(i2);
                            FragmentManager f = getFragmentManager();
                            f2 = f.findFragmentById(R.id.details);

                            if (f2 == null) {
                                f2 = new DetailsFragment();
                                f2.setRetainInstance(true);
                                f.beginTransaction().add(R.id.details, f2).commit();

                            } else {
                                //f.beginTransaction().remove(f2);

                                //f2 = new DetailsFragment();
                                //f2.setRetainInstance(true);

                                //f.beginTransaction().add(R.id.details, f2).commit();
                            }
                        }


                    }
                }
                /**********************************************************************************************************************************************/

                else if (getShowTyp() == "top_rated") {

                    setMovieToFillWithReviews(getTop_ratedList().get(i));
                    if (getMovieToFillWithReviews().getReviews() == null)
                        new reviewsTask().execute("https://api.themoviedb.org/3/movie/" + getMovieToFillWithReviews().getId() + "/reviews?api_key=fa7e0aeaf6a6db5397d72702703cae47&language=en-US");
                    else {
                        setMovieToFillWithReviews(getPopularList().get(i));
                        Intent i2 = new Intent(getActivity(), Details.class);
                        fillIntent(i2);
                        if (!checkTowPane())
                            startActivityForResult(i2, 0);
                        else {

                            getActivity().setIntent(i2);
                            FragmentManager f = getFragmentManager();
                            f2 = f.findFragmentById(R.id.details);

                            if (f2 == null) {
                                f2 = new DetailsFragment();
                                f2.setRetainInstance(true);
                                f.beginTransaction().add(R.id.details, f2).commit();

                            } else {
                                //f.beginTransaction().remove(f2);
                                // f2 = new DetailsFragment();
                                //f2.setRetainInstance(true);
                                //f.beginTransaction().add(R.id.details, f2).commit();
                            }
                        }


                    }
                }
                /**********************************************************************************************************************************************/

                else {
                    setMovieToFillWithReviews(getFavoritLiat().get(i));
                    Intent i2 = new Intent(getActivity(), Details.class);
                    fillIntent(i2);

                    if (!checkTowPane())
                        startActivityForResult(i2, 0);
                    else {

                        getActivity().setIntent(i2);
                        FragmentManager f = getFragmentManager();
                        f2 = f.findFragmentById(R.id.details);

                        if (f2 == null) {
                            f2 = new DetailsFragment();
                            f2.setRetainInstance(true);
                            f.beginTransaction().add(R.id.details, f2).commit();

                        } else {
                            //f.beginTransaction().remove(f2);
                            //f2 = new DetailsFragment();
                            //f2.setRetainInstance(true);

                            //f.beginTransaction().add(R.id.details, f2).commit();
                        }
                    }


                }
                /**********************************************************************************************************************************************/


            }
        });
        setHasOptionsMenu(true);
        return view;
    }
    /**********************************************************************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        movieRequest myRequest = new movieRequest();

        if (item.getItemId() == R.id.top) {

           // if(getTop_ratedList().size()==0)

            editor.putInt("data", 1);
            editor.commit();
            setShowTyp("top_rated");
            myRequest.execute("http://api.themoviedb.org/3/movie/top_rated?api_key=fa7e0aeaf6a6db5397d72702703cae47");
            item.setChecked(true);
            return true;
           // else{
              //  getMovieGrid().setAdapter(new MoviesAdapter(getActivity(), getTop_ratedList()));

            }

            //setTopListEmpty();
            //if (getTop_ratedList().isEmpty() == true) {

               // } else {
                   // if (item.isChecked() == false) {


        /**********************************************************************************************************************************************/

        else if (item.getItemId() == R.id.popular) {

            // if(getPopularList().size()==0)
            editor.putInt("data", 2);
            editor.commit();
            setShowTyp("popular");
            myRequest.execute("http://api.themoviedb.org/3/movie/popular?api_key=fa7e0aeaf6a6db5397d72702703cae47");
            item.setChecked(true);

            return true;
           // else{
               // getMovieGrid().setAdapter(new MoviesAdapter(getActivity(), getPopularList()));

            //}



        }


        /**********************************************************************************************************************************************/

        else if (item.getItemId() == R.id.favorit) {
            String[] projection = {
                    MyProvider._ID,
                    MyProvider.title,
                    MyProvider.posterPath,
                    MyProvider.overView,
                    MyProvider.date,
                    MyProvider.vote,
                    MyProvider.reviews
            };
            Cursor c=  getActivity().getContentResolver().query(MyProvider.myUrl, projection, null, null, null);




            editor.putInt("data", 3);
            editor.commit();
                if (getFavoritLiat().isEmpty() ==true) {

                    setShowTyp("favorit");
                    while(c.moveToNext()) {
                        Movie movie = new Movie();
                        movie.setId(c.getInt(c.getColumnIndex(MyProvider._ID)));
                        movie.setTitle(c.getString(c.getColumnIndex(MyProvider.title)));
                        movie.setPosterPath(c.getString(c.getColumnIndex(MyProvider.posterPath)));
                        movie.setOverView(c.getString(c.getColumnIndex(MyProvider.overView)));
                        movie.setDate(c.getString(c.getColumnIndex(MyProvider.date)));
                        movie.setVote(c.getDouble(c.getColumnIndex(MyProvider.vote)));
                        movie.setReviews(c.getString(c.getColumnIndex(MyProvider.reviews)));

                        movie.setFavorir(true);
                        favoritList.add(movie);


                    }
                    c.close();
                    getMovieGrid().setAdapter(new MoviesAdapter(getActivity(), getFavoritLiat()));

                }
            else{
                    getMovieGrid().setAdapter(new MoviesAdapter(getActivity(), getFavoritLiat()));

                }
            item.setChecked(true);
return true;

        }
        /**********************************************************************************************************************************************/



else
            return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int typ = sharedPref.getInt("data", 2);
        if(typ==1)
        {
            MenuItem item=menu.findItem(R.id.top);
            item.setChecked(true);
        }
        if(typ==3)
        {
            MenuItem item=menu.findItem(R.id.favorit);
            item.setChecked(true);
        }
else    {
            MenuItem item=menu.findItem(R.id.popular);
            item.setChecked(true);
        }
    }

    /**********************************************************************************************************************************************/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean checked = data.getBooleanExtra("cheked", false);
        System.out.println(checked);
        if (getMovieToFillWithReviews().isFavorir() == false && checked == true) {
            getMovieToFillWithReviews().setFavorir(true);
          //  if (getFavoritLiat().contains(getMovieToFillWithReviews()))
               // return;
           // getFavoritLiat().add(getMovieToFillWithReviews());
        } else if (getMovieToFillWithReviews().isFavorir() == true && checked == false) {
            getMovieToFillWithReviews().setFavorir(false);
            //getFavoritLiat().remove(getMovieToFillWithReviews());
        }


    }



    /**********************************************************************************************************************************************/
    private class movieRequest extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPostExecute(String srt) {
            super.onPostExecute(srt);
           // progressDialog.dismiss();
            if(srt==null) {
                progressDialog.dismiss();
                return;
            }
            setmAdapter(new MoviesAdapter(getActivity(), fillMovies(srt)));
            getMovieGrid().setAdapter(getmAdapter());
            progressDialog.dismiss();


        }

       /* @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
        }

        /********************************************************************************/
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            String movietJsonStr = null;
            // Will contain the raw JSON response as a string.
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(strings[0]);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movietJsonStr = buffer.toString();
                //JSONObject jsn = new JSONObject(forecastJsonStr);
                //setTodayWeather(jsn.getJSONArray("list").getJSONObject(0).getJSONArray("weather").toString());
                //setNextdayWeather(jsn.getJSONArray("list").getJSONObject(2).getJSONArray("weather").toString());


            } catch (IOException e) {
                //  Toast.makeText(getActivity(), Data.message, Toast.LENGTH_LONG);
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }

                }
            }


            return movietJsonStr;

        }
    }
    /**********************************************************************************************************************************************/

    private class reviewsTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String srt) {
            super.onPostExecute(srt);


            filMovieWithReviews(srt);

            Intent i2 = new Intent(getActivity(), Details.class);
            fillIntent(i2);
            if (!checkTowPane())
                startActivityForResult(i2, 0);
            /**********************************************************************************************************************************************/

            else {

                getActivity().setIntent(i2);
                FragmentManager f = getFragmentManager();
                f2 = f.findFragmentById(R.id.details);

                if (f2 == null) {
                    f2 = new DetailsFragment();
                    f2.setRetainInstance(true);
                    f.beginTransaction().add(R.id.details, f2).commit();

                } else {
                    //f.beginTransaction().remove(f2);
                    //f2 = new DetailsFragment();
                    //f2.setRetainInstance(true);

                    //f.beginTransaction().add(R.id.details, f2).commit();
                }
            }
            /**********************************************************************************************************************************************/
        }


        /**********************************************************************************************************************************************/
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            String reviewsJsonStr = null;
            // Will contain the raw JSON response as a string.
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(strings[0]);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                reviewsJsonStr = buffer.toString();
            } catch (IOException e) {
                //  Toast.makeText(getActivity(), Data.message, Toast.LENGTH_LONG);
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }

                }
            }
            return reviewsJsonStr;

        }
    }
}
/**********************************************************************************************************************************************/







