package sk.spsepo.ban.fbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class RequestAdapter extends FirebaseRecyclerAdapter<
        Person, RequestAdapter.personsViewholder> {
    Person model;
    personsViewholder holder;
    Context con;
    DatabaseReference databaseReference, friendRequestDB, friendListDB;
    ArrayList<String> a;
    Boolean aa;
    FirebaseAuth fAuth;
    int position;

    public RequestAdapter(
            @NonNull FirebaseRecyclerOptions<Person> options, FragmentActivity fragmentActivity) {
        super(options);
        con = fragmentActivity;
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")

    @Override
    protected void
    onBindViewHolder(@NonNull final personsViewholder holder,
                     final int position, @NonNull Person model) {
        this.position = position;
        a = null;
        aa = false;
        databaseReference
                = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/");
        friendRequestDB = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("friendRequest");
        friendListDB = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("friendList");
        fAuth = FirebaseAuth.getInstance();
        this.model = model;
        this.holder = holder;


        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.fname.setText(model.getFname());
        Picasso.with(con).load(model.getImage()).into(holder.profilePic);

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")




        databaseReference.child("friendRequest").child(fAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

               /* if (snapshot.hasChildren()) {
                    if (snapshot.hasChild(getRef(position).getKey())) {
                        if (snapshot.child("request").getValue().toString().equals("received")) {
                            aa = true;
                        } else aa = false;
                        if (aa) {
                            holder.imgBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reqAccept();
                                }
                            });
                        } else {

                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.getLayoutParams().height = 0;
                            holder.itemView.getLayoutParams().width = 0;


                        }


                    }
                }*/
                holder.imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    reqAccept();
                            }
                });
                holder.btnDeny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendRequestDB.child(getRef(position).getKey()).child(fAuth.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                friendRequestDB.child(fAuth.getUid()).child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        holder.itemView.setVisibility(View.GONE);
                                        holder.itemView.getLayoutParams().height = 0;
                                        holder.itemView.getLayoutParams().width = 0;
                                    }
                                });

                            }
                        });
                    }
                });
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
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request, parent, false);
        //Picasso.with(parent.getContext()).load(model.getImage()).into(holder.personAvatar);


        //Picasso.with(parent.getContext()).load(Uri.parse(model.getImage())).into(holder.personAvatar);
        return new RequestAdapter.personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView fname;
        ImageButton imgBtn;
        ImageView profilePic;
        ImageButton btnDeny;


        public personsViewholder(@NonNull View itemView) {
            super(itemView);

            fname = itemView.findViewById(R.id.fnameRequest);
            imgBtn = itemView.findViewById(R.id.requestBtnAccept);
            btnDeny = itemView.findViewById(R.id.requstBtnDeny);
            profilePic = itemView.findViewById(R.id.requestAvatar);


        }
    }

    public void reqAccept() {
        friendListDB.child(fAuth.getUid()).child(getRef(position).getKey()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                friendListDB.child(getRef(position).getKey()).child(fAuth.getUid()).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendRequestDB.child(getRef(position).getKey()).child(fAuth.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                friendRequestDB.child(fAuth.getUid()).child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        holder.itemView.setVisibility(View.GONE);
                                        holder.itemView.getLayoutParams().height = 0;
                                        holder.itemView.getLayoutParams().width = 0;
                                    }
                                });

                            }
                        });
                    }
                });
            }
        });

    }
}

