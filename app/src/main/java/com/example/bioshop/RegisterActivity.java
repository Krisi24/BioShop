package com.example.bioshop;

import static com.example.bioshop.Notify.CHANNEL_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    TextView cimTextView;
    EditText userNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    EditText phoneEditText;
    EditText addressEditText;
    Spinner spinner;
    RadioGroup accountTypeGroup;

    Animation animFadeIn;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    private NotificationManagerCompat notificationManagerCompat;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.editTextPasswordConfirm);
        phoneEditText = findViewById(R.id.phoneEditText);
        spinner = findViewById(R.id.phoneSpinner);
        addressEditText = findViewById(R.id.addressEditText);
        cimTextView = findViewById(R.id.registrationTextView);
        accountTypeGroup = findViewById(R.id.accountTypeRadioGroup);
        accountTypeGroup.check(R.id.buyerRadioButton);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userEmailEditText.setText(userName);
        passwordEditText.setText(password);
        passwordConfirmEditText.setText(password);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phone_labels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        sendOnChannel();
    }

    public void sendOnChannel(){
        String title = "BEJELENTKEZÉS";
        String info = "Google email-el be lehet jelentkezni regisztráció nélkül!";

        Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifi)
                .setContentTitle(title)
                .setContentText(info)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManagerCompat.notify(1, notification2);
    }

    public void sendOnChannel2(){
        String title = "Sikeres regisztráció";
        String info = "A regisztráció sikeresen megtörtént!";

        Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifi)
                .setContentTitle(title)
                .setContentText(info)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManagerCompat.notify(1, notification2);
    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            return;
        }

        String phone = phoneEditText.getText().toString();
        String phoneType = spinner.getSelectedItem().toString();

        int accountTypeId = accountTypeGroup.getCheckedRadioButtonId();
        View radioButton = accountTypeGroup.findViewById(accountTypeId);
        int id = accountTypeGroup.indexOfChild(radioButton);
        String accountType =  ((RadioButton)accountTypeGroup.getChildAt(id)).getText().toString();

        Log.i(LOG_TAG, "Regisztrált: " + userName + ", e-mail: " + email);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(RegisterActivity.this, "Register notification");
//        builder.setContentTitle("Sikeres regisztráció");
//        builder.setAutoCancel(true);
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(RegisterActivity.this);
//        managerCompat.notify(1, builder.build());

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    sendOnChannel2();
                    startShopping();
                } else {
                    Log.d(LOG_TAG, "User was't created successfully:", task.getException());
                    Toast.makeText(RegisterActivity.this, "User was't created successfully:", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    private void startShopping(/* registered used class */) {
        Intent intent = new Intent(this, ShopListActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");

        cimTextView.startAnimation(animFadeIn);
        userNameEditText.startAnimation(animFadeIn);
        userEmailEditText.startAnimation(animFadeIn);
        passwordEditText.startAnimation(animFadeIn);
        passwordConfirmEditText.startAnimation(animFadeIn);
        phoneEditText.startAnimation(animFadeIn);
        accountTypeGroup.startAnimation(animFadeIn);
        spinner.startAnimation(animFadeIn);
        addressEditText.startAnimation(animFadeIn);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");

//        SharedPreferences.Editor editor = preferences.edit();
//        if(!userEmailEditText.getText().toString().isEmpty()){
//            editor.putString("userName", userEmailEditText.getText().toString());
//        }
//        if(!passwordEditText.getText().toString().isEmpty()){
//            editor.putString("password", passwordEditText.getText().toString());
//        }
//        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Log.i(LOG_TAG, selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
