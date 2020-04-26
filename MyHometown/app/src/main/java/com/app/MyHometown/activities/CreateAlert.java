package com.app.MyHometown.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.MyHometown.R;
import com.app.MyHometown.activities.CreateCity;
import com.app.MyHometown.ui.login.MainActivity;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CreateAlert extends AppCompatActivity {

    ParseObject city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alert);
        final Button back = findViewById(R.id.back);
        final Button create_alert = findViewById(R.id.CreateAlert);
        final EditText title = findViewById(R.id.Title);
        final EditText description = findViewById(R.id.Description);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAlert.this, CityActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        create_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject new_alert = new ParseObject("Alert");
                new_alert.put("Title", title.getText().toString());
                new_alert.put("Description", description.getText().toString());
                new_alert.put("City", city);
                new_alert.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            alertDisplayer("New alert successfully created!", "Please click OK to continue.");
                            Intent intent = new Intent(CreateAlert.this, CityActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            //Error
                            alertDisplayer("Alert could not be created.", "Please try again.");
                        }
                    }
                });
            }
        });
    }

    private void getCity() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cities, ParseException e) {
                if (e == null) {
                    ParseObject obj = cities.get(0);
                    int i = 1;
                    while(!obj.getBoolean("open")) {
                        obj = cities.get(i);
                        i++;
                    }
                    city = obj;
                }
                else {
                    //Exception
                }
            }
        });
    }

    //This method displays an alert with the given title and message on the current page
    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAlert.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
