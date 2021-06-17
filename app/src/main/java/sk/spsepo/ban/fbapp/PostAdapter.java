package sk.spsepo.ban.fbapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class PostAdapter extends FirebaseRecyclerAdapter<
        Post, PostAdapter.postsViewholder> {
    Post model;
    PostAdapter.postsViewholder holder;
    Context con;
    DatabaseReference databaseReference;
    ArrayList<String> a;
    Boolean aa;
    FirebaseAuth fAuth;
    int position;

    public PostAdapter(
            @NonNull FirebaseRecyclerOptions<Post> options, FragmentActivity fragmentActivity)
    {
        super(options);
        con = fragmentActivity;
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")

    @Override
    protected void
    onBindViewHolder(@NonNull final PostAdapter.postsViewholder holder,
                     final int position, @NonNull final Post model)
    {
        this.position = position;
        a = null;
        aa = false;
        databaseReference
                = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/");
        fAuth = FirebaseAuth.getInstance();
        this.model = model;
        this.holder = holder;



        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.postDesc.setText(model.getDescription());
        Picasso.with(con).load(model.getImage()).into(holder.postImg);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)con).change2chat(fAuth.getUid());


            }
        });*/
        databaseReference.child("Users").child(model.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.userNamePost.setText(snapshot.child("fname").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("friendList").child(fAuth.getUid()).child(model.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {



                if (snapshot.getValue()!=null){
                    aa = true;
                }else aa = false;
                try {
                    if (snapshot.getValue().toString().isEmpty()) {
                        holder.itemView.setVisibility(View.VISIBLE);



                    }
                }catch (NullPointerException e) {
                    if (model.getUID().equals(fAuth.getUid())) {
                        holder.itemView.setVisibility(View.VISIBLE);
                    } else {
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.getLayoutParams().height = 0;
                        holder.itemView.getLayoutParams().width = 0;
                    }
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
    public PostAdapter.postsViewholder
    onCreateViewHolder(@NonNull final ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        //Picasso.with(parent.getContext()).load(model.getImage()).into(holder.personAvatar);




        //Picasso.with(parent.getContext()).load(Uri.parse(model.getImage())).into(holder.personAvatar);
        return new PostAdapter.postsViewholder(view);
    }



    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class postsViewholder
            extends RecyclerView.ViewHolder {
        TextView userNamePost, postDesc;
        ImageView postImg;
        public postsViewholder(@NonNull View itemView)
        {
            super(itemView);

            userNamePost = itemView.findViewById(R.id.userNamePost);
            postDesc = itemView.findViewById(R.id.postDesc);
            postImg = itemView.findViewById(R.id.postImg);


        }
    }
}