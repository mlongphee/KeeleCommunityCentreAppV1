package com.keele_v11;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PostEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mcampName;
    private EditText mEventName;
    private EditText mPostTime;
    private EditText mpostLocation;

    private DatabaseReference mDatabase;

    private Button mSubmitBtn;

    private TextView date;

    private ProgressDialog mProgress;

    private long randomNum = Math.round((Math.random()* 89999)+10000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar2);

        mcampName = (EditText)findViewById(R.id.campField_E);
        mEventName = (EditText)findViewById(R.id.titleField_E);
        mPostTime = (EditText) findViewById(R.id.descField_E);
        mpostLocation = (EditText)findViewById(R.id.loaction);

        mSubmitBtn = (Button) findViewById(R.id.activateButton);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        mProgress = new ProgressDialog(this);

        long currentDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM, dd, yyyy");
        String dateString = sdf.format(currentDate);

        date = (TextView)findViewById(R.id.showDate);
        date.setText(dateString);

        mSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startPosting();

            }
        });
    }

    private void startPosting(){

        final String campName_val = mcampName.getText().toString().trim();
        final String eventName_val = mEventName.getText().toString().trim();
        final String time_val = mPostTime.getText().toString().trim();
        final String location_val = mpostLocation.getText().toString().trim();
        final String date_val = date.getText().toString().trim();

        if(!TextUtils.isEmpty(campName_val) && !TextUtils.isEmpty(eventName_val) && !TextUtils.isEmpty(time_val) && !TextUtils.isEmpty(location_val)){

            mProgress.setMessage("Posting ...");
            mProgress.show();

            DatabaseReference newPost = mDatabase.push();

            newPost.child("camp_name").setValue(campName_val);
            newPost.child("event_name").setValue(eventName_val);
            newPost.child("time").setValue(time_val);
            newPost.child("location").setValue(location_val);
            newPost.child("date").setValue(date_val);
            newPost.child("id").setValue(randomNum);

            startActivity(new Intent(PostEvent.this, MainActivity.class));
        }

    }


    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    public void setDate(final Calendar calendar){
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM, dd, yyyy");
        date.setText(sdf.format(calendar.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(),year, month, day);
        }
    }
}
