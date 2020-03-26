//---------------------------------------------------------------//
//  Kevin Ehresman - MyHometown                                  //
//                                                               //
//  This class implements the Alert object for MyHometown. There //
//  are variables for the title of the Alert and the full        //
//  message/description of the Alert.                            //
//---------------------------------------------------------------//

package com.app.MyHometown;

public class Alert<string>
{
    private string title;
    private string message;

    public Alert(string title, string message)
    {
        this.title = title;
        this.message = message;
    }

    public void setTitle(string title)
    {
        this.title = title;
    }

    public void setMessage(string message)
    {
        this.message = message;
    }

    public string getTitle()
    {
        return title;
    }

    public string getMessage()
    {
        return message;
    }
}
