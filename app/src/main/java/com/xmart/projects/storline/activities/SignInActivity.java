package com.xmart.projects.storline.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.UserDriver;
import com.xmart.projects.storline.models.User;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGNIN = 111;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private UserDriver userDriver;

    private SignInButton signInButton;
    private EditText edtMail, edtPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_AUTO);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle(v);
            }
        });

        userDriver = new UserDriver(this, "SignIn");

        edtMail = (EditText) findViewById(R.id.edtMail);
        edtPass = (EditText) findViewById(R.id.edtPassword);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGNIN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Auth Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void signInGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGNIN);
    }


    public void signInFirebase(View view) {

        String mail = edtMail.getText().toString();
        String pass = edtPass.getText().toString();

        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)) {
            signInUserFirebase(mail, pass);
        }
        else {
            Toast.makeText(this, "Campos requeridos", Toast.LENGTH_SHORT).show();
        }

    }


    public void registerFirebase(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }


    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                    String[] roles = { "user" };

                    User userdata = new User(acct.getEmail(), acct.getDisplayName(), Arrays.asList(roles), acct.getPhotoUrl().toString());
                    userdata.setKey(user.getUid());
                    userDriver.addUser(userdata, acct.getPhotoUrl());

                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            goMainScreen();
        } else {
            //Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }
    }


    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void signInUserFirebase(String mail, String pass) {

        mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                } else {

                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
