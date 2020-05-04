package com.example.flappybird.Model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Bird {

    private Bitmap bird;
    private int bird_x, bird_y, bird_h;
    private int velocity =0, gravity =3;

    public Bird(Resources resources, int file_path) {
        this.bird = BitmapFactory.decodeResource(resources, file_path);
    }

    public void setBird_x(int screen_width)
    {
        this.bird_x = screen_width/2 - this.bird.getWidth();
    }

    public void setBird_y(int screen_heigth)
    {
        this.bird_y = screen_heigth/2 - this.bird.getHeight();
    }

    public int getBird_w()
    {
       return getBird_x() + this.bird.getWidth();
    }

    public int getBird_x() {
        return bird_x;
    }

    public int getBird_y() {
        return bird_y;
    }


    public Bitmap getBird() {
        return bird;
    }
    public void changeBirdImg(Resources resources, int file_path)
    {
        this.bird = BitmapFactory.decodeResource(resources, file_path);
    }

    public void down(int speed)
    {
        this.bird_y += speed;
    }

    public void up(int speed)
    {
        this.bird_y =  this.bird_y + speed;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(this.bird, this.bird_x, this.bird_y, null);
    }

}
