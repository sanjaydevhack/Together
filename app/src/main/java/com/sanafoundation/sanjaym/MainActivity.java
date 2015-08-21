package com.sanafoundation.sanjaym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sanafoundation.sanjaym.helper.ParseUtils;
import com.sanafoundation.sanjaym.helper.PrefManager;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText uNam, uPass;
    private Button signIn;
    private TextView registerAccount;
    private String userName, password, objectId, fullName, profilePic;
    private ProgressDialog mProgressDialog;

    private PrefManager pref;

    private String encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing Firebase
        Firebase myFirebaseRef = new Firebase("https://crackling-fire-6726.firebaseio.com/Sanjay");

        // Verifying Parse configuration. This is method is for developers only.
        ParseUtils.verifyParseConfiguration(this);

        pref = new PrefManager(getApplicationContext());
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);

            finish();
        }

        setContentView(R.layout.activity_main);

        uNam = (EditText) findViewById(R.id.uNameET);
        uPass = (EditText) findViewById(R.id.uPassET);
        signIn = (Button) findViewById(R.id.loignBtn);
        signIn.setOnClickListener(this);

        registerAccount = (TextView) findViewById(R.id.no_acc_create_one);

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void login() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        userName = uNam.getText().toString();
        password = uPass.getText().toString();

        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    objectId = ParseUser.getCurrentUser().getObjectId();
                    fullName = (String) ParseUser.getCurrentUser().get("fullname");

                    final ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("pic");
                    if (profileImage != null && profileImage.getUrl() != null && profileImage.getUrl().length() > 0) {
                    profileImage.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null && bytes != null) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0,
                                        bytes.length);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm pis the bitmap object
                                byte[] b = baos.toByteArray();

                                encoded = Base64.encodeToString(b, Base64.DEFAULT);
                                pref.createProfilePic(encoded);
                                Log.d("Encoded if :-)", encoded);


                            } else {
                                Toast.makeText(getApplicationContext(), "Unable to get image", Toast.LENGTH_LONG).show();
                            }
                        }
                    });}else{
                        Bitmap place_holder_icon = BitmapFactory.decodeResource(getResources(), R.drawable.place_holder);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        place_holder_icon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        pref.createProfilePic(encoded);
                        Log.d("Encoded else :-)", encoded);

                    }


                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user", ParseObject.createWithoutData("_User", objectId));
                    installation.saveInBackground();

                    if (fullName == null) {
                        fullName = "Unknown";
                    }

                    Toast.makeText(getApplicationContext(), fullName, Toast.LENGTH_LONG).show();

                    pref.createLoginSession(userName, objectId, fullName);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    mProgressDialog.show();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                    mProgressDialog.hide();
                }
            }
        });
        mProgressDialog.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loignBtn:
                login();
                break;
            default:
        }
    }
}