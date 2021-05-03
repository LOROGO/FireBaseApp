package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicMarkableReference;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import id.zelory.compressor.constraint.Compression;

public class SettingsActivity extends AppCompatActivity {

    DatabaseReference mUserDatabase;
    FirebaseUser mCurrentUser;

    private CircleImageView mImage;
    private TextView mName;
    private TextView mStatus;
    private Button statusBtn;
    private Button imageBtn;

    private static final int GALLERY_PICK = 0;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.settings_display_name);
        mStatus = (TextView) findViewById(R.id.settings_status);
        statusBtn = findViewById(R.id.settings_status_btn);
        imageBtn = findViewById(R.id.settings_image_btn);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fname").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);
                Picasso.with(SettingsActivity.this).load(image).into(mImage);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, StatusActivity.class));
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();



                /*try {
                    imageBitmap = SiliCompressor.with(SettingsActivity.this).getCompressBitmap(String.valueOf(resultUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/





                final StorageReference thumb_image = storageRef.child("icons").child("thumbs").child(mCurrentUser.getUid()+".jpg");
                final StorageReference imgPath = storageRef.child("icons").child(mCurrentUser.getUid()+".jpg");

               // StorageReference imgPth2 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fbapp-ba93b.appspot.com/icons");


                imgPath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                mUserDatabase.child("image").setValue(uri.toString());
                               /* Bitmap full = BitmapFactory.decodeFile("");
                                Bitmap reduced = ImageResizer.reduceBitmapSize(full, 240000);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                reduced.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();


                                UploadTask uploadTask = thumb_image.putBytes(data);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        String thumb_Url = Objects.requireNonNull(task.getResult()).getStorage().getDownloadUrl().toString();
                                        if (task.isSuccessful()){
                                            Map update = new HashMap();
                                            update.put("image", uri.toString());
                                            update.put("thumb_image", thumb_Url);
                                            mUserDatabase.updateChildren(update);

                                        }
                                    }
                                });*/




                                Toast.makeText(SettingsActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                        /*.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, "ide tooo", Toast.LENGTH_LONG).show();
                            String img_url = task.getResult().getStorage().getDownloadUrl().toString();
                            mUserDatabase.child("image").setValue(img_url);

                        }
                    }
                });*/


               // Toast.makeText(SettingsActivity.this, resultUri.toString(),Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}