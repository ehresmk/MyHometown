//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This Activity class handles the creation of a new user       //
//  account. It reads username and password data from the user   //
//  and then signs up the user through the Back4App system.      //
//  Once the user successfully signs up, this class logs them in //
//  and transfers them to the MyHometown main screen. There is   //
//  also a "Back" button that the user can click which will      //
//  return them to the Login screen.                             //
//---------------------------------------------------------------//

package com.app.MyHometown.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.app.MyHometown.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateActivity extends AppCompatActivity {

    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dlg = new ProgressDialog(CreateActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Returning to login...");
                dlg.show();
                Intent intent = new Intent(CreateActivity.this, LoginActivity.class);
                dlg.dismiss();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.Password);
        passwordAgainView = (EditText) findViewById(R.id.PasswordAgain);

        final Button signup_button = findViewById(R.id.createNew);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");

                if (isEmpty(usernameView)) {
                    validationError = true;
                    validationErrorMessage.append("an username");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a password");
                }

                if (isEmpty(passwordAgainView)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your password again");
                } else {
                    if (!isMatching(passwordView, passwordAgainView)) {
                        if (validationError) {
                            validationErrorMessage.append(" and ");
                        }
                        validationError = true;
                        validationErrorMessage.append("the same password twice.");
                    }
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(CreateActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                //Setting up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(CreateActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Creating an account...");
                dlg.show();

                ParseUser user = new ParseUser();
                user.setUsername(usernameView.getText().toString());
                user.setPassword(passwordView.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            dlg.dismiss();
                            alertDisplayer("Successful Login", "Welcome " + usernameView.getText().toString() + "!");
                        } else {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(CreateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isMatching(EditText text1, EditText text2){
        if(text1.getText().toString().equals(text2.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
