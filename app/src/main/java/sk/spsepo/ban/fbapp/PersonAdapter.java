package sk.spsepo.ban.fbapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View


public class PersonAdapter extends FirebaseRecyclerAdapter<
        Person, PersonAdapter.personsViewholder> {
    Person model;
    personsViewholder holder;
    Context con;
    DatabaseReference databaseReference;
    ArrayList<String> a;
    Boolean aa;
    FirebaseAuth fAuth;

    public PersonAdapter(
            @NonNull FirebaseRecyclerOptions<Person> options, AllUsers allUsers)
    {
        super(options);
        con = allUsers;
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")

    @Override
    protected void
    onBindViewHolder(@NonNull final personsViewholder holder,
                     final int position, @NonNull Person model)
    {
        a = null;
        aa = false;
        databaseReference
                = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/");
        fAuth = FirebaseAuth.getInstance();
        this.model = model;
        this.holder = holder;
        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.fname.setText(model.getFname());

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.status.setText(model.getStatus());
        Picasso.with(con).load(model.getImage()).into(holder.personAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(con, getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                Intent profileIntent = new Intent(con, ProfileActivity.class);
                String profile_uid = getRef(position).getKey();
                profileIntent.putExtra("UID", profile_uid);
                con.startActivity(profileIntent);

            }
        });









    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public personsViewholder
    onCreateViewHolder(@NonNull final ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person, parent, false);
        //Picasso.with(parent.getContext()).load(model.getImage()).into(holder.personAvatar);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Picasso.with(parent.getContext()).load(Uri.parse(model.getImage())).into(holder.personAvatar);
        return new PersonAdapter.personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView fname, status;
        ImageView personAvatar;
        public personsViewholder(@NonNull View itemView)
        {
            super(itemView);

            fname = itemView.findViewById(R.id.fnamePerson);
            status = itemView.findViewById(R.id.statusPerson);
            personAvatar = itemView.findViewById(R.id.personAvatar);


        }
    }
}
