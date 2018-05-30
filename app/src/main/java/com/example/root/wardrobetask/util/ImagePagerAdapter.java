package com.example.root.wardrobetask.util;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.root.wardrobetask.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class ImagePagerAdapter extends PagerAdapter
{
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> imagePathList;

    public ImagePagerAdapter(Context context, ArrayList<String> imagePathList)
    {
        this.context = context;
        this.imagePathList = imagePathList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {

        View itemView = layoutInflater.inflate(R.layout.fragment_screen_slide_image, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.wardrobe_imageview);

        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                Uri uri = Uri.fromFile(new File(imagePathList.get(position)));
                Picasso.with(context).load(uri).
                        placeholder(R.drawable.grid_item_image_placeholder)
                        .resize(imageView.getMeasuredWidth(), imageView.getMeasuredHeight()).centerCrop().into(imageView);

                return true;
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
