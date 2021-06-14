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
import androidx.fragment.app.FragmentActivity;
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


public class FriendsAdapter extends FirebaseRecyclerAdapter<
        Person, FriendsAdapter.personsViewholder> {
    Person model;
    personsViewholder holder;
    Context con;
    DatabaseReference databaseReference;
    ArrayList<String> a;
    Boolean aa;
    FirebaseAuth fAuth;
    int position;

    public FriendsAdapter(
            @NonNull FirebaseRecyclerOptions<Person> options, FragmentActivity fragmentActivity)
    {
        super(options);
        con = fragmentActivity;
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")

    @Override
    protected void
    onBindViewHolder(@NonNull final personsViewholder holder,
                     final int position, @NonNull Person model)
    {
        this.position = position;
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
                ((MainActivity)con).change2chat(fAuth.getUid());


            }
        });

        
        databaseReference.child("friendList").child(fAuth.getUid()).child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {


                if (snapshot.getValue()!=null){
                    aa = true;
                }else aa = false;
                if (aa){
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.getLayoutParams().width = 500;
                    holder.itemView.getLayoutParams().height = 100;


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(con, ChatActivity.class);
                            intent.putExtra("UID", getRef(position).getKey());
                            con.startActivity(intent);



                        }
                    });
                }else if (!aa){

                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.getLayoutParams().height = 0;
                    holder.itemView.getLayoutParams().width = 0;


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        databaseReference.child("friendList").child(fAuth.getUid()).child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {


                if (snapshot.getValue()!=null){
                    aa = true;
                }else aa = false;
                if (aa){
                    holder.itemView.setVisibility(View.VISIBLE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(con, ChatActivity.class);
                            intent.putExtra("UID", getRef(position).getKey());
                            con.startActivity(intent);



                        }
                    });
                }else if (!aa){

                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.getLayoutParams().height = 0;
                    holder.itemView.getLayoutParams().width = 0;


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //Picasso.with(parent.getContext()).load(Uri.parse(model.getImage())).into(holder.personAvatar);
        return new FriendsAdapter.personsViewholder(view);
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
