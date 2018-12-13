package com.keele_v11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keele_v11.switchers.EventsFragment;
import com.keele_v11.switchers.LostnFoundFragment;
import com.keele_v11.switchers.MoreFragment;
import com.keele_v11.switchers.NewFeedFragment;
import com.keele_v11.switchers.WhatsupFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabaseUser;
    private FrameLayout mframe;

    public static String user_id;
    public static String filter = "All";

    private int navCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar2);

        mframe = (FrameLayout) findViewById(R.id.frame);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                } else {
                    user_id = mAuth.getCurrentUser().getUid();
                }

            }
        };

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);

        NewFeedFragment n = new NewFeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, n).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_news:
                                NewFeedFragment n = new NewFeedFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, n).commit();
                                navCheck = 0;

                                break;
                            case R.id.action_events:
                                EventsFragment f = new EventsFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                                navCheck = 1;

                                break;
                            case R.id.action_whatUp:
                                WhatsupFragment w = new WhatsupFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, w).commit();
                                navCheck = 2;

                                break;
                            case R.id.action_lostfound:
                                LostnFoundFragment l = new LostnFoundFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, l).commit();
                                navCheck = 3;

                                break;
                            case R.id.action_more:
                                MoreFragment m = new MoreFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame, m).commit();
                                navCheck = 4;
                        }
                        return false;
                    }
                });


    }

    @Override
    protected void onStart(){
        super.onStart();


        mAuth.addAuthStateListener(mAuthListener);


    }

    public void onButonClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final Menu menu1 = menu;

        if(user_id != null) {

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.child(user_id).getValue(Users.class);

                    try {
                        if (users.Admin) {
                            getMenuInflater().inflate(R.menu.add_button, menu1);
                            getSupportActionBar().setCustomView(R.layout.title_bar);
                        } else {
                            getMenuInflater().inflate(R.menu.filter_button, menu1);
                            getSupportActionBar().setCustomView(R.layout.title_bar);
                        }
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //getMenuInflater().inflate(R.menu.filter_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this, LayoutPickerActivity.class));
        }
        if(item.getItemId() == R.id.action_filter){
            new MaterialDialog.Builder(this).title("Filter Camps").content("Choose a camp").positiveText("Done").negativeText("Cancel").items("All","Keele Camp", "Tiny Tots", "Adventure Camp", "Youth Camp").itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                    //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    //Query tots = ref.child("NewsFeed").orderByChild("camp_name").equalTo("Tiny Tots");
                    filter = text.toString();
                    String LOG = "My Activity";
                    Log.d(LOG, "Filter Camp: " + filter);
                    if (navCheck == 0) {
                        NewFeedFragment n = new NewFeedFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, n).commit();
                    }
                    if (navCheck == 1) {
                        EventsFragment f = new EventsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, f).commit();
                    }
                    if (navCheck == 2) {
                        WhatsupFragment w = new WhatsupFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, w).commit();
                    }
                    if (navCheck == 3) {
                        LostnFoundFragment l = new LostnFoundFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, l).commit();
                    }
                    if(navCheck == 4){

                    }
                    return true;
                }
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }


}
