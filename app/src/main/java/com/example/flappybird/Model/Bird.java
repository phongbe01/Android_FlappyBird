package com.example.flappybird.Model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flappybird.R;

public class Bird {
    private Bitmap bird;
    private int x_right, y_right; // check collision
    public  Bird(Resources resources, int image) {
        this.bird =  BitmapFactory.decodeResource(resources, image);
    }

    public int getWidth()
    {
        return this.bird.getWidth();
    }

    public int getHeight()
    {
        return this.bird.getHeight();
    }

    public Bitmap getBird() {
        return bird;
    }

    public int getX_val(int screen_width) {
        return screen_width/2 - getWidth()/2;
    }

    public int getY_val(int screen_heigth) {
        return screen_heigth/2 - getHeight()/2;
    }

}
