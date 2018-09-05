package com.info.inet.pm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserActivity extends AppCompatActivity {

    GoogleSignInAccount account;

    String userName, UserId, Email, Photo;
    TextView DisplayName, Id, Mail, Pic;
    ImageView userDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        DisplayName = findViewById(R.id.userName);
        Mail = findViewById(R.id.UserEmail);
        userDP = findViewById(R.id.UserimageView);

        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            userName = account.getDisplayName();
            UserId = account.getId();
            Email = account.getEmail();
            Photo = String.valueOf(account.getPhotoUrl());

        }
        DisplayName.setText(userName);
        Mail.setText(Email);
        Glide.with(this)
                .load(Photo)
                .apply(new RequestOptions()
                    .placeholder(R.color.colorAccent))
                .thumbnail(0.1f)
                .into(userDP);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
