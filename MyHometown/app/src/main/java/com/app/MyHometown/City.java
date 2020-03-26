//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This class implements the City object for MyHometown. There  //
//  are variables for the name of the City and the state the     //
//  City is located in. I used an Array List to store the alerts //
//  that are posted to the City page.                            //
//---------------------------------------------------------------//

package com.app.MyHometown;
import com.app.MyHometown.Alert;
import java.util.ArrayList;
import java.util.List;

public class City<string>
{
    private string name;
    private string state;
    private List<Alert> alerts;

    public City(string name, string state)
    {
        this.name = name;
        this.state = state;
        alerts = new ArrayList<>();
    }

    public string getName()
    {
        return name;
    }

    public string getState()
    {
        return state;
    }

    public void addAlert(Alert newAlert)
    {
        alerts.add(newAlert);
    }
}
