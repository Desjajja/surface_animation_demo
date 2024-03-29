package com.cyq.ninegridview.nine_grid;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cyq.ninegridview.R;

import java.util.List;

/**
 * Create by 陈扬齐
 * Create on 2019-07-07
 * description:自定义九宫格ImageView,基于组合布局
 */
public class NineGridView extends FrameLayout {
    private RecyclerView recyclerView;
    private View placeholderView;
    private NineGridViewAdapter nineGridViewAdapter;
    private int itemGap = 4;//item之间的间隔,默认4dp 单位：dp
    private int roundSize = 10;//圆角大小,默认10dp 单位：dp
    private OnNineGridViewEvent onNineGridViewEvent;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);
        itemGap = array.getDimensionPixelSize(R.styleable.NineGridView_nine_grid_item_gap,
                DeviceUtils.dip2px(context, itemGap));
        roundSize = array.getDimensionPixelSize(R.styleable.NineGridView_nine_grid_round_size,
                DeviceUtils.dip2px(context, roundSize));
        array.recycle();
        Log.i("test", "0---------" + itemGap + "---" + roundSize);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.nine_grid_layout, this, true);
        recyclerView = findViewById(R.id.rv_nine_grid_list);
        placeholderView = findViewById(R.id.placeholder_view);
    }

    /**
     * 设置数据，初始化Recyclerview
     *
     * @param datas 图片地址列表
     */
    public void setData(List<String> datas) {
        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(getContext());
        //设置边距
        builder.dividerHorSize = itemGap;
        builder.dividerVerSize = itemGap;
        GridItemDecoration gridItemDecoration = new GridItemDecoration(builder);
        //防止重复设置addItemDecoration造成item间距变大
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(gridItemDecoration);
        }
        nineGridViewAdapter = new NineGridViewAdapter(getContext(), datas, roundSize);
        nineGridViewAdapter.setOnEventListener(new NineGridViewAdapter.OnEventListener() {
            @Override
            public void onClick(int position) {
                if (onNineGridViewEvent != null) {
                    onNineGridViewEvent.onClick(position);
                }
            }

            @Override
            public void onLongClick(int position) {
                if (onNineGridViewEvent != null) {
                    onNineGridViewEvent.onLongClick(position);
                }
            }
        });
        if (datas.size() == 1) {
            placeholderView.setVisibility(GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (datas.size() == 2 || datas.size() == 4) {
            placeholderView.setVisibility(VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else if (datas.size() <= 9) {
            placeholderView.setVisibility(GONE);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        recyclerView.setAdapter(nineGridViewAdapter);
    }

    /**
     * 设置事件监听
     *
     * @param onNineGridViewEvent
     */
    public void setOnNineGridViewEvent(OnNineGridViewEvent onNineGridViewEvent) {
        this.onNineGridViewEvent = onNineGridViewEvent;
    }

    /**
     * 定义事件监听的回调
     */
    public interface OnNineGridViewEvent {
        void onClick(int position);

        void onLongClick(int position);
    }
}
