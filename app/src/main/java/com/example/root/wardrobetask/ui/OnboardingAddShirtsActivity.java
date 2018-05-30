package com.example.root.wardrobetask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.root.wardrobetask.R;
import com.example.root.wardrobetask.pojo.WardrobeItem;
import com.example.root.wardrobetask.util.GridRecycleAdapter;
import com.example.root.wardrobetask.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class OnboardingAddShirtsActivity extends AppCompatActivity
{
    private static String TAG = "OnboardingAddShirts";
    private Realm realm;

    @BindView(R.id.shirts_recyclerview)
    RecyclerView shirtsRecyclerview;

    @BindView(R.id.preference_shirts_question_container)
    LinearLayout preferenceContainer;

    GridRecycleAdapter shirtsGridAdapter;
    ArrayList<String> shirtsPathList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_shirts);
        initUI();
    }

    private void initUI()
    {
        ButterKnife.bind(this);


        realm = Realm.getDefaultInstance();
        shirtsPathList = new ArrayList<>();

        shirtsRecyclerview.setLayoutManager(new GridLayoutManager
                (this, getResources().getInteger(R.integer.grid_column_count)));
        shirtsGridAdapter = new GridRecycleAdapter(this, shirtsPathList);
        shirtsRecyclerview.setAdapter(shirtsGridAdapter);

        shirtsRecyclerview.setDrawingCacheEnabled(true);
        shirtsRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        realm.close();
    }

    @OnClick(R.id.onboarding_shirts_next_btn)
    void startPantsActivity()
    {
        startActivity(new Intent(OnboardingAddShirtsActivity.this, OnboardingAddPantsActivity.class));
        finish();
    }

    @OnClick(R.id.onboarding_gender_positive_btn)
    void setPreferenceForTops()
    {
        Util.setGenderPreferenceForShirts(OnboardingAddShirtsActivity.this, false);
        Toast.makeText(OnboardingAddShirtsActivity.this, "Preference saved!", Toast.LENGTH_SHORT).show();
        preferenceContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.onboarding_gender_negative_btn)
    void setPreferenceForShirts()
    {
        Util.setGenderPreferenceForShirts(OnboardingAddShirtsActivity.this, true);
        Toast.makeText(OnboardingAddShirtsActivity.this, "Preference saved!", Toast.LENGTH_SHORT).show();
        preferenceContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.add_shirts_fab)
    void selectImagesForShirts()
    {
        EasyImage.clearConfiguration(this);
        EasyImage.configuration(this).saveInRootPicturesDirectory();
        EasyImage.openChooserWithGallery(this, "Pick image", 1);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback()
        {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type)
            {
                Log.e("Wardrobe", e.toString());
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type)
            {
                imagesFiles.size();
                addToDatabase(imagesFiles.get(0).getAbsolutePath());

            }
        });
    }


    private void addToDatabase(final String filePath)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {

                Log.d(TAG, filePath);
                WardrobeItem wardrobeItem = realm.createObject(WardrobeItem.class, UUID.randomUUID().toString());
                wardrobeItem.setImagePath(filePath);
                wardrobeItem.setClothType(Util.SHIRT);
            }
        });

        RealmResults<WardrobeItem> wardrobeItems = realm.where(WardrobeItem.class)
                .contains("clothType", Util.SHIRT)
                .findAll();

        shirtsPathList.clear();

        for (WardrobeItem item : wardrobeItems)
        {
            shirtsPathList.add(item.getImagePath());
        }

        shirtsGridAdapter.notifyDataSetChanged();
    }



}
