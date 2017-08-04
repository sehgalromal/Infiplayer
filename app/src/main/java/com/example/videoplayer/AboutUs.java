package com.example.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutUs extends Activity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView tv = (TextView) findViewById(R.id.infitxt);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/arista.ttf");

        tv.setTypeface(face);
        Button sharingButton = (Button) findViewById(R.id.buttonmail);

        sharingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                shareIt();

            }
        });


    }

    public void facebook(View view){
        String fbpage="http://www.facebook.com/infiplayer2523";
        Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(fbpage));

    }

    private void shareIt() {


        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Request from CMH");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, " ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));



    }


}

