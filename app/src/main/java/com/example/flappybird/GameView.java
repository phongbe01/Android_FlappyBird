package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontStyle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.flappybird.Model.BackGround;
import com.example.flappybird.Model.Bird;
import com.example.flappybird.Model.Tube;

import java.util.ArrayList;
import java.util.Random;


public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final  int UPDATE_MILLISECONDS = 30;
    Bitmap background;
    Display display;
    Point point;
    int SCREEN_WIDTH, SCREEN_HEIGHT;
    Rect rect;
    Paint paint;

    //defined
    int BIRD_FRAME_0 = R.drawable.flappy_bird1_70x70;
    int BIRD_FRAME_1 = R.drawable.flappy_bird2_70x70;
    int BACKGROUND = R.drawable.background;
    int TOP_TUBE = R.drawable.top_tube;
    int BOTTOM_TUBE = R.drawable.bottom_tube;
    int velocity = 0, gravity = 3;
    int numberOfTube = 3;
    int tubeVelocity = 4; //tube speed

    // check bird position
    ArrayList<Tube> arr_tubes;
    Bird bird;
    boolean gameState = false;
    int distanceBetweenTubes;
    Random random;
    int score = 0;

    public GameView(Context context) {
        super(context);
        handler = new android.os.Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); // call onDraw()
            }
        };

        BackGround bg = new BackGround();
        background = bg.loadImage(getResources(),BACKGROUND);

        //create background
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        rect = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //create bird
        bird = new Bird(getResources(), BIRD_FRAME_0);
        bird.setBird_x(SCREEN_WIDTH); // x =156
        bird.setBird_y(SCREEN_HEIGHT);

        //create tubes
        Tube tube = new Tube(getResources(), TOP_TUBE,BOTTOM_TUBE);
        distanceBetweenTubes = SCREEN_WIDTH * 2/5;
        arr_tubes = new ArrayList<Tube>();
        random = new Random();
        for (int i =0; i< numberOfTube;i++)
        {
            tube.setMaxTubeOffset(SCREEN_HEIGHT);
            tube.setTubeX(SCREEN_WIDTH + distanceBetweenTubes); //position tube appear
            tube.setTopTubeY(random); //topTubeY will vary between minTube and maxTube offset
            arr_tubes.add(tube);
        }

        //score
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        Typeface typeface = paint.getTypeface();
        Typeface bold = Typeface.create(typeface, Typeface.BOLD);
        paint.setTypeface(bold);
    }


    // Draw view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw the background
        canvas.drawBitmap(background, null, rect, null);
        if (gameState) {
            // check when the bird fall on the screen
            if (bird.getBird_y() < SCREEN_HEIGHT -bird.getBird().getHeight() || velocity < 0) {
                velocity += gravity; // when the bird fall, it drop faster
               bird.down(velocity);
            }

            //tube
            for (int i = 0; i< numberOfTube; i++)
            {
                Tube tube = arr_tubes.get(i);
                tube.move(tubeVelocity);
                if (tube.getTubeX() < -tube.getTopTube().getWidth())
                {
                    tube.makeRandomValue(numberOfTube, distanceBetweenTubes);
                    tube.setTopTubeY(random);

                }
                if (checkCollision(bird, tube))
                {
                    score ++;
                }
                tube.drawTopTube(canvas);
                tube.drawBottomTube(canvas);
            }

        }
        //display bird at the center of the screen
        bird.draw(canvas);
        //score
        canvas.drawText(score + "", SCREEN_WIDTH/2, 200, paint);
        handler.postDelayed(runnable, UPDATE_MILLISECONDS);
    }

    //get touch event

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) // that is tap is dedicated on screen
        {
            velocity = -30; // tap to jump
            bird.up(velocity);
            gameState = true;
        }
        return true;
    }

    public boolean checkCollision(Bird bird, Tube tube)
    {
        if (bird.getBird_w() == tube.getTubeX() + tube.getTubeX()/2)
        {
            return true;
        } else {
            return true;
        }
    }
}
