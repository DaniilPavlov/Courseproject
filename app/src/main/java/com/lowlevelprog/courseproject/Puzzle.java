package com.lowlevelprog.courseproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Random;

public class Puzzle extends AppCompatActivity {

    int a = 0;
    private int counter, firstClick, secondClick, rememberData;
    public static int width;
    public static int index = 1;
    private static Random random = new Random();

    private int[] level1;
    private int[] newImageArray;
    int[] randomImageArray;

    public static void swap(int[] array, int firstInd, int secondInd) {
        int temporary = array[firstInd];
        array[firstInd] = array[secondInd];
        array[secondInd] = temporary;
    }

    public static int[] shake(int[] array) {
        int counter = array.length - 1;
        while (counter > 0) {
            int index = random.nextInt(counter + 1);
            swap(array, index, counter);
            --counter;
        }
        return array;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("key1", level1);
        outState.putIntArray("key2", newImageArray);
        outState.putIntArray("key3", randomImageArray);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            level1 = savedInstanceState.getIntArray("key1");
            newImageArray = savedInstanceState.getIntArray("key2");
            randomImageArray = savedInstanceState.getIntArray("key3");
            final GridView gridView = (GridView) findViewById(R.id.gridView);
            if (index < 4) gridView.setNumColumns(3);
            else gridView.setNumColumns(4);
            gridView.setAdapter(new Adapter(this, randomImageArray));
        }
    }

    boolean soundIsOff;

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // music
        soundIsOff = Home.soundIsOff;
        if (soundIsOff) {
            doUnbindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
        } else {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        Bundle bundle = getIntent().getExtras();
        load(bundle.getString("strName"));
        index = Integer.parseInt(bundle.getString("strName"));
        if (index < 4) {
            newImageArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < 9; i++) newImageArray[i] = level1[i];
        } else {
            newImageArray = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < 16; i++) newImageArray[i] = level1[i];
        }
        randomImageArray = shake(level1);

        DisplayMetrics dmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dmetrics);
        if (index < 4) width = dmetrics.widthPixels / 3;
        else width = dmetrics.widthPixels / 4;

        final GridView gridView = (GridView) findViewById(R.id.gridView);
        if (index < 4) gridView.setNumColumns(3);
        else gridView.setNumColumns(4);
        gridView.setAdapter(new Adapter(this, randomImageArray));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                counter++;
                if (counter == 1) {
                    firstClick = position;
                    rememberData = randomImageArray[position];
                } else if (counter == 2) {
                    secondClick = position;
                    randomImageArray[firstClick] = randomImageArray[secondClick];
                    randomImageArray[secondClick] = rememberData;
                    gridView.invalidateViews();
                    counter = 0;
                }
                if (randomImageArray[0] == newImageArray[0] &&
                        randomImageArray[1] == newImageArray[1] &&
                        randomImageArray[2] == newImageArray[2] &&
                        randomImageArray[3] == newImageArray[3] &&
                        randomImageArray[4] == newImageArray[4] &&
                        randomImageArray[5] == newImageArray[5] &&
                        randomImageArray[6] == newImageArray[6] &&
                        randomImageArray[7] == newImageArray[7] &&
                        randomImageArray[8] == newImageArray[8]) {
                    if (index < 4) finish();
                    else if (randomImageArray[9] == newImageArray[9] &&
                            randomImageArray[10] == newImageArray[10] &&
                            randomImageArray[11] == newImageArray[11] &&
                            randomImageArray[12] == newImageArray[12] &&
                            randomImageArray[13] == newImageArray[13] &&
                            randomImageArray[14] == newImageArray[14] &&
                            randomImageArray[15] == newImageArray[15]) {
                        finish();
                    }
                }
            }
        });
    }

    public void load(String choice) {
        switch (Integer.parseInt(choice)) {
            case 1:
                level1 = new int[]{R.drawable.p1_1, R.drawable.p1_2,
                        R.drawable.p1_3, R.drawable.p1_4,
                        R.drawable.p1_5, R.drawable.p1_6,
                        R.drawable.p1_7, R.drawable.p1_8, R.drawable.p1_9};
                break;
            case 2:
                level1 = new int[]{R.drawable.p2_1, R.drawable.p2_2,
                        R.drawable.p2_3, R.drawable.p2_4,
                        R.drawable.p2_5, R.drawable.p2_6,
                        R.drawable.p2_7, R.drawable.p2_8, R.drawable.p2_9};
                break;
            case 3:
                level1 = new int[]{R.drawable.p3_1, R.drawable.p3_2,
                        R.drawable.p3_3, R.drawable.p3_4,
                        R.drawable.p3_5, R.drawable.p3_6,
                        R.drawable.p3_7, R.drawable.p3_8, R.drawable.p3_9};
                break;
            case 4:
                level1 = new int[]{R.drawable.p4_1, R.drawable.p4_2,
                        R.drawable.p4_3, R.drawable.p4_4,
                        R.drawable.p4_5, R.drawable.p4_6,
                        R.drawable.p4_7, R.drawable.p4_8, R.drawable.p4_9,
                        R.drawable.p4_10, R.drawable.p4_11, R.drawable.p4_12,
                        R.drawable.p4_13, R.drawable.p4_14, R.drawable.p4_15,
                        R.drawable.p4_16};
                break;
            case 5:
                level1 = new int[]{R.drawable.p5_1, R.drawable.p5_2,
                        R.drawable.p5_3, R.drawable.p5_4,
                        R.drawable.p5_5, R.drawable.p5_6,
                        R.drawable.p5_7, R.drawable.p5_8, R.drawable.p5_9,
                        R.drawable.p5_10, R.drawable.p5_11, R.drawable.p5_12,
                        R.drawable.p5_13, R.drawable.p5_14, R.drawable.p5_15,
                        R.drawable.p5_16};
                break;
            case 6:
                level1 = new int[]{R.drawable.p6_1, R.drawable.p6_2,
                        R.drawable.p6_3, R.drawable.p6_4,
                        R.drawable.p6_5, R.drawable.p6_6,
                        R.drawable.p6_7, R.drawable.p6_8, R.drawable.p6_9,
                        R.drawable.p6_10, R.drawable.p6_11, R.drawable.p6_12,
                        R.drawable.p6_13, R.drawable.p6_14, R.drawable.p6_15,
                        R.drawable.p6_16};
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mServ != null) {
            mServ.resumeMusic();
        }
    }
}
