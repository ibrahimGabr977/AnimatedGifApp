package com.hope.igb.catgif.screens.comman.customgridlayout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class CustomGridLayoutManager extends GridLayoutManager {

    // column count for orientation = HORIZONTAL
    private  int columnPerRow;
    // margins between items in order (left, top, right, bottom)
//    private int [] margins = new int[4];
    private final Context context;

    //recyclerWidth represent the width of recyclerview with respect to the screen width
    // 1.0 meaning recyclerview occupied 100% of screen width
    private double recyclerWidth = 1.0;

    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        this.context = context;
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.context = context;
    }






    /* Setting LayoutParams for the child views of the recycler view.*/
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return spanLayoutSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return spanLayoutSize(super.generateLayoutParams(c, attrs));
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return spanLayoutSize(super.generateLayoutParams(lp));
    }

    /* The function has been made for Horizontal Recycler View.
     *  1. It checks for the orientation to be HORIZONTAL.
     *  2. Its spancount has already been set to 2 in this case throught the constuctor at the initialisation time.
     *    i.e,  layoutManager = new AbsolutefitLayourManager(this,2,GridLayoutManager.HORIZONTAL,false);
     *    the spancount = 2 , specifies the no. of rows for HORIZONTAL orientation
     * 3. The rest of the function divides the horizontal screen width by 2 (spanColumnCount = 2 HERE) hence specyfing the column
          width of each view and hence specifying  2 columns (can be made 3, by dividing by three.)
     */
    private RecyclerView.LayoutParams spanLayoutSize(RecyclerView.LayoutParams layoutParams) {
        if (getOrientation() == HORIZONTAL) {

            layoutParams.width = (int) Math.round(getHorizontalSpace() * 1.0 / columnPerRow) - 10 ;
//            // its the margin between the items
//            layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
        }
//        else if(getOrientation() == VERTICAL){
//            layoutParams.height = (int) Math.round(getVerticalSpace() /  spanCount);
//        }
        return layoutParams;
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return super.checkLayoutParams(lp);
    }

    private double getHorizontalSpace() {
        return  (getWidth() * (recyclerWidth * 1.0));
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }


    public  void setColumnPerRow(int columnPerRow) {
        this.columnPerRow = columnPerRow;
    }



//    public void setMargins(int[] margins) {
//        this.margins = margins;
//    }


    public void setRecyclerWidth(double recyclerWidth) {
        this.recyclerWidth = recyclerWidth;
    }
}