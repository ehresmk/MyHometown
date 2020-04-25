package com.app.MyHometown.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import java.util.List;

public class CityActivity extends AppCompatActivity {

    TextView cityName;
    TextView stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        cityName = findViewById(R.id.City);
        stateName = findViewById(R.id.State);
        final Button back = findViewById(R.id.back);
        final Button alert = findViewById(R.id.CreateAlert);
        boolean check = ParseUser.getCurrentUser().getBoolean("Admin");

        cityName.clearComposingText();
        stateName.clearComposingText();

        if(check){
            alert.setVisibility(View.VISIBLE);
        }
        else{
            alert.setVisibility(View.INVISIBLE);
        }

        getCity();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName.clearComposingText();
                stateName.clearComposingText();
                Intent intent = new Intent(CityActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
                    if(obj.getBoolean("open")){
                        cityName.setText(obj.getString("cityName"));
                        stateName.setText(obj.getString("cityState"));
                        obj.put("open", false);
                    }
                    else {
                        obj = cities.get(1);
                        cityName.setText(obj.getString("cityName"));
                        stateName.setText(obj.getString("cityState"));
                        obj.put("open", false);
                    }
                }
                else {
                    //Exception
                }
            }
        });
    }

}
