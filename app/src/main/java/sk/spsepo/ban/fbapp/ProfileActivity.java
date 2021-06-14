package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private String profileUID;
    private String state;
    DatabaseReference profileDatabase;
    DatabaseReference friendRequestDB;
    DatabaseReference friendListDB;
    FirebaseAuth fAuth;
    TextView displayName, status;
    Button requestBtn;
    CircleImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileUID = getIntent().getExtras().get("UID").toString();
        fAuth = FirebaseAuth.getInstance();
        profileDatabase = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("Users").child(profileUID);
        friendRequestDB = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("friendRequest");
        friendListDB = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("friendList");

        state = "notFriends";
        requestBtn = findViewById(R.id.requestBtnAccept);
        icon = findViewById(R.id.profile_image);
        displayName = findViewById(R.id.displayName);
        status = findViewById(R.id.statusProfile);
        profileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("fname").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String status1 = snapshot.child("status").getValue().toString();

                displayName.setText(name);
                status.setText(status1);
                Picasso.with(ProfileActivity.this).load(image).into(icon);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference frdb = friendRequestDB.child(fAuth.getUid());
        frdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                if (snapshot.child(profileUID).exists()){
                    if (snapshot.child(profileUID).child("request").getValue().toString().equals("sent"))  {
                        requestBtn.setText("Cancel request");
                        requestBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                friendRequestDB.child(profileUID).child(fAuth.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        frdb.child(profileUID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendRequest();
                                            }
                                        });

                                    }
                                });

                            }
                        });
                    }else if (snapshot.child(profileUID).child("request").getValue().toString().equals("received")){
                        requestBtn.setText("Accept");
                        requestBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Map<String, String> user = new HashMap<>();

                                profileDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("fname").getValue().toString();
                                        String image = snapshot.child("image").getValue().toString();
                                        String status1 = snapshot.child("status").getValue().toString();
                                        user.put("fname", name);
                                        user.put("image", image);


                                        displayName.setText(name);
                                        status.setText(status1);
                                        Picasso.with(ProfileActivity.this).load(image).into(icon);
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                friendListDB.child(fAuth.getUid()).child(profileUID).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        friendListDB.child(profileUID).child(fAuth.getUid()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                friendRequestDB.child(profileUID).child(fAuth.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        frdb.child(profileUID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                requestBtn.setText("Already Friends");
                                                                requestBtn.setEnabled(false);
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                }else{
                    sendRequest();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    public void sendRequest(){
        requestBtn.setText("Send Friend Request");
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendRequestDB.child(fAuth.getUid()).child(profileUID).child("request").setValue("sent").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendRequestDB.child(profileUID).child(fAuth.getUid()).child("request").setValue("received");
                    }
                });


            }
        });
    }
}