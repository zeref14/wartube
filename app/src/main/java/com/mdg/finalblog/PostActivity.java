package com.mdg.finalblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageView mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private static  final  int GALLERY_REQUEST=1;
    private Button mSumbitBtn;
    private Uri mimageUri=null;
    private ProgressDialog mProgress;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        Menu menu= navigation.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);


        mPostTitle=(EditText)findViewById(R.id.titleField);
        mPostDesc=(EditText)findViewById(R.id.descFeild);
        mSumbitBtn=(Button)findViewById(R.id.sumbitBtn);
        mSelectImage = (ImageView) findViewById(R.id.imageSelect);
        mProgress=new ProgressDialog(this);
        mStorage=FirebaseStorage.getInstance().getReference();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Blog");



        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        mSumbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPosting();

            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        Intent intent=new Intent(PostActivity.this,Pew.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_notifications:

                        break;

                }


                return false;
            }
        });
    }

    private void startPosting() {

        mProgress.setMessage("Posting to Blog....");
        mProgress.show();
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mimageUri != null) {


            final StorageReference filepath = mStorage.child("Blog_Images").child(mimageUri.getLastPathSegment());



            //uploadTask= filepath.putFile(mimageUri);
            Task<Uri>urlTask=filepath.putFile(mimageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                        return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        DatabaseReference newpost=mDatabase.push();
                        newpost.child("title").setValue(title_val);
                        newpost.child("desc").setValue(desc_val);
                        newpost.child("image").setValue(downloadUri.toString());
                        //newpost.child("userid").setValue(FirebaseAuth.getC)
                        mProgress.dismiss();

                        startActivity(new Intent(PostActivity.this,MainActivity.class));




                    }
                }
            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){

              mimageUri= data.getData();
             mSelectImage.setImageURI(mimageUri);
         }



    }
}








