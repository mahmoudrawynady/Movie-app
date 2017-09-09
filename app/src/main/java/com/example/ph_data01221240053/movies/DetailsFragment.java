package com.example.ph_data01221240053.movies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
/**********************************************************************************************************************************************/
public class DetailsFragment extends Fragment implements Serializable{
    private boolean starChecked = false;
    private Intent data;
    private Movie movie = new Movie();

    /**********************************************************************************************************************************************/

    public void fillDeatails() {
        movie.setTitle(data.getStringExtra(Data.titleKey));
        movie.setId(data.getIntExtra(Data.idKey, 0));
        if(data.getStringExtra(Data.reviewKey)==null)
            movie.setReviews("");
        else
            movie.setReviews(data.getStringExtra(Data.reviewKey));
        movie.setVote(data.getDoubleExtra(Data.voteKey, 0.0));
        movie.setDate(data.getStringExtra(Data.dateKey));
        movie.setPosterPath(data.getStringExtra(Data.pathKey));
        movie.setOverView(data.getStringExtra(Data.overViewKey));
        movie.setVideoPath(data.getStringExtra(Data.videoBase));

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

    public void fillvideoPath(String stream) {

        try {

            JSONObject reviewObject = new JSONObject(stream);
            JSONArray reviewArray = reviewObject.getJSONArray("results");
            String Key = reviewArray.getJSONObject(0).getString("key");
            movie.setVideoPath(Key);

        } catch (JSONException e)

        {
            System.err.println("Error1");
        }

    }
    /**********************************************************************************************************************************************/

    public void setStarChecked(boolean starChecked) {
        this.starChecked = starChecked;
    }

    public boolean isStarChecked() {
        return starChecked;
    }

    /**********************************************************************************************************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adtivity_detailstow, container, false);
        ImageButton video=( ImageButton)view.findViewById(R.id.imageButton);
        Button title = (Button) view.findViewById(R.id.button2);
        Button trial = (Button) view.findViewById(R.id.button3);
        TextView rate = (TextView) view.findViewById(R.id.textView2);
        TextView overview = (TextView) view.findViewById(R.id.textView4);
        final TextView Date = (TextView) view.findViewById(R.id.textView);
        TextView review = (TextView) view.findViewById(R.id.textView5);
        CheckBox favorit = (CheckBox) view.findViewById(R.id.favorite);
        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        data = getActivity().getIntent();
        fillDeatails();
        rate.setText("" + movie.getVote() + "/10");
        overview.setText(movie.getOverView());
        title.setText(movie.getTitle());
        review.setText(movie.getReviews());
        Date.setText(movie.getDate());
        Picasso.with(getActivity()).load(Data.imageBase + movie.getPosterPath()).fit().into(image);
       setStarChecked(getActivity().getIntent().getBooleanExtra("favorit", false));
        favorit.setChecked(getActivity().getIntent().getBooleanExtra("favorit", false));

        /**********************************************************************************************************************************************/

        favorit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (getActivity() instanceof MainActivity) {
                    System.err.println("yes mainActivity");

                    ((MainActivity) getActivity()).getFavoritOrNotWhenTowPane(b);
                }
if( isStarChecked()==false)
{
    ContentValues values = new ContentValues();
    values.put(MyProvider._ID, movie.getId());
    values.put(MyProvider.title, movie.getTitle());
    values.put(MyProvider.posterPath, movie.getPosterPath());
    values.put(MyProvider.overView, movie.getOverView());
    values.put(MyProvider.date, movie.getDate());
    values.put(MyProvider.vote, movie.getVote());
    values.put(MyProvider.favorir, true);
    values.put(MyProvider.reviews, movie.getReviews());
    getActivity().getContentResolver().insert(MyProvider.myUrl, values);
    setStarChecked(b);
}
                else{
    int x=getActivity().getContentResolver().delete(MyProvider.myUrl,MyProvider._ID + "=" + movie.getId(),null);
    System.err.println(x);
    setStarChecked(b);

}


            }
        });
        /**********************************************************************************************************************************************/

       video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()==false)
                {
                    Toast.makeText(getActivity(),"No connection please try again",Toast.LENGTH_LONG);
                }
                else {
                    if (movie.getVideoPath() == null)
                        new videoTask().execute(Data.baseRequest + movie.getId() + Data.video + Data.appiKey);
                    else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Data.videoBase + movie.getVideoPath()));
                        startActivity(browserIntent);
                    }
                }
            }
        });

        return view;

    }


    /**********************************************************************************************************************************************/
    private class videoTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String srt) {
            super.onPostExecute(srt);
            fillvideoPath(srt);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Data.videoBase + movie.getVideoPath()));
            startActivity(browserIntent);

        }


        /**********************************************************************************************************************************************/
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            String videoPathJsonStr = null;
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
                videoPathJsonStr = buffer.toString();
            } catch (IOException e) {

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


            return videoPathJsonStr;

        }
    }

}
/**********************************************************************************************************************************************/
