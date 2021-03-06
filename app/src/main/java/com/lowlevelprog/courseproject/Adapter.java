package com.lowlevelprog.courseproject;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Adapter extends BaseAdapter {
    private Context mContext;
    private ImageView imageView;
    private int[] grideArray;
    private int gwidth = PuzzleActivity.width;
    private int gheight = PuzzleActivity.height;
    private int gorientation = PuzzleActivity.orientation;

    public Adapter(Context context, int[] gridarray) {
        mContext = context;
        grideArray = gridarray;
    }

    @Override
    public int getCount() {
        return grideArray.length;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            imageView = new ImageView(mContext);
            if (gorientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageView.setLayoutParams(new GridView.LayoutParams(gheight, gheight));
            } else {
                imageView.setLayoutParams(new GridView.LayoutParams(gwidth, gwidth));
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(3, 3, 3, 3);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(grideArray[position]);
        return imageView;
    }
}
