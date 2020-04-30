package com.example.flappybird.Model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonFunction {
    public Bitmap loadImage(Resources resources, int file_path)
    {
       return BitmapFactory.decodeResource(resources, file_path);
    }


}
