package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatabaseReference reference;
    TextView name;
    ImageView image;

    FirebaseAuth fAuth;


    ImageButton sendBtn;
    EditText sendTxt;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    String UID;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fAuth = FirebaseAuth.getInstance();

        databaseReference
                = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/");


        recyclerView = findViewById(R.id.messRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        
        sendBtn = findViewById(R.id.btnSend);
        sendTxt = findViewById(R.id.text_send);
        UID = getIntent().getStringExtra("UID");
        if(!sendTxt.getText().equals("")) {
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = sendTxt.getText().toString();
                    if (!msg.equals("")) {
                        sendMessage(fAuth.getUid(), UID, msg);
                        sendTxt.setText("");
                    }

                }
            });
        }

        databaseReference.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //name.setText(snapshot.child("fname").getValue().toString());
                //Picasso.with(ChatActivity.this).load(snapshot.child("image").getValue().toString()).into(image);
                setTitle(snapshot.child("fname").getValue().toString());

                readMessages(fAuth.getUid(),UID,snapshot.child("image").getValue().toString());
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

    private void readMessages(final String myid, final String userid, final String imageurl){
      mchat=new ArrayList<>();
      reference = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReference().child("Chats");
        Toast.makeText(this, databaseReference.toString(), Toast.LENGTH_LONG).show();
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              mchat.clear();
              for ( DataSnapshot snapshot :
                      dataSnapshot.getChildren()){
                  Chat chat = snapshot.getValue(Chat.class);
                  if (chat!=null) {
                      if (chat.getReceiver()
                              .equals
                                      (myid)
                              && chat.getSender().equals(userid)
                              || chat.getReceiver().equals(userid)
                              && chat.getSender().equals(myid)) {
                          mchat.add(chat);
                      }
                      Toast.makeText(ChatActivity.this, chat.getReceiver(), Toast.LENGTH_LONG).show();
                  }

                  messageAdapter = new MessageAdapter(ChatActivity.this, mchat, imageurl);
                  recyclerView.setAdapter(messageAdapter);

              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }
}