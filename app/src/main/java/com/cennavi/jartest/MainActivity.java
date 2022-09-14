package com.cennavi.jartest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import com.cennavi.doodle.oldscraw.ScrawView;
import com.gyf.immersionbar.ImmersionBar;

public class MainActivity extends AppCompatActivity {
    private TextView tv_from;
    private TextView tv_ip;
    private ScrawView customer;
    private TextView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_main);
        customer = findViewById(R.id.customer);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.screenShot(MainActivity.this);
                Bitmap bitmapBase = Utils.getCurrentBitmap();
                customer.setVisibility(View.VISIBLE);
                customer.setCavansView(bitmapBase);
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}