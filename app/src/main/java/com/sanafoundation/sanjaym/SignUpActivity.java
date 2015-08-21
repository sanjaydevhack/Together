package com.sanafoundation.sanjaym;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends Activity {

    //private Toolbar mToolbar;
    private EditText enterUserName, createPassword, confirmPassword, enterEmail, enterFullName;
    private Button signUp;
    private String uName, uPass, confirmPass, mail, fullName;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mProgressDialog = new ProgressDialog(SignUpActivity.this);
        mProgressDialog.setMessage("Creating Account Please Wait...");

        enterUserName = (EditText) findViewById(R.id.create_uName);
        createPassword = (EditText) findViewById(R.id.create_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        enterEmail = (EditText) findViewById(R.id.enter_mail);
        enterFullName = (EditText) findViewById(R.id.full_name);


        signUp = (Button) findViewById(R.id.create_account);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                uName = enterUserName.getText().toString();
                uPass = createPassword.getText().toString();
                confirmPass = confirmPassword.getText().toString();
                mail = enterEmail.getText().toString();
                fullName = enterFullName.getText().toString();

                if (isValidEmail(mail)) {
                    ParseUser user = new ParseUser();
                    user.setUsername(uName);
                    user.setPassword(confirmPass);

                    user.setEmail(mail);
                    user.put("fullname", fullName);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Toast.makeText(getApplicationContext(), "Hooray! you can use the app now.", Toast.LENGTH_LONG).show();

                                Intent openHomeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(openHomeActivity);

                                mProgressDialog.hide();
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Toast.makeText(getApplicationContext(), "Sorry! something went wrong.", Toast.LENGTH_LONG).show();
                                mProgressDialog.hide();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid email address!", Toast.LENGTH_LONG).show();
                    enterEmail.setError("Enter valid email address!");

                    mProgressDialog.hide();
                }

                mProgressDialog.hide();
            }
        });

    }

    /*private void setActionBar(Toolbar mToolbar) {
        mToolbar.inflateMenu(R.menu.menu_sign_up);
        mToolbar.getMenu();
        mToolbar.setTitle(getResources().getString(R.string.title_activity_sign_up));
    }*/

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
