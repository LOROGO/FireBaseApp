package sk.spsepo.ban.fbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class AllUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private ImageButton searchBtn;
    PersonAdapter
            adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the
    // Firebase Realtime Database
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        // Create a instance of the database and get
        // its reference
        fAuth = FirebaseAuth.getInstance();
        mbase
                = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/");


        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.search_bar);
        searchBtn = findViewById(R.id.searchBtn);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        //mbase.child("friendList").child(fAuth.getUid())
        Query s = mbase.child("Users");
        FirebaseRecyclerOptions<Person> options
                = new FirebaseRecyclerOptions.Builder<Person>()
                .setQuery(s, Person.class)
                .build();




        // Connecting object of required Adapter class to
        // the Adapter class itself

        adapter = new PersonAdapter(options, AllUsers.this);

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query a = mbase.child("Users").orderByChild("fname").startAt(searchBar.getText().toString()).endAt(searchBar.getText().toString()+"\uf8ff");
                FirebaseRecyclerOptions<Person> options
                        = new FirebaseRecyclerOptions.Builder<Person>()
                        .setQuery(a, Person.class)
                        .build();
                adapter.updateOptions(options);
            }
        });
    }



    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}
