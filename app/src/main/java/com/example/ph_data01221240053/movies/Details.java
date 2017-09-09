package com.example.ph_data01221240053.movies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
/**********************************************************************************************************************************************/


public class Details extends AppCompatActivity {
    private DetailsFragment f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        FragmentManager f = getFragmentManager();
        f2 = new DetailsFragment();
        f2.setRetainInstance(true);
        f.beginTransaction().add(R.id.details, f2).commit();
    }
    /**********************************************************************************************************************************************/


    @Override
    public void onBackPressed() {
        System.err.println("yes iam get" + "" + f2.isStarChecked());

        Intent i = new Intent();
        i.putExtra("cheked", f2.isStarChecked());
        setResult(RESULT_OK, i);
        super.onBackPressed();

    }
    /**********************************************************************************************************************************************/

}

