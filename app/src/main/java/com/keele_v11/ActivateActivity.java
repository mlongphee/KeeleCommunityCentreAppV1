package com.keele_v11;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suke.widget.SwitchButton;

public class ActivateActivity extends AppCompatActivity {

    private EditText userEmail;
    private com.suke.widget.SwitchButton activate;
    private com.suke.widget.SwitchButton admin;
    private Button activateButton;

    private DatabaseReference mDatabaseUser;

    private boolean isActivated = false;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar2);

        userEmail = (EditText) findViewById(R.id.userEmail);
        activate = (com.suke.widget.SwitchButton) findViewById(R.id.switchActivate);
        admin = (com.suke.widget.SwitchButton) findViewById(R.id.switchAdmin);
        activateButton = (Button) findViewById(R.id.activateButton);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkActivation();
            }
        });

        activate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                isActivated = isChecked;
            }
        });

        admin.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                isAdmin = isChecked;
            }
        });
    }

    public void checkActivation(){
        final String email = userEmail.getText().toString().trim();

        if(!TextUtils.isEmpty(email)) {


            mDatabaseUser.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userSnapshot.getRef().child("Active").setValue(isActivated);
                        userSnapshot.getRef().child("Admin").setValue(isAdmin);

                    }

                    Toast.makeText(ActivateActivity.this, "Account Activated Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ActivateActivity.this, LayoutPickerActivity.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ActivateActivity.this, "Email does not exist/Wrong Format", Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
