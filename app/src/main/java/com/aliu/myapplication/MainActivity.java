package com.aliu.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aliu.myapplication.pathicon.DrawerArrowDrawable;
import com.aliu.myapplication.pathicon.DrawerMoreDrawable;

public class MainActivity extends AppCompatActivity {


    private Button bt;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        ImageView imageView = (ImageView) findViewById(R.id.iv);
        ImageView imageView2 = (ImageView) findViewById(R.id.iv2);
        SeekBar sb = (SeekBar) findViewById(R.id.sb);
        final DrawerArrowDrawable drawable = new DrawerArrowDrawable(MainActivity.this);
        final DrawerMoreDrawable drawable2 = new DrawerMoreDrawable(MainActivity.this);
        imageView.setImageDrawable(drawable);
        imageView2.setImageDrawable(drawable2);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float v = progress / 100.0f;
                drawable.setProgress(v); // animate
                drawable2.setProgress(v); // animate

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    public void big() {


    }

    public void small() {

    }

}
