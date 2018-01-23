package com.easy.app.core.view.swiperefreshlayout;

/**
 * Created by oliviergoutay on 1/23/15.
 */
public enum SwipeRefreshLayoutDirection {

    TOP(0),
    BOTTOM(1),
    BOTH(2),
    NONE(3);//create by zhsf

    private int mValue;

    SwipeRefreshLayoutDirection(int value) {
        this.mValue = value;
    }

    public static SwipeRefreshLayoutDirection getFromInt(int value) {
        for (SwipeRefreshLayoutDirection direction : SwipeRefreshLayoutDirection.values()) {
            if (direction.mValue == value) {
                return direction;
            }
        }
        return BOTH;
    }

}
