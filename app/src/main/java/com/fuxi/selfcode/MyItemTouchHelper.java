package com.fuxi.selfcode;

import android.graphics.Canvas;
import android.graphics.Color;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemTouchHelper extends ItemTouchHelper.Callback {

    private ItemTouchMoveListener moveListener;

    public MyItemTouchHelper(ItemTouchMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    /**
     * Called first when Callback, it is used to determine action and direction for current
     * function: drag and swipe action
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //direction：up，down，left，right
        //constants
        // ItemTouchHelper.UP    0x0001
        // ItemTouchHelper.DOWN  0x0010
        // ItemTouchHelper.LEFT  0x0100
        // ItemTouchHelper.RIGHT 0x1000

        //listen for direction of drag
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        //listen for direction of swipe
        int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * whether to enable long press drag
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //callback when move
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcHolder,
            RecyclerView.ViewHolder targetHolder) {
        // call adapter.notifyItemMoved(from,to) continuously when drag process
        if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
            return false;
        }
        // call adapter.notifyItemMoved(from,to) continuously when drag process,
        // callback onItemMove to implements class
        return moveListener.onItemMove(srcHolder.getAdapterPosition(),
                targetHolder.getAdapterPosition());
    }

    //callback when swipe
    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
        //listen for swipe up:1.delete data;2.call adapter.notifyItemRemove(position);
        moveListener.onItemRemove(holder.getAdapterPosition());
    }

    //set background for swiped item
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //judge state for selected
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(
                    viewHolder.itemView.getContext().getResources().getColor(
                            R.color.colorTransparent));
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //clear background for swiped item
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // recovery
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        //in case to not display for item : way 1
        viewHolder.itemView.setAlpha(1);//1-0
        //set size of item when swiped out
        viewHolder.itemView.setScaleX(1);
        viewHolder.itemView.setScaleY(1);
        super.clearView(recyclerView, viewHolder);
    }

    //set background transparent for swiped item
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //dX:delta of move horizontally(-：left；+：right) 0-view.getWidth()
        float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //animation for transparent
            viewHolder.itemView.setAlpha(alpha);//1-0
            //set size of swiped out
            viewHolder.itemView.setScaleX(1);
            viewHolder.itemView.setScaleY(1);
        }
        //in case to not display for item : way 2
        /*if (alpha == 0) {
            viewHolder.itemView.setAlpha(1);//1-0
            //set size of swiped out
            viewHolder.itemView.setScaleX(1);
            viewHolder.itemView.setScaleY(1);
        }*/
        //handle setTranslationX automatically in super method
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
