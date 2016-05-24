package com.licheng.app.rxandroidsample.common;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by licheng on 23/5/16.
 */
public abstract class OnRecyclerViewClickListener implements RecyclerView.OnItemTouchListener {


    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;


    public OnRecyclerViewClickListener(RecyclerView recyclerView) {
        this.mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child != null){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(viewHolder);
            }
            return true;
        }
    }

    protected abstract void onItemClick(RecyclerView.ViewHolder viewHolder);


}
