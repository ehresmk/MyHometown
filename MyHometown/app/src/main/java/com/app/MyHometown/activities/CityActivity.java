//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This Activity class is for the main city page. It is dynamic //
//  in that it loads the specific page for the specific city     //
//  that the user has clicked for. Admins will be able to click  //
//  to create alerts and normal users will be able to click to   //
//  subscribe. This page also displays all the previously        //
//  created alerts which it retrieves from the database.         //
//---------------------------------------------------------------//

package com.app.MyHometown.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.app.MyHometown.R;
import com.app.MyHometown.ui.login.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    TextView cityName;
    TextView stateName;
    TextView list;
    Button sub;
    ParseObject city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        cityName = findViewById(R.id.City);
        stateName = findViewById(R.id.State);
        list = findViewById(R.id.alerts);
        final Button back = findViewById(R.id.back);
        sub = findViewById(R.id.subscribe);
        final Button alert = findViewById(R.id.CreateAlert);
        boolean check = ParseUser.getCurrentUser().getBoolean("Admin");

        if(check) {
            sub.setVisibility(View.INVISIBLE);
            alert.setVisibility(View.VISIBLE);
        }
        else {
            sub.setVisibility(View.VISIBLE);
            alert.setVisibility(View.INVISIBLE);
        }

        getCity();

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSub()) {
                    removeSub();
                }
                else {
                    addSub();
                }
            }
        });

        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, CreateAlert.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName.clearComposingText();
                stateName.clearComposingText();
                city.put("open", false);
                city.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(CityActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    //Removes the current subscription from the current user by changing
    //the "Subscribed" value to nothing.
    private void removeSub() {
        ParseUser.getCurrentUser().put("Subscribed", "");
        ParseUser.getCurrentUser().saveInBackground();
        sub.setText("Subscribe");
    }
    //Sets the current users "Subscribed" value to the current city
    private void addSub() {
        ParseUser.getCurrentUser().put("Subscribed", city.get("cityName"));
        ParseUser.getCurrentUser().saveInBackground();
        sub.setText("Unsubscribe");
    }
    //Checks the if the current user is subscribed to the opened city page
    private boolean checkSub() {
        if(city.getString("cityName") == ParseUser.getCurrentUser().get("Subscribed")) {
            return true;
        }
        else {
            return false;
        }
    }
    //Retrieves a list of all the alerts in the database and adds them
    //to the page based on whether or not their "City" value is equal
    //to the current city page that is open.
    private void getAlerts() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Alert");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> alerts, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < alerts.size(); i++) {
                        if(alerts.get(i).getString("City").equals(city.getString("cityName"))) {
                            StringBuilder alert = new StringBuilder("-" + alerts.get(i).getString("Title"));
                            alert.append("\n" + " "+ alerts.get(i).getString("Description") + "\n\n");
                            list.append(alert);
                        }
                    }
                }
                else {
                    //Exception
                }
            }
        });
    }
    //Gets the "open" city from the database by checking which cities'
    //"open" variable is true. It then sets the text on the page based
    //on that finding.
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
                    cityName.setText(obj.getString("cityName").trim());
                    stateName.setText(obj.getString("cityState").trim());
                    city = obj;
                    getAlerts();
                    if(checkSub()) {
                        sub.setText("Unsubscribe");
                    }
                    else {
                        sub.setText("Subscribe");
                    }
                }
                else {
                    //Exception
                }
            }
        });
    }
}
