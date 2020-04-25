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
import android.widget.ScrollView;

import com.app.MyHometown.R;
import com.app.MyHometown.activities.CityActivity;
import com.app.MyHometown.activities.CreateCity;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button logout_button = findViewById(R.id.logout);
        final Button city_button = findViewById(R.id.create_city);
        final Button city1 = findViewById(R.id.city1);
        final Button city2 = findViewById(R.id.city2);
        final ScrollView list = findViewById(R.id.viewList);
        boolean check = ParseUser.getCurrentUser().getBoolean("Admin");

        if(check){
            city_button.setVisibility(View.VISIBLE);
        }
        else{
            city_button.setVisibility(View.INVISIBLE);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cities, ParseException e) {
                if (e == null) {
                    ParseObject obj = cities.get(0);
                    ParseObject obj2 = cities.get(1);
                    city1.setText(obj.getString("cityName") + ", " + obj.getString("cityState"));
                    city2.setText(obj2.getString("cityName") + ", " + obj2.getString("cityState"));
                }
                else {
                //Exception
                }
            }
        });

        city1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCity("Milltown");
            }
        });

        city2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCity("Edison");
            }
        });

        city_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateCity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

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

    //This method displays an alert with the given title and message on the current page
    //and then switches to the login activity class when the user logs out
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

    private void getCity(String cityName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.whereEqualTo("cityName", cityName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> city, ParseException e) {
                if (e == null){
                    ParseObject obj = city.get(0);
                    obj.put("open", true);
                }
                else {
                    //Exception
                }
            }
        });
        Intent intent = new Intent(MainActivity.this, CityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
