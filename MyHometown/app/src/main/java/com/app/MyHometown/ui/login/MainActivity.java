//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This Activity class deals with the logout of the user from   //
//  the main screen. (This class is subject to change because it //
//  controls the main screen functionality, right now logout is  //
//  the only functionality this has). It logs out the user from  //
//  the Back4App server and returns the user to the login screen.//
//---------------------------------------------------------------//

package com.app.MyHometown.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.MyHometown.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button logout_button = findViewById(R.id.logout);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Logging Out...");
                dlg.show();
                ParseUser.logOut();
                alertDisplayer("Logging Out...", "Goodbye!");
            }
        });
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
