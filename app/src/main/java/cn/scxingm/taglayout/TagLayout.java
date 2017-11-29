package cn.scxingm.taglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scxingm on 2017/11/28.
 */

public class TagLayout extends ViewGroup {

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //warp_content
        int width = 0;
        int height = 0;

        // 行宽/高
        int lineWidth = 0;
        int lineHeight = 0;

        //得到内部子view个数
        int viewCount = getChildCount();
        for (int i = 0; i < viewCount; i ++){
            View child = getChildAt(i);//获取子view

            //测量子view宽/高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //获取子ViewLayoutParams（与generateLayoutParams方法对应）
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            //子view占据宽度
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            //子view占据高度
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            //行宽 + 子view宽度 > 屏幕宽度
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()){
                //得到最大宽度
                width = Math.max(width, lineWidth);
                //重新赋值行宽
                lineWidth = childWidth;
                //新行需要增加高度
                height += lineHeight;
                //新行高度
                lineHeight = childHeight;
            } else { // 不换行
                //叠加行宽
                lineWidth += childWidth;
                //得到当前行最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //最后一个子view
            if (i == viewCount-1){
                //得到最终宽度
                width = Math.max(lineHeight, height);
                //得到最终高度
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );
        //MeasureSpec.AT_MOST -> warp_content
        //MeasureSpec.EXACTLY -> xxxdp / match_parent

//        if (modeWidth == MeasureSpec.AT_MOST){//warp_content
//            setMeasuredDimension(width, height);
//        } else {
//            setMeasuredDimension(sizeWidth, sizeHeight);
//        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    //所有子view
    private List<List<View>> allViews = new ArrayList<>(); //所有<行<行每个>>
    // 每行高度
    private List<Integer> allHeights = new ArrayList<>();
    //子view位置
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        allViews.clear();
        allHeights.clear();

        //当前ViewGroup宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int viewCount = getChildCount();

        for (int ii = 0; ii < viewCount; ii ++){
            View child = getChildAt(ii);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHieght = child.getMeasuredHeight();

            // 需要换行
            if (childWidth + lineWidth + params.leftMargin + params.rightMargin > width - getPaddingLeft() - getPaddingRight()){
                //记录height
                allHeights.add(lineHeight);

                //记录当前行的views
                allViews.add(lineViews);

                //重置行宽/高
                lineWidth = 0;
                lineHeight = childHieght + params.topMargin + params.bottomMargin;

                //重置view集合
                lineViews = new ArrayList<>();
            }

            lineWidth += childWidth + params.leftMargin + params.rightMargin;

            lineHeight = Math.max(lineHeight, childHieght + params.topMargin + params.bottomMargin);

            lineViews.add(child);
        }

        //最后一行
        allHeights.add(lineHeight);
        allViews.add(lineViews);

        //设置子view位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = allViews.size();

        for (int ii = 0; ii < lineNum; ii ++){
            //当前行所有view
            lineViews = allViews.get(ii);
            lineHeight = allHeights.get(ii);

            for (int jj = 0; jj < lineViews.size(); jj ++){
                View child = lineViews.get(jj);
                //子view状态
                if (child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

                int childLeft = left + params.leftMargin;
                int childTop = top + params.topMargin;
                int childRight = childLeft + child.getMeasuredWidth();
                int childBottom = childTop + child.getMeasuredHeight();

                //为子view进行布局
                child.layout(childLeft, childTop, childRight, childBottom);

                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    /**
     * 与当前viewGroup对应的LayoutParams
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
