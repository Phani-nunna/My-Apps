package com.example.multuplelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by rajan on 24-08-2014.
 */
public class HomefeedAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items;
    int[] icons;
    int[] type;

    public HomefeedAdapter(Context context,String[] items,int[] icons,int[] type) {
        super(context, R.layout.listitem,items);
        this.context=context;
        this.icons=icons;
        this.items=items;
        this.type=type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // The convertView argument is essentially a "ScrapView" as described is Lucas post
        // http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
        // It will have a non-null value when ListView is asking you recycle the row layout.
        // So, when convertView is not null, you should simply update its contents instead of inflating a new row    layout.

        if(convertView==null){

            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item);
            viewHolder.image= (ImageView) convertView.findViewById(R.id.dish_pic);
            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(items[position]);
        viewHolder.image.setImageResource(icons[0]);

        return convertView;
    }

    public static class ViewHolder
    {
        TextView name;
        ImageView image;
    }



}
