package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.ContextMenu;
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
    int BIRD_FRAME_0 = R.drawable.bee_up_40;
    int BIRD_FRAME_1 = R.drawable.bee_down_40;
    int BACKGROUND = R.drawable.background;
    int TOP_TUBE = R.drawable.top_tube;
    int BOTTOM_TUBE = R.drawable.bottom_tube;
    int velocity = 0, gravity = 4;
    int numberOfTube = 6;
    int tubeVelocity = 15; //tube speed
    int frame = 0;

    // check bird position
    ArrayList<Tube> arr_tubes;
    Bird bird;
    Bird[] birds = new Bird[2];
    Tube[] tubes = new Tube[numberOfTube];
    boolean gameState = false;
    int distanceBetweenTubes;
    Random random;
    int score = 0;
    int topTubeHeight = 0;
    boolean is_col, is_score;

    private GameStart gameStart;

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
//        Tube tube = new Tube(getResources(), TOP_TUBE,BOTTOM_TUBE);
        distanceBetweenTubes = SCREEN_WIDTH * 3/4;
//        arr_tubes = new ArrayList<Tube>();
        random = new Random();
//        for (int i =0; i< numberOfTube;i++)
//        {
//            tube.setMaxTubeOffset(SCREEN_HEIGHT);
//            tube.setTubeX(SCREEN_WIDTH + i*distanceBetweenTubes); //position tube appear
//            tube.setTopTubeY(random); //topTubeY will vary between minTube and maxTube offset
//            arr_tubes.add(tube);
//
//        }
//        Tube tube = new Tube(getResources(), TOP_TUBE, BOTTOM_TUBE);
        for (int i = 0; i < numberOfTube; i++)
        {
            Tube tube = new Tube(getResources(), TOP_TUBE, BOTTOM_TUBE);
            tube.setMaxTubeOffset(SCREEN_HEIGHT);
            tube.setTubeX(SCREEN_WIDTH + i * distanceBetweenTubes);
            tube.setTopTubeY(random);
            tubes[i] = tube;
        }

        //score
        paint = new Paint();
        paint.setTextSize(120);
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
            if (frame == 0)
            {
                frame = 1;
                bird.changeBirdImg(getResources(), BIRD_FRAME_1);
            }
            else {
                frame = 0;
                bird.changeBirdImg(getResources(), BIRD_FRAME_0);
            }
            // check when the bird fall on the screen
            if (bird.getBird_y() < SCREEN_HEIGHT -bird.getBird().getHeight() || velocity < 0) {
                velocity += gravity; // when the bird fall, it drop faster
               bird.down(velocity);
            }

            //tube
            for (int i = 0; i< numberOfTube; i++)
            {
//                Tube tube = arr_tubes.get(i);
//                tube.move(tubeVelocity);
//                if (tube.getTubeX() < -tube.getTopTube().getWidth())
//                {
//                    tube.makeRandomValue(numberOfTube, distanceBetweenTubes);
//                    tube.setTopTubeY(random);
//
//                }
//                score = bird.getBird_y();
//                is_col = checkCollision(bird, tube);
//
//                tube.drawTopTube(canvas);
//                tube.drawBottomTube(canvas);

                tubes[i].move(tubeVelocity);
                if (tubes[i].getTubeX() < -tubes[i].getTopTube().getWidth())
                {
                    tubes[i].makeRandomValue(numberOfTube, distanceBetweenTubes);
                    tubes[i].setTopTubeY(random);
                }
                score = bird.getBird_y();
                is_col = checkCollision(bird, tubes[i]);
                tubes[i].drawTopTube(canvas);
                tubes[i].drawBottomTube(canvas);

                if (is_col) {
                    tubeVelocity = 0;
                }
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

            if (is_col) {
                Toast.makeText(getContext(), String.valueOf(is_col), Toast.LENGTH_SHORT).show();
                getContext().startActivity(new Intent(getContext(), GameStart.class));
            }
            Toast.makeText(getContext(), String.valueOf(is_col), Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    public boolean checkCollision(Bird bird, Tube tube)
    {
        int left_a =  bird.getBird_x();
        int right_a = bird.getBird_w();
        int top_a = bird.getBird_y();
        int bottom_a = top_a + bird.getBird().getHeight();

        int left_b = tube.getTubeX();
        int right_b = tube.getTubeX() + tube.getTopTube().getWidth();
        int top_b = tube.getTopTubeY();
        int bottom_b = top_b + 400;

        if (left_a > left_b && right_a < right_b)
        {
            if (top_a < top_b || bottom_a > bottom_b)
            {
                return true;
            }
        }

        if (left_a < right_b && right_a > right_b)
        {
            if (top_a < top_b || bottom_a > bottom_b)
            {
                return true;
            }
        }
        if (left_a < left_b && right_a > left_b)
        {
            if (top_a < top_b || bottom_a > bottom_b)
            {
                return true;
            }
        }



        return false;
    }

    public boolean checkScore(Bird bird, Tube tube) {
        int left_a = bird.getBird_x();
        int right_a = bird.getBird_w();
        int top_a = bird.getBird_y();
        int bottom_a = top_a + bird.getBird().getHeight();

        int left_b = tube.getTubeX();
        int right_b = tube.getTubeX() + tube.getTopTube().getWidth();
        int top_b = tube.getTopTubeY();
        int bottom_b = top_b + 400;


        return true;
    }

    public void end(Bird bird, Tube tube)
    {
        boolean is_col = checkCollision(bird, tube);
        if (!is_col)
        {
            velocity = SCREEN_HEIGHT;
            tubeVelocity = 0;
        }
    }


}
