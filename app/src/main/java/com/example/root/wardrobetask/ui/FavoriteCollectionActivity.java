package com.example.root.wardrobetask.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.root.wardrobetask.R;
import com.example.root.wardrobetask.pojo.FavoriteCollection;
import com.example.root.wardrobetask.util.FavoriteRecycleAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteCollectionActivity extends AppCompatActivity
{

    @BindView(R.id.favorite_recycleview)
    RecyclerView favoriteRecycleview;

    ArrayList<String> favoriteShirtsList;
    ArrayList<String> favoritePantsList;

    private Realm realm;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    FavoriteRecycleAdapter favoriteRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_collection);
        initUI();
    }

    private void initUI()
    {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.favorites));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        realm = Realm.getDefaultInstance();

        favoriteShirtsList = new ArrayList<>();
        favoritePantsList = new ArrayList<>();
        getFavoriteCollectionFromDatabase();

        favoriteRecycleview.setLayoutManager(new GridLayoutManager
                (this, getResources().getInteger(R.integer.favorite_grid_column_count)));

        favoriteRecycleAdapter = new FavoriteRecycleAdapter(FavoriteCollectionActivity.this,
                                                                favoriteShirtsList, favoritePantsList);
        favoriteRecycleview.setAdapter(favoriteRecycleAdapter);

    }

    private void getFavoriteCollectionFromDatabase()
    {
        RealmResults<FavoriteCollection> favoriteItems = realm.where(FavoriteCollection.class)
                                                              .findAll();


        for (FavoriteCollection item : favoriteItems)
        {
            favoriteShirtsList.add(item.getShirtImagePath());
            favoritePantsList.add(item.getPantImagePath());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
