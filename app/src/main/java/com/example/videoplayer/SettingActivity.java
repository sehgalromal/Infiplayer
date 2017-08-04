package com.example.videoplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class SettingActivity extends Activity {
    Button toggle;
    static String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
       toggle = (Button) findViewById(R.id.button);
        toggle.setText(status);

    }
    public void changeTheme(View view)
    {
         status=toggle.getText().toString();

        if(status.equals("OFF"))
        {
            toggle.setText("ON");
            Utils.changeToTheme(SettingActivity.this,Utils.THEME_DARK);


        }
        else
       if(status.equals("ON") )
        {
            toggle.setText("OFF");
            Utils.changeToTheme(SettingActivity.this,Utils.THEME_WHITE);
        }

    }


}

