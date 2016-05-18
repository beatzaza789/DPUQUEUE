package com.example.beatz_000.dpuqueues;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by beatz_000 on 6/22/2015.
 */
public class notification  extends MainActivity {
    int num = 0,notificationID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_receiver);
        ShowmyQ();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}