package com.example.stylish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {
    ImageView user;
    EditText name, email, phone, pass, cpass, address;
    Button register;
    RadioButton male, female;
    ProgressBar progressBar;
    TextView signin;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImageUri;

    FirebaseAuth mAuth;
    //private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.imageView);
        name = findViewById(R.id.NameET);
        email = findViewById(R.id.EmailET);
        phone = findViewById(R.id.PhoneET);
        pass = findViewById(R.id.PasswordET);
        cpass = findViewById(R.id.CPasswordET);
        address = findViewById(R.id.AddressET);
        male = findViewById(R.id.radioButtonM);
        female = findViewById(R.id.radioButtonF);
        register = findViewById(R.id.buttonBTN);
        signin=findViewById(R.id.signIn_text);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this, LogIn.class);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                mAuth = FirebaseAuth.getInstance();

                final String userName = name.getText().toString();
                final String userEmail = email.getText().toString();
                final String userPhone = phone.getText().toString();
                final String userPass = pass.getText().toString();
                final String cUserPass = cpass.getText().toString();
                final String userAddress = address.getText().toString();
                final String Male = male.getText().toString();
                final String Female = female.getText().toString();

                if (userName.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() || userPass.isEmpty()
                        || cUserPass.isEmpty() || userAddress.isEmpty() ||
                        !userPass.equals(cUserPass)) {
                    showMessage("Please verify all the fields");
                    register.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    //CreateUserAccount method will create the user if the email is valid
                    CreateUserAccount(userName, userEmail, userPass);
                }
            }
        });


        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });
    }

    private void checkAndRequestForPermission()
    {
        if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Register.this, "Please accept for required permission", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        , PReqCode);
            }
        } else {
            openGallery();

        }
    }

    private void CreateUserAccount(final String userName, final String userEmail, String userPass) {
        //This method will create user account with specific email and password
        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user account created successfully
                            showMessage("Your Account has been created");
                            //after created, we need update user account
                            updateUserInfo(pickedImageUri, userName, mAuth.getCurrentUser());
                        } else {
                            showMessage("Account creation failed" + task.getException().getMessage());
                            register.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private void showMessage(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();    }

    private void updateUserInfo(Uri pickedImageUri, final String userName, final FirebaseUser currentUser) {
        //update user account //first we need to upload photo to Firebase storage and then get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image uploaded successfully
                //now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //uri contains user image url

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) //userInfo updated successfully
                                        {
                                            showMessage("Register Complete");
                                            updateUI();
                                        }

                                    }
                                });


                    }

                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            pickedImageUri = data.getData();
            user.setImageURI(pickedImageUri);
        }
    }

    private void updateUI()
    {
        Intent intent=new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
        finish();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }
}
