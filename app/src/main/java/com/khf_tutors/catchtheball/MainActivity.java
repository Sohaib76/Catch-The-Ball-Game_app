package com.khf_tutors.catchtheball;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    //position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;




    // Initializing Class (Creating objects)
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Status check
    private boolean action_flg = false;
    private boolean start_flg  = false;

    // Score
    private int score = 0;

    //Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ID
        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        black = findViewById(R.id.black);

        // Get screen size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        // (Move to out of screen)
        orange.setX(-80);
        orange.setY(-80);

        pink.setX(-80);
        pink.setY(-80);

        black.setX(-80);
        black.setY(-80);


        scoreLabel.setText("Score :  0");


        // Temporary
       // startLabel.setVisibility(View.INVISIBLE);
        // boxY = 500;





    }


    public void changePos (){
        // Hit Checking
        hitCheck();

        // Orange
        orangeX -= 12;
        if(orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));

        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Black
        blackX -= 16;
        if(blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        // Pink
        pinkX -= 20;
        if(pinkX < 0){
            pinkX = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);




        // Move box
        if (action_flg == true){
            // Touching
            boxY -=20;
        }else{
            // Releasing
            boxY += 20;
        }

        // Check position
        if (boxY < 0) boxY =0 ;

        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;
        box.setY(boxY);

        scoreLabel.setText("Score :" + score);

    }

    private void hitCheck() {
        // If center of ball is in box it counts a hit

        //Orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;


        if (0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize) {
            score += 10;
            orangeX = -10;
        }


        // Pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {
            score += 30;
            pinkX = -10;
        }

        // Black
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        if (0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize) {
           // Stop timer

           timer.cancel();
           timer = null;

           // Show Result
            Intent intent = new Intent(getApplicationContext(), Result.class);
            intent.putExtra("SCORE" , score);
            startActivity(intent);



        }
















    }

    // Touch the screen the box will move. (default method)
    public boolean onTouchEvent(MotionEvent motionEvent){

        if (start_flg== false){

            start_flg = true;
            startLabel.setVisibility(View.GONE);

            // For box not go out of screen
            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();

            // Box is square height and width are same.
            boxSize = box.getHeight();

            // Timer
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);


        }

        else{
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }




        return true;
    }
}
