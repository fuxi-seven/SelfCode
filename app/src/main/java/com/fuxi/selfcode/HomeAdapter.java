package com.fuxi.selfcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> implements
        MainActivity.DeleteAllItemCallback, ItemTouchMoveListener{

    private static final String TAG = HomeAdapter.class.getSimpleName();
    private MainActivity mMainActivity;
    private HomeData mHomeData;
    private ArrayList<HomeData.DataInfo> mDataInfoList =  new ArrayList<>();

    public HomeAdapter(MainActivity mainActivity, HomeData homeData) {
        mMainActivity = mainActivity;
        mHomeData = homeData;
        mDataInfoList.addAll(homeData.getDataList());
    }

    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(mMainActivity).inflate(R.layout.layout_holder,parent, false);
        View v = LayoutInflater.from(mMainActivity).inflate(R.layout.layout_grid_holder, parent,
                false);
        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.HomeViewHolder holder, int position) {
        if (mDataInfoList != null) {
            HomeData.DataInfo dataInfo = mHomeData.getDataInfo(position);
            if (dataInfo != null) {
                holder.text.setText(dataInfo.getTitle());
                holder.img.setVisibility(dataInfo.isLocked() ? View.VISIBLE : View.INVISIBLE);
                holder.img1.setImageBitmap(dataInfo.getBmp());
                holder.img1.setTag(position);
                holder.img1.setOnLongClickListener(new LongClickListener());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataInfoList.size();
    }

    @Override
    public void onDeleteAllClick() {
        removeAllData();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemRemove(int position) {
        removeData(position);
        return false;
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView text;
        private ImageView img1;
        private HomeViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_lock);
            text = itemView.findViewById(R.id.txt);
            img1 = itemView.findViewById(R.id.img1);
        }
    }

    private class LongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            int position = (int)v.getTag();
            updateDataLocked(position);
            return false;
        }
    }

    private void updateDataLocked(int position) {
        HomeData.DataInfo dataInfo = mHomeData.getDataInfo(position);
        dataInfo.setLocked(!dataInfo.isLocked());
        // only one item changed, just call notifyItemChanged
        // has animation, if also call notifyDataSetChanged(), no animation
        notifyItemChanged(position);
    }

    private void removeData(int position) {
        notifyItemRemoved(position);
        mHomeData.deleteIndexData(position);
        updateDataList();
        //when remove data, the rest of data after this position should be updated
        notifyItemRangeChanged(position, mDataInfoList.size() + 1 - position);
        updateView();
    }

    private void removeAllData() {
        ArrayList<HomeData.DataInfo> tmpDataInfoList = new ArrayList<>(mDataInfoList);
        for (HomeData.DataInfo dataInfo : tmpDataInfoList) {
            if (!dataInfo.isLocked()) {
                mHomeData.deleteData(dataInfo);
            }
        }
        updateDataList();
        //update all items of adapter, so call notifyDataSetChanged();
        notifyDataSetChanged();
        updateView();
    }

    private void updateDataList() {
        mDataInfoList = mHomeData.getDataList();
    }

    private void updateView() {
        if (mDataInfoList.size() == 0){
            mMainActivity.showEmptyView();
        } else {
            //mMainActivity.setAncestralRecyclerCenterHor(mDataInfoList.size());
        }
    }
}
