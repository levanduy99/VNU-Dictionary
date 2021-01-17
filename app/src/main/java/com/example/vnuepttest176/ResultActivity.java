package com.example.vnuepttest176;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    ImageView imgTotal,imgCorrect,imgWrong,playAgain;
    TextView txv_total_qestion;
    TextView txv_correct_answer;
    TextView txv_wrong_answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_result);
        //mapping
        imgTotal    =  (ImageView) findViewById(R.id.total);
        imgCorrect  =  (ImageView) findViewById(R.id.correct);
        imgWrong    =  (ImageView) findViewById(R.id.wrong);
        playAgain   =  (ImageView) findViewById (R.id.play_again);

        txv_total_qestion = (TextView) findViewById (R.id.txv_total_question);
        txv_correct_answer = (TextView) findViewById (R.id.txv_correct_answer);
        txv_wrong_answer = (TextView) findViewById (R.id.txv_wrong_answer);

        //create the animations
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_rotate);
        imgTotal.startAnimation(animation);
        imgCorrect.startAnimation(animation);
        imgWrong.startAnimation(animation);

        //get question,correct,wrong from PracticeActivity
        Intent i = getIntent ();
        String question = i.getStringExtra("total_question");
        String correct = i.getStringExtra ("correct");
        String wrong = i.getStringExtra ("incorrect");

        //set text
        txv_total_qestion.setText (question);
        txv_correct_answer.setText (correct);
        txv_wrong_answer.setText (wrong);

        playAgain.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ResultActivity.this,PracticeActivity.class);
                startActivity (intent);
            }
        });

    }

}