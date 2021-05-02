package sk.spsepo.ban.fbapp;

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

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class PersonAdapter extends FirebaseRecyclerAdapter<
        Person, PersonAdapter.personsViewholder> {

    public PersonAdapter(
            @NonNull FirebaseRecyclerOptions<Person> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder,
                     int position, @NonNull Person model)
    {

        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.fname.setText(model.getFname());

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.status.setText(model.getStatus());
        holder.personAvatar.setImageDrawable(null);


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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "onCLick", Toast.LENGTH_SHORT).show();
            }
        });
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

            fname = itemView.findViewById(R.id.fname);
            status = itemView.findViewById(R.id.status);
            personAvatar = itemView.findViewById(R.id.personAvatar);


        }
    }
}
