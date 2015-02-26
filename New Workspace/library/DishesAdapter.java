package com.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.svaad.R;


/**
 * Created by Svaad on 19-09-2014.
 */
public class DishesAdapter extends BaseAdapter {
    private Context ctx;
    private static String[] items={"Anime","Sports","Politics","Love","Jokes","Sarcastic","Confessions","Current affairs","Android","Recorder","Technology","trending","Auto Mobile","Fashion","LifeStyle","Gadgets"};

    public DishesAdapter(Context context) {
        ctx = context;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String[] getItem(int i) {
        return items;
    }

    @Override
    public long getItemId(int i) {
        return Long.getLong(items[i]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View)inflater.inflate(R.layout.dish_item_layout,null);
            mViewHolder = new ViewHolder();
            mViewHolder.dish_txt = (TextView)convertView.findViewById(R.id.tvListName);
            mViewHolder.dishImageView =(ImageView)convertView.findViewById(R.id.listDishImageView);
            mViewHolder.likes_txt = (TextView)convertView.findViewById(R.id.viewsTxt);
            mViewHolder.love_btn  = (Button)convertView.findViewById(R.id.loved_btn);
            mViewHolder.wish_btn  = (Button)convertView.findViewById(R.id.wish_btn);
           // mViewHolder.usrImg    = (ImageView)convertView.findViewById(R.id.prof_img);
            mViewHolder.usrName_txt = (TextView)convertView.findViewById(R.id.userTxt);
            mViewHolder.usrComment_txt = (TextView)convertView.findViewById(R.id.commentsTxt);
            mViewHolder.restaurant_txt = (TextView)convertView.findViewById(R.id.resName);
            mViewHolder.tag_txt     = (TextView)convertView.findViewById(R.id.tagTxt);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
         mViewHolder.dish_txt.setText(items[position]);
        mViewHolder.dishImageView.setImageResource(R.drawable.pic);
       // mViewHolder.usrImg.setImageResource(R.drawable.pic);
        return convertView;
    }

    public  class ViewHolder{
         ImageView dishImageView;
         TextView dish_txt;
         TextView restaurant_txt;
         TextView tag_txt;
         TextView usrName_txt;
         TextView usrComment_txt;
         TextView likes_txt;
         ImageView usrImg;
         Button wish_btn;
         Button love_btn;


    }
}
