package com.xmart.projects.storline.activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.UserDriver;
import com.xmart.projects.storline.models.User;

import java.lang.reflect.Array;
import java.util.Arrays;

import okhttp3.internal.connection.StreamAllocation;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private UserDriver userDriver;

    private EditText edtName, edtMail, edtPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registrarse");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userDriver = new UserDriver(this, "Register");

        edtName = (EditText) findViewById(R.id.edtNombre);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtPass = (EditText) findViewById(R.id.edtPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void singUp(View view) {

        String name = edtName.getText().toString();
        String mail = edtMail.getText().toString();
        String pass = edtPass.getText().toString();

        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(name)) {
            singUpFirebase(mail, pass, name);
        }
        else {
            Toast.makeText(this, "Campos requeridos", Toast.LENGTH_SHORT).show();
        }
    }


    public void close(View view) {
        finish();
    }


    private void singUpFirebase(final String mail, String pass, final String name) {

        mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String img = "https://firebasestorage.googleapis.com/v0/b/storlineproject.appspot.com/o/user-images%2Fuser.png?alt=media&token=c45acd45-0080-45a7-b0e4-b73e7f144fb5";

                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();

                    String[] roles = { "user" };

                    User userdata = new User(mail, name, Arrays.asList(roles), img);
                    userdata.setKey(user.getUid());
                    userDriver.addUser(userdata, Uri.parse(img));

                    finish();

                } else {

                    // If sign in fails, display a message to the user.
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
