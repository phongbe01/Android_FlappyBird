package com.example.flappybird.Model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class Tube {
    private Bitmap topTube, bottomTube;
    public static int gap = 400;  //distance between top tube and bottom tube
    private int  maxTubeOffset, tubeX, topTubeY;

    public Tube(Resources resources, int top, int bottom) {
        this.topTube = BitmapFactory.decodeResource(resources, top);
        this.bottomTube = BitmapFactory.decodeResource(resources, bottom);
    }


    public int getMinTubeOffset()
    {
        return gap/2;
    }

    //max height tube
    public void setMaxTubeOffset(int screen_heigth)
    {
        this.maxTubeOffset = screen_heigth - getMinTubeOffset() - gap;
    }

    public int getMaxTubeOffset()
    {
        return this.maxTubeOffset;
    }

    // tubeX
    public void setTubeX(int screen_width)
    {
        this.tubeX = screen_width;
    }

    public int getTubeX() {
        return tubeX;

    }

    //top tubeY

    public void setTopTubeY(Random random)
    {
        this.topTubeY = getMinTubeOffset() + random.nextInt(getMaxTubeOffset()- getMinTubeOffset() + 1);
    }

    public int getTopTubeY() {
        return topTubeY;
    }

    public Bitmap getTopTube() {
        return topTube;
    }

    public Bitmap getBottomTube() {
        return bottomTube;
    }

    public void move(int speed)
    {
        this.tubeX -= speed;
    }

    public void makeRandomValue(int number, int distance)
    {
        this.tubeX += number * distance;
    }

    public void drawTopTube(Canvas canvas)
    {
        canvas.drawBitmap(this.topTube, this.tubeX, this.topTubeY - this.topTube.getHeight(), null);
    }

    public void drawBottomTube(Canvas canvas)
    {
        canvas.drawBitmap(this.bottomTube, this.tubeX, this.topTubeY + gap, null);
    }
}
