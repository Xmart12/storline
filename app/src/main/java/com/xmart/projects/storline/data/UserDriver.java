package com.xmart.projects.storline.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.helpers.Utils;
import com.xmart.projects.storline.models.User;

import java.util.ArrayList;

/**
 * Created by smart on 05/09/2018.
 */

public class UserDriver {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private DatabaseReference userReference;
    private StorageReference storageReference;
    private Context context;
    private User user = null;
    public String location;

    public TextView tvUser, tvMail;
    public ImageView imgUser;
    public MenuItem navAdmin;



    //Constuctor
    public UserDriver(Context con, String location) {

        userReference = database.getReference("users");
        storageReference = storage.getReference("user-images");
        this.context = con;
        this.location = location;
    }


    public void addUser(User user, Uri imgUri) {

        try {

            //Toast.makeText(context.getApplicationContext(), "user: " + user.getKey(), Toast.LENGTH_SHORT).show();

            //Get ProductNode
            userReference.child(user.getKey()).setValue(user);
            this.user = user;

            uploadPicture(user.getKey(), imgUri);

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public User getOneUser(String key) {
        userReference.child(key).addValueEventListener(oneUserListener);

        return this.user;
    }

    public void refreshToken(String token) {
        userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/token" ).setValue(token);
    }


    public void uploadPicture(final String key, Uri imageUri) {

        if (imageUri != null) {

            try {

                final String filename = key + "." + Utils.getFileExtension(imageUri, context);
                StorageReference fileref =  storageReference.child(filename);
                fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        user.setUrl_img(taskSnapshot.getDownloadUrl().toString());
                        user.setImgName(filename);

                        userReference.child(key).setValue(user);

                        Toast.makeText(context.getApplicationContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            catch (Exception e) {

            }

        }

    }


    //Listener for DataSnatshot Product
    private ValueEventListener oneUserListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            user = dataSnapshot.getValue(User.class);

            if (location.equals("Cart")) {

                ProductDriver.usersys = user;

            }
            else {

                tvUser.setText(user.getName());
                tvMail.setText(user.getEmail());
                Picasso.get().load(user.getUrl_img()).fit().centerCrop().into(imgUser);
                navAdmin.setVisible(user.getRoles().contains("admin"));

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
