package com.example.root.wardrobetask.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class OnboardingAddPantsActivity extends AppCompatActivity
{
    private static String TAG = "OnboardingAddShirts";
    private Realm realm;

    @BindView(R.id.pants_recyclerview)
    RecyclerView pantsRecyclerview;

    @BindView(R.id.preference_pants_question_container)
    LinearLayout preferenceContainer;

    GridRecycleAdapter pantsGridAdapter;
    ArrayList<String> pantsPathList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_pants);
        initUI();
    }

    private void initUI()
    {
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        pantsPathList = new ArrayList<>();

        pantsRecyclerview.setLayoutManager(new GridLayoutManager
                (this, getResources().getInteger(R.integer.grid_column_count)));
        pantsGridAdapter = new GridRecycleAdapter(this, pantsPathList);
        pantsRecyclerview.setAdapter(pantsGridAdapter);

        pantsRecyclerview.setDrawingCacheEnabled(true);
        pantsRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        realm.close();
    }

    @OnClick(R.id.onboarding_pants_next_btn)
    void startMainActivity()
    {
        startActivity(new Intent(OnboardingAddPantsActivity.this, MainActivity.class));
        finish();
    }


    @OnClick(R.id.onboarding_gender_positive_btn)
    void setPreferenceForBottoms()
    {
        Util.setGenderPreferenceForPants(OnboardingAddPantsActivity.this, false);
        Toast.makeText(OnboardingAddPantsActivity.this, "Preference saved!", Toast.LENGTH_SHORT).show();
        preferenceContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.onboarding_gender_negative_btn)
    void setPreferenceForPants()
    {
        Util.setGenderPreferenceForPants(OnboardingAddPantsActivity.this, true);
        Toast.makeText(OnboardingAddPantsActivity.this, "Preference saved!", Toast.LENGTH_SHORT).show();
        preferenceContainer.setVisibility(View.GONE);
    }


    @OnClick(R.id.add_pants_fab)
    void selectImagesForPants()
    {
        EasyImage.clearConfiguration(this);
        EasyImage.configuration(this).saveInRootPicturesDirectory();
        EasyImage.openChooserWithGallery(this, "Pick image", 1);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Wardrobe", "came back");

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
                wardrobeItem.setClothType(Util.PANT);
            }
        });

        RealmResults<WardrobeItem> wardrobeItems = realm.where(WardrobeItem.class)
                .contains("clothType", Util.PANT)
                .findAll();

        pantsPathList.clear();

        for (WardrobeItem item : wardrobeItems)
        {
            pantsPathList.add(item.getImagePath());
        }

        pantsGridAdapter.notifyDataSetChanged();
    }


}
