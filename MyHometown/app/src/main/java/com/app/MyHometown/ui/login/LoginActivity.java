package com.app.MyHometown.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.MyHometown.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.LogInCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameView;
    private EditText passwordView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);

        final Button login_button = findViewById(R.id.login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean validation = false;
                StringBuilder errorMsg = new StringBuilder("Please, insert");

                if(isEmpty(usernameView))
                {
                    validation = true;
                    errorMsg.append("an username");
                }
                else if(isEmpty(passwordView))
                {
                    if(validation) {
                        errorMsg.append(" and ");
                    }
                    validation = true;
                    errorMsg.append("a password");
                }
                errorMsg.append(".");

                if(validation)
                {
                    Toast.makeText(LoginActivity.this, errorMsg.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog prog = new ProgressDialog(LoginActivity.this);
                prog.setTitle("Just a moment.");
                prog.setMessage("Logging in...");
                prog.show();

                ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null)
                        {
                            prog.dismiss();
                            alertDisplayer("Successfully logged in!", "Welcome back!");
                        }
                        else
                        {
                            prog.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        final Button create_button = findViewById(R.id.create);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private boolean isEmpty(EditText text)
    {
        if(text.getText().toString().trim().length()>0)
            return false;
        else
            return true;
    }

    private void alertDisplayer(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
