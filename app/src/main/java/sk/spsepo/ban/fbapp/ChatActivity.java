package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView name;
    ImageView image;

    FirebaseAuth fAuth;


    ImageButton sendBtn;
    EditText sendTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fAuth = FirebaseAuth.getInstance();

        databaseReference
                = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/");
        name = findViewById(R.id.chatName);
        image = findViewById(R.id.chatImg);
        sendBtn = findViewById(R.id.btnSend);
        sendTxt = findViewById(R.id.text_send);
        final String UID = getIntent().getStringExtra("UID");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendTxt.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fAuth.getUid(), UID, msg);
                    sendTxt.setText("");
                }

            }
        });

        databaseReference.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //name.setText(snapshot.child("fname").getValue().toString());
                //Picasso.with(ChatActivity.this).load(snapshot.child("image").getValue().toString()).into(image);
                setTitle(snapshot.child("fname").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void sendMessage (String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        databaseReference.child("Chats").push().setValue(hashMap);

    }
}