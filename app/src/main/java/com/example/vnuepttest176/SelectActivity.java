package com.example.vnuepttest176;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SelectActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView translateCard;
    private CardView practiceCard;
    private CardView pronunciationCard;
    private CardView dictionaryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        //defining cards
        translateCard = (CardView) findViewById (R.id.translateGrid);
        practiceCard = (CardView) findViewById (R.id.practiceGrid);
        pronunciationCard = (CardView) findViewById (R.id.pronunciationGrid);
        dictionaryCard = (CardView) findViewById (R.id.dictionaryGrid);
        //add click listener to the cards
        translateCard.setOnClickListener (this);
        practiceCard.setOnClickListener (this);
        pronunciationCard.setOnClickListener (this);
        dictionaryCard.setOnClickListener (this);
    }
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId ()){
            case R.id.translateGrid :
                i = new Intent (this,TranslateActivity.class);
                startActivity (i);
                break;
            case R.id.practiceGrid :
                i = new Intent (this,PracticeActivity.class);
                startActivity (i);
                break;
            case R.id.pronunciationGrid :
                i = new Intent (this,PronunciationActivity.class);
                startActivity (i);
                break;
            case R.id.dictionaryGrid :
                i = new Intent (this,MainActivity.class);
                startActivity (i);
                break;
            default:
                break;
        }
    }
}