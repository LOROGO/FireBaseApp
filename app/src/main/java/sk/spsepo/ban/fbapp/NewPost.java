package sk.spsepo.ban.fbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

public class NewPost extends AppCompatActivity {
    private ImageView postImage;
    private TextView postDesc;
    private Button postAdd;
    private Uri resultUri;

    DatabaseReference mPostDatabase;
    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postImage = findViewById(R.id.postImg);
        postDesc = findViewById(R.id.newPostBtn);
        postAdd = findViewById(R.id.newPostBtn);

        mPostDatabase = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("Posts");


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(NewPost.this);


            }
        });
        postAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String postId = Calendar.getInstance().getTime().toString()+mCurrentUser.getUid();
                final StorageReference imgPath = storageRef.child("posts").child(postId);




                imgPath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                mPostDatabase.child(postId).child("image").setValue(uri.toString()+".jpg");
                                mPostDatabase.child(postId).child("description").setValue(postDesc.getText().toString());
                                mPostDatabase.child(postId).child("UID").setValue(mCurrentUser.getUid());
                                mPostDatabase.child(postId).child("datetime").setValue(Calendar.getInstance().getTime().toString());
                                Toast.makeText(NewPost.this, uri.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                Intent intent = new Intent(NewPost.this, MainActivity.class);
                intent.putExtra("viewPagerPos", 1);
                startActivity(intent);
            }
        });




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                postImage.setImageURI(data.getData());





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }}}
}