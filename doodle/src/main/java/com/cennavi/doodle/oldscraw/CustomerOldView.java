package com.cennavi.doodle.oldscraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.cennavi.doodle.R;

/**
 */

public class CustomerOldView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private ScrawlBoardView boardView;
    private ImageView iv_cancel;
    public CustomerOldView(Context context) {
        super(context);
    }

    public CustomerOldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.doodle_scraw_customer,this);
        boardView = findViewById(R.id.board_view1);
//        iv_cancel = findViewById(R.id.cancel);
//        iv_cancel.setOnClickListener(this);
    }

    public CustomerOldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    public void setCavansView(Bitmap bitmap) {
        boardView.setBackgroud(bitmap);
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.cancel) {
//            boardView.cancelPath();
//        }
    }
}
