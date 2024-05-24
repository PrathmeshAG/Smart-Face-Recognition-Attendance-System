package com.example.collegeproject;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Activity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animation);

        TextView animatedText = findViewById(R.id.animated_text);
        TextView animatiText_1 = findViewById(R.id.animated_text_1);
        TextView animatiText_2 = findViewById(R.id.animated_text_2);


        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation_activity);
        animatiText_1.startAnimation(fadeInAnimation);
        animatiText_2.startAnimation(fadeInAnimation);
        animatedText.startAnimation(fadeInAnimation);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(Splash_Activity.this,MainActivity.class);
                Splash_Activity.this.startActivity(mainIntent);
                Splash_Activity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        // set text in various animation text view
        final String textinanimatedText = animatiText_1.getText().toString();

        animatiText_1.setText("");

        ValueAnimator animator = ValueAnimator.ofInt(0, textinanimatedText.length());

        animator.setDuration(2000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                animatiText_1.setText(textinanimatedText.substring(0, animatedValue));


            }


        });
        animator.start();
    }
}
