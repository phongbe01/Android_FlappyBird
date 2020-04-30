package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.flappybird.Model.Bird;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final  int UPDATE_MILLISECONDS = 30;
    Bitmap background;
    Bitmap topTube, bottomTube;
    Display display;
    Point point;
    int SCREEN_WIDTH, SCREEN_HEIGHT;
    Rect rect,rect1;
    Bitmap[] birds;
    int BIRD_FRAME_0 = R.drawable.flappy_bird1_70x70;
    int BIRD_FRAME_1 = R.drawable.flappy_bird2_70x70;
    int BACKGROUND = R.drawable.background;
    int TOP_TUBE = R.drawable.top_tube;
    int BOTTOM_TUBE = R.drawable.bottom_tube;
    int frame = 0;
    int velocity = 0, gravity = 3;
    // check bird position
    int bird_x, bird_y;

    boolean gameState = false;
    int gap = 400; //distance between top tube and bottom tube
    int minTubeOffset, maxTubeOffset; // min, max of tube's height
    int numberOfTube = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTube];
    int[] topTubeY = new int[numberOfTube]; // height of top tube
    Random random;
    int tubeVelocity = 10; //tube speed

    public GameView(Context context) {
        super(context);
        handler = new android.os.Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); // call onDraw()
            }
        };
        background = BitmapFactory.decodeResource(getResources(),BACKGROUND);
        topTube = BitmapFactory.decodeResource(getResources(), TOP_TUBE);
        bottomTube = BitmapFactory.decodeResource(getResources(), BOTTOM_TUBE);
        //create background
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        rect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        //create bird
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(), BIRD_FRAME_0);
        birds[1] = BitmapFactory.decodeResource(getResources(), BIRD_FRAME_1);

        //set bird position
        bird_x = SCREEN_WIDTH/2 - birds[frame].getWidth()/2;
        bird_y = SCREEN_HEIGHT/2 - birds[frame].getHeight()/2; // position between x,y to the top of the screen

        rect1 = new Rect(bird_x, SCREEN_HEIGHT/2 + birds[frame].getWidth()/2, SCREEN_WIDTH/2 + birds[frame].getWidth()/2, bird_y);

        //create tubes
        distanceBetweenTubes = SCREEN_WIDTH * 3/4;
        minTubeOffset = gap/2;  //min height tube
        maxTubeOffset = SCREEN_HEIGHT - minTubeOffset - gap; //max heigth tube
        random = new Random();
        for (int i =0; i< numberOfTube;i++)
        {
            tubeX[i] = SCREEN_WIDTH + i*distanceBetweenTubes; //position tube appear
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1); //topTubeY will vary between minTube and maxTube offset

        }

    }


    // Draw view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw the background
        canvas.drawBitmap(background, null, rect, null);
        if (frame == 0)
        {
            frame = 1;
        } else {
            frame = 0;
        }
        if (gameState) {
            // check when the bird fall on the screen
            if (bird_y < SCREEN_HEIGHT - birds[0].getHeight() || velocity < 0) {
                velocity += gravity; // when the bird fall, it drop faster
                bird_y += velocity;
            }

            //tube
            for (int i = 0; i< numberOfTube; i++)
            {
                tubeX[i] -= tubeVelocity;
                if (tubeX[i] < -topTube.getWidth())
                {
                    tubeX[i] += numberOfTube * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                }
                canvas.drawBitmap(topTube, tubeX[i], topTubeY[i] - topTube.getHeight(), null );
                canvas.drawBitmap(bottomTube, tubeX[i], topTubeY[i] + gap, null );
            }

        }
        //display bird at the center of the screen
        canvas.drawBitmap(birds[frame], bird_x, bird_y, null );
        handler.postDelayed(runnable, UPDATE_MILLISECONDS);

    }

    //get touch event

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) // that is tap is dedicated on screen
        {
            velocity = -30; // tap to jump
            gameState = true;
            Toast.makeText(getContext(), String.valueOf(birds[frame].getScaledHeight(bird_y)), Toast.LENGTH_SHORT).show();

        }
        return true;

    }

    public boolean checkCollision()
    {
        return true;
    }
}
