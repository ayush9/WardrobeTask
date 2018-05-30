package com.example.root.wardrobetask.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.root.wardrobetask.R;
import com.example.root.wardrobetask.ui.OnboardingAddShirtsActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pwadh on 26-11-2016.
 */

public class GridRecycleAdapter extends RecyclerView.Adapter<GridRecycleAdapter.ViewHolder>
{
    Context context;
    ArrayList<String> imagePathList;

    public GridRecycleAdapter(Context context, ArrayList<String> imagePathList)
    {
        this.context = context;
        this.imagePathList = imagePathList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        try
        {
            Uri uri = Uri.fromFile(new File(imagePathList.get(position)));
            Picasso.with(context).load(uri).fit().into(holder.gridItemImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        return imagePathList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.grid_item_container)
        CardView gridItemContainer;
        @BindView(R.id.grid_item_image)
        ImageView gridItemImage;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
