package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.EventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private String profileUID;
    DatabaseReference profileDatabase;
    TextView displayName, status;
    CircleImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileUID = getIntent().getExtras().get("UID").toString();
        profileDatabase = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("Users").child(profileUID);

        Toast.makeText(this, profileUID, Toast.LENGTH_LONG).show();
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

    }
}