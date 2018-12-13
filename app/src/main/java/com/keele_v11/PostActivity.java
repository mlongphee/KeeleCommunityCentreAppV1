package com.keele_v11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mpostTitle;
    private EditText mPostDesc;
    private EditText mcampName;

    private Button mSubmitBtn;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar2);

        mSelectImage = (ImageButton)findViewById(R.id.imageSelect);

        mpostTitle = (EditText) findViewById(R.id.titleField_E);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mcampName = (EditText) findViewById(R.id.campField_W);

        mSubmitBtn = (Button) findViewById(R.id.activateButton);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Camp");
        mDatabase.keepSynced(true);

        mProgress = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST );

            }
        });

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

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val)){

            mProgress.setMessage("Posting ...");
            mProgress.show();

            StorageReference filepath = mStorage.child("Camp_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("camp_name").setValue(campName_val);

                    newPost.child("time").setValue(ServerValue.TIMESTAMP);
                    newPost.child("timestamp").setValue("");

                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                }
            });


        }

    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);

        if(requestcode == GALLERY_REQUEST && resultcode == RESULT_OK){

            mImageUri = data.getData();
            //String[] filePathColumn = {MediaStore.Images.Media.DATA};

            //Cursor selectorCursor = getContentResolver().query(mImageUri,filePathColumn,null,null,null);
           //selectorCursor.moveToFirst();

            //int columnIndex = selectorCursor.getColumnIndex(filePathColumn[0]);
            //String picturePath = selectorCursor.getString(columnIndex);
            //selectorCursor.close();

            mSelectImage.setImageURI(mImageUri);
            //mSelectImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            //Toast.makeText(getApplicationContext(), picturePath, Toast.LENGTH_SHORT).show();

        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(7);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}
