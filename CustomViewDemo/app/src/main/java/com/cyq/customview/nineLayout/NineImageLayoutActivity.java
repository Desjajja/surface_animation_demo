package com.cyq.customview.nineLayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cyq.customview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NineImageLayoutActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private Random random;
    private final String URL_IMG = "http://q3x62hkt1.bkt.clouddn.com/banner/58f57dfa5bb73.jpg";
    private final String URL_IMG_2 = "http://q3x62hkt1.bkt.clouddn.com/timg.jpeg";
    private List<List<String>> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_image_layout);
        random = new Random();
        List<String> testList = new ArrayList<>();
        testList.add(URL_IMG_2);
        for (int i = 0; i < 100; i++) {
            int count = i % 9 + 1;
            List<String> list = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                list.add(URL_IMG);
            }
            if (i % 8 == 0) {
                mList.add(testList);
            }
            mList.add(list);
        }
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new MyAdapter(mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
}
