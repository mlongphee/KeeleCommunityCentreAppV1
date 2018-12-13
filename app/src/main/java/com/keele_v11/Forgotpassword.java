package com.keele_v11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    private EditText mUsername;

    private Button mSend;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().hide();

        mUsername = (EditText) findViewById(R.id.usernameFeild);
        mUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mSend = (Button)findViewById(R.id.sendBtn);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();

                if(!TextUtils.isEmpty(username)) {

                    mProgress.setMessage("Sending Email ...");
                    mProgress.show();

                    mAuth.sendPasswordResetEmail(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                            if (task.isSuccessful()) {
                                mProgress.dismiss();

                                Toast.makeText(Forgotpassword.this, "Password reset email sent successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Forgotpassword.this, LoginActivity.class));
                            } else {
                                mProgress.dismiss();
                                Toast.makeText(Forgotpassword.this, "Error sending password reset email", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }

            }
        });
    }
}
