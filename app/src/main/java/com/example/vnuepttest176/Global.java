package com.example.vnuepttest176;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Global {
    public static void saveState(Activity activity,String key,String value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getState (Activity activity,String key){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public static class BookmarkAdapter extends BaseAdapter {

        Context mContext;
        String[] mSource;

        public BookmarkAdapter(Context context,String[] source){
            this.mContext = context;
            this.mSource = source;
        }
        @Override
        public int getCount() {
            return mSource.length;
        }

        @Override
        public Object getItem(int position) {
            return mSource[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;
            if(view == null){
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.bookmark_layout_item,viewGroup,false);
                viewHolder.textView = (TextView) view.findViewById(R.id.tvWord);
                viewHolder.btnDelete = (ImageView) view.findViewById(R.id.btnDelete);

                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.textView.setText(mSource[position]);
            return view;
        }

        class ViewHolder{
            TextView textView;
            ImageView btnDelete;
        }
    }
}
