package com.fuxi.selfcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private Button mButton;
    private TextView mTextView;
    private HomeAdapter mHomeAdapter;
    private HomeData mHomeData;
    private ItemTouchHelper mItemTouchHelper;
    private boolean isLineLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelativeLayout = findViewById(R.id.main_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mButton = findViewById(R.id.btn_clear_all);
        mTextView = findViewById(R.id.display_nothing);
        mHomeData = HomeData.getInstance(this);
        if (mHomeData != null) {
            mHomeData.initData();
        }
        if (isLineLayout) {
            MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(20, 0));//420
        } else {
            MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(this, 2,
                    RecyclerView.HORIZONTAL, true);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(30, 0));
        }
        mHomeAdapter = new HomeAdapter(this, mHomeData);
        mRecyclerView.setAdapter(mHomeAdapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(mHomeAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mButton.setOnClickListener(this);
        mRelativeLayout.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mHomeAdapter != null) {
            mHomeAdapter.onDeleteAllClick();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (v.getId() != R.id.btn_clear_all && v.getId() != R.id.recycler_view) {
                finish();
            }
        }
        return false;
    }

    public interface DeleteAllItemCallback {
        void onDeleteAllClick();
    }

    public void showEmptyView() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }
}
