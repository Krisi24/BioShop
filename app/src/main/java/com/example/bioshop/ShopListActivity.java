package com.example.bioshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShopListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private FirebaseAuth mFirebaseAuth;
    private TextView TVuserEmail;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        mFirebaseAuth = FirebaseAuth.getInstance();

        TVuserEmail = findViewById(R.id.TextViewUserEmail);


    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            Log.d(LOG_TAG, "Authenticated user!");
            TVuserEmail.setText( "Bejelentkezett email címmel: " + mFirebaseAuth.getCurrentUser().getEmail());
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    public void logout(View view) {
        mFirebaseAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}