package com.fuxi.selfcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.ArrayList;

public class HomeData {

    private Context mContext;
    private String[] mTextArray =
            {"明日之子", "TA-TG", "疯狂节", "热气球", "纯真年代", "客人", "晚霞", "辽宁舰", "卡片", "南锣鼓巷"};
    private int[] mImgArray =
            {R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three, R.drawable.ic_four,
                    R.drawable.ic_five, R.drawable.ic_six, R.drawable.ic_seven, R.drawable.ic_eight,
                    R.drawable.ic_nine, R.drawable.ic_ten};
    private ArrayList<DataInfo> mDataInfoList = new ArrayList<>();

    private static class InstanceHolder {
        public static HomeData sInstance = new HomeData();
    }

    private HomeData() {
    }

    public static HomeData getInstance(Context context) {
        if (InstanceHolder.sInstance.mContext == null) {
            if (context == null) {
                return null;
            }
            InstanceHolder.sInstance.mContext = context;
        }
        return InstanceHolder.sInstance;
    }

    public void initData() {
        for (int i = 0;i < mImgArray.length;i++) {
            DataInfo dataInfo = new DataInfo(mTextArray[i],getDefaultBitmap(mContext, i), false);
            mDataInfoList.add(dataInfo);
        }
    }

    public ArrayList<DataInfo> getDataList() {
        return mDataInfoList;
    }

    public DataInfo getDataInfo(int index) {
        if (mDataInfoList != null) {
            return mDataInfoList.get(index);
        }
        return null;
    }

    public void deleteIndexData(int index) {
        if (mDataInfoList != null && !mDataInfoList.get(index).isLocked()) {
            mDataInfoList.remove(index);
        }
    }

    public void deleteData(DataInfo dataInfo) {
        if (mDataInfoList != null) {
            mDataInfoList.remove(dataInfo);
        }
    }

    class DataInfo {
        private String title;
        private Bitmap bmp;
        private boolean locked;

        public DataInfo (String text, Bitmap bitmap, boolean isLocked) {
            title = text;
            bmp = bitmap;
            locked = isLocked;
        }

        public String getTitle() {
            return title;
        }

        public Bitmap getBmp() {
            return bmp;
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean isLock) {
            locked = isLock;
        }
    }

    private Bitmap getDefaultBitmap(Context context, int resId) {
        InputStream is = context.getResources().openRawResource(mImgArray[resId]);
        return BitmapFactory.decodeStream(is);
    }
}
