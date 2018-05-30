package com.example.root.wardrobetask.util;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.root.wardrobetask.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pwadh on 27-11-2016.
 */

public class FavoriteRecycleAdapter extends RecyclerView.Adapter<FavoriteRecycleAdapter.ViewHolder>
{
    Context context;
    ArrayList<String> favoriteShirtsImageList;
    ArrayList<String> favoritepantsImageList;

    public FavoriteRecycleAdapter(Context context, ArrayList<String> favoriteShirtsImageList
                                                        , ArrayList<String> favoritepantsImageList)
    {
        this.context =context;
        this.favoriteShirtsImageList = favoriteShirtsImageList;
        this.favoritepantsImageList = favoritepantsImageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_grid_item, parent, false);
        return new FavoriteRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        try
        {
            Uri shirtUri = Uri.fromFile(new File(favoriteShirtsImageList.get(position)));
            Picasso.with(context).load(shirtUri).fit().into(holder.gridShirtImage);

            Uri panttUri = Uri.fromFile(new File(favoritepantsImageList.get(position)));
            Picasso.with(context).load(panttUri).fit().into(holder.gridPantImage);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return favoriteShirtsImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.grid_item_container)
        CardView gridItemContainer;
        @BindView(R.id.favorite_shirt_image)
        ImageView gridShirtImage;
        @BindView((R.id.favorite_pant_image))
        ImageView gridPantImage;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
