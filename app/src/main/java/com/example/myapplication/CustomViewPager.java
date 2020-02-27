package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;



public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//取消滑动翻页
/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }*/
//取消点击翻页
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
    // TODO Auto-generated method stub  
    super.setCurrentItem(item, smoothScroll);
  }


    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }


}

