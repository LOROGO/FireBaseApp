package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    //FirebaseFirestore fStore;
    String userID;
    FirebaseUser current_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);

        mRegisterBtn = findViewById(R.id.regButt);
        mLoginBtn = findViewById(R.id.login1);

        fAuth = FirebaseAuth.getInstance();
       // fStore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email    = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullname = mFullName.getText().toString().trim();
                final String phone    = mPhone.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;

                }
                if (password.length() < 6){
                    mPassword.setError("Password must be 6 or longer");
                    return;

                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {

                           current_user = fAuth.getCurrentUser();
                           userID = fAuth.getCurrentUser().getUid();
                           DatabaseReference mdatabase = FirebaseDatabase.getInstance("https://fbapp-ba93b-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://fbapp-ba93b-default-rtdb.firebaseio.com/").child("Users").child(userID);
                           


                           //DocumentReference documentReference = fStore.collection("users").document(userID);
                           Map<String, String> user = new HashMap<>();
                           user.put("fname", fullname);
                           user.put("status", "Default status");
                           user.put("image", "default_avatar");
                           user.put("thumb_image", "default_avatar");
                           user.put("email", email);
                           user.put("phone", phone);
                           mdatabase.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Log.d(TAG, "onSuccesss: user Profile is created for "+userID);
                                   Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                               }
                           });
                           /*documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Log.d(TAG, "onSuccesss: user Profile is created for "+userID);
                                   Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                               }
                           });*/
                           Intent mainIntent = new Intent(Register.this, MainActivity.class);
                           mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(mainIntent);
                           finish();
                       }else {
                            Toast.makeText(Register.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                       }
                    }
                });
            }


        });

    }
}