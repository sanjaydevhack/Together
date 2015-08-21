package com.sanafoundation.sanjaym;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sanafoundation.sanjaym.helper.PrefManager;

import java.util.Arrays;
import java.util.List;


public class MultiLoginActivity extends AppCompatActivity {

    private Button facebookLogin,twitterLogin, login, register;
    private Dialog progressDialog;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new PrefManager(getApplicationContext());

        if (pref.isLoggedIn()) {
            Intent intent = new Intent(MultiLoginActivity.this, HomeActivity.class);
            startActivity(intent);

            finish();
        }
        setContentView(R.layout.activity_multi_login);

        facebookLogin= (Button) findViewById(R.id.facebook_login);
        twitterLogin= (Button) findViewById(R.id.twitter_login);
        register= (Button) findViewById(R.id.register);
        login= (Button) findViewById(R.id.login);


        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> permissions = Arrays.asList("public_profile", "email");

                ParseFacebookUtils.logInWithReadPermissionsInBackground(MultiLoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        progressDialog.dismiss();
                        if (user == null) {
                            Toast.makeText(getApplicationContext(), "Uh oh. The user cancelled the Facebook login.", Toast.LENGTH_LONG).show();
                            Log.d("DetectingFacebookLogin", user.toString());
                        } else if (user.isNew()) {
                            Toast.makeText(getApplicationContext(), "User signed up and logged in through Facebook!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "User logged in through Facebook!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_button = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(register_button);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_button = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login_button);
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.whereEqualTo("playerName", "Dan Stemkoski");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_login, menu);
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

}
