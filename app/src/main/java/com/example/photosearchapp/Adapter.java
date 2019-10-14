package com.example.photosearchapp;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class Adapter extends BaseAdapter {
    Context context;
    ViewHolder holder;
    List<String> urls;

    public Adapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }
    /* @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);
        return new ReviewViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder viewHolder, int i) {
           viewHolder.bind_data(Category.categories.get(i));
    }

    @Override
    public int getItemCount() {
        return Category.categories.size();
    }*/

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false);
            holder = new ViewHolder(view, context);
            //holder.bind_data(Category.categories.get(i));
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        holder.bind_data(urls.get(i));
        return view;
    }
}

class ViewHolder extends RecyclerView.ViewHolder{

   ImageView Image;
   LinearLayout container;
   Context c;
    public ViewHolder(@NonNull View itemView, Context c) {
        super(itemView);
        this.c= c;
        assign_views(itemView);
    }
    void assign_views(View v){
        Image = v.findViewById(R.id.thumbcat);
        container = v.findViewById(R.id.container);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)c).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        container.getLayoutParams().width =(int) (width/3);


    }
    public  void bind_data(String url){
        Glide.with(c).load(url).apply(new RequestOptions().override(400)).into(Image);
    }
}