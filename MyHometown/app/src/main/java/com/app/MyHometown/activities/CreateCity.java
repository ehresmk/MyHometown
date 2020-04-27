//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This Activity class deals with the creating a city page and  //
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.MyHometown.R;
import com.app.MyHometown.ui.login.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import java.util.List;


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
                final ProgressDialog dlg = new ProgressDialog(CreateCity.this);
                dlg.setTitle("New City page successfully created!");
                dlg.setMessage("Please click OK to continue.");
                dlg.show();

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

                ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
                query.whereEqualTo("cityName", city_name.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> city, ParseException e) {
                        if (e == null) {
                            if (city.size() > 0) {
                                dlg.dismiss();
                                //Throw dialog, city already exists
                                alertDisplayer("A city page for that city already exists.", "Please enter a different city.");
                            }
                            else {
                                //Create new city object
                                ParseObject new_city = new ParseObject("City");
                                new_city.put("cityName", city_name.getText().toString());
                                new_city.put("cityState", state_name.getText().toString());
                                new_city.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            dlg.dismiss();
                                            alertDisplayer("City page successfully created!", "Please click OK to continue.");
                                            Intent intent = new Intent(CreateCity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        else {
                                            dlg.dismiss();
                                            //Error
                                            alertDisplayer("City page could not be created.", "Please try again.");
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            //Throw exception: some unknown error
                        }
                    }
                });


            }
        });
    }
    //This method checks if the passed text is empty and returns a boolean
    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        }
        else {
            return true;
        }
    }
    //This method checks if the passed text is too long (specifically for
    //city and state names to ensure security from injection)
    private boolean isLong(EditText text) {
        //The longest city name in the world is 26 letters in New Zealand
        if (text.getText().toString().trim().length() < 26) {
            return false;
        }
        else {
            return true;
        }
    }

    //This method displays an alert with the given title and message on the current page
    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateCity.this)
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
