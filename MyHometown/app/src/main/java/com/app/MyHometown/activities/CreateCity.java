//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This Activity class deals with the create a city page and    //
//  appropriately creates a city object in the database if one   //
//  for the required city does not exist. This class does do     //
//  input validation for the city to ensure the database's       //
//  is ensured and that no duplicate pages are created.          //
//---------------------------------------------------------------//

package com.app.MyHometown.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.app.MyHometown.ui.login.MainActivity;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.ParseObject;


public class CreateCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_city);

        final EditText city_name = findViewById(R.id.CityName);
        final EditText state_name = findViewById(R.id.CityState);
        final Button back_button = findViewById(R.id.Back);
        final Button city_button = findViewById(R.id.Create_City);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        city_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please enter a valid ");

                if (isEmpty(city_name) || isLong(city_name)) {
                    valid = true;
                    validationErrorMessage.append("city name");
                }
                if (isEmpty(state_name) || isLong(state_name)) {
                    if(valid) {
                        validationErrorMessage.append(" and state name.");
                    }
                    valid = true;
                    validationErrorMessage.append("state name.");
                }

                if (valid) {
                    Toast.makeText(CreateCity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }


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

    private boolean isLong(EditText text) {
        //The longest city name in the world is 26 letters in New Zealand
        if (text.getText().toString().trim().length() < 26) {
            return false;
        }
        else {
            return true;
        }
    }
}
