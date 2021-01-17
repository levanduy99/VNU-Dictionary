package com.example.vnuepttest176;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PronunciationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_pronunciation);

        final MediaPlayer soundI = MediaPlayer.create (this,R.raw.i);
        Button playSoundI = (Button) this.findViewById (R.id.soundI);

        playSoundI.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                soundI.start ();
            }
        });
    }
}