
package com.example.ph_data01221240053.movies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ph_data01221240053.movies.R;
import com.squareup.picasso.Picasso;
/**********************************************************************************************************************************************/

public class MainActivity extends AppCompatActivity {
    private GridFrgment f2;
Menu m;

    public void setM(Menu m) {
        this.m = m;
    }

    public Menu getM() {
        return m;
    }

    public void setF2(GridFrgment f2) {
        this.f2 = f2;
    }

    public GridFrgment getF2() {
        return f2;
    }

    /**********************************************************************************************************************************************/
    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;

    }

  /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        try {
            outState.putSerializable("mydata", getF2());
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG);

        }
        }
        */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    FragmentManager f = getFragmentManager();
    f2 = new GridFrgment();
    f.beginTransaction().add(R.id.main, f2).commit();

    }



    /**********************************************************************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.min, menu);
        // return true so that the menu pop up is opened
        return true;
    }
    /**********************************************************************************************************************************************/

    public void getFavoritOrNotWhenTowPane(boolean fav) {
        f2.setFavoritIfTowPane(fav);

    }

    /**********************************************************************************************************************************************/

}
/**********************************************************************************************************************************************/

