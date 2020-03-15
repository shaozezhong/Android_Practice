package com.example.myapplication.channel;

/**
 * Created by szz on 2020/3/13.
 */

public interface IItemHelper {

    void itemMoved(int oldPosition, int newPosition);
    void itemDismiss(int position);
}
