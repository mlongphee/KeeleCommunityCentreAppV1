package com.keele_v11;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LayoutPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_picker);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar2);
    }

    public void buttonClicked(View v){
        startActivity(new Intent(LayoutPickerActivity.this, PostNewsActivity.class));

    }

    public void buttonClicked2(View v){
        startActivity(new Intent(LayoutPickerActivity.this, PostEvent.class));

    }
    public void buttonClicked3(View v){
        startActivity(new Intent(LayoutPickerActivity.this, PostActivity.class));

    }
    public void buttonClicked4(View v){
        startActivity(new Intent(LayoutPickerActivity.this, PostLnfActivity.class));

    }
    public void buttonClicked5(View v){
        startActivity(new Intent(LayoutPickerActivity.this, ActivateActivity.class));
    }
}
