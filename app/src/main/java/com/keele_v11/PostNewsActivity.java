package com.keele_v11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostNewsActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mpostTitle;
    private EditText mPostDesc;
    private EditText mcampName;

    private Button mSubmitBtn;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private static LruCache<String, Bitmap> mMemoryCache;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_news);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar2);

        mSelectImage = (ImageButton)findViewById(R.id.imageSelect);

        mpostTitle = (EditText) findViewById(R.id.titleField_E);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mcampName = (EditText) findViewById(R.id.campField);

        mSubmitBtn = (Button) findViewById(R.id.activateButton);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewsFeed");

        mDatabase.keepSynced(true);

        mProgress = new ProgressDialog(this);


        /*mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST );

            }
        });*/

        mSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startPosting();

            }
        });
    }

    private void startPosting(){

        final String title_val = mpostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String campName_val = mcampName.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val)){

            mProgress.setMessage("Posting ...");
            mProgress.show();

                    mProgress.dismiss();

                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("camp_name").setValue(campName_val);
                    //newPost.child("image").setValue(downloadUrl.toString());

                    newPost.child("time").setValue(ServerValue.TIMESTAMP);
                    newPost.child("timestamp").setValue("");

                    startActivity(new Intent(PostNewsActivity.this, MainActivity.class));




        }

    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);

        if(requestcode == GALLERY_REQUEST && resultcode == RESULT_OK){

            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);

        }
    }

}
