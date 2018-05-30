package com.example.root.wardrobetask.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.root.wardrobetask.R;
import com.example.root.wardrobetask.pojo.FavoriteCollection;
import com.example.root.wardrobetask.pojo.WardrobeItem;
import com.example.root.wardrobetask.util.AlarmReceiver;
import com.example.root.wardrobetask.util.ImagePagerAdapter;
import com.example.root.wardrobetask.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.shirts_viewpager)
    ViewPager shirtsViewpager;
    @BindView(R.id.pants_viewpager)
    ViewPager pantsViewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager_one_overflow)
    ImageView pagerOneOverflow;
    ImagePagerAdapter shirtsPagerAdapter, pantsPagerAdapter;
    ArrayList<String> shirtsImagePathList, pantsImagePathList;
    private Realm realm;
    private static String TAG = "ManActivity";
    private static String IS_FIRST_RUN_KEY = "IS_FIRST_RUN_KEY";
    boolean isFirstRun;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    final int REQUEST_PERMISSIONS = 123;

    private String clothType;
    int currentShirtPosition, currentPantPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        initUI();
        checkIfFirstRun();
    }


    // ------------------------------------------------ LISTENERS

    public void checkIfFirstRun()
    {
        if (sharedPreferences.getBoolean(IS_FIRST_RUN_KEY, true))
        {
            startActivity(new Intent(this, OnboardingWelcomeActivity.class));
            sharedPreferencesEditor.putBoolean(IS_FIRST_RUN_KEY, false).commit();
            finish();
        } else
        {
            generateRandomCombo(shirtsPagerAdapter, pantsPagerAdapter);
        }
    }

    private void initUI()
    {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        realm = Realm.getDefaultInstance();

        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent(this, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp
                && Util.getPreferenceForNotification(MainActivity.this))
        {
            Log.d(TAG, "Alarm is not active");
            setRecurringAlarm(this);
        }

        shirtsImagePathList = new ArrayList<>();
        getShirtImagesFromDatabase();
        shirtsPagerAdapter = new ImagePagerAdapter(MainActivity.this, shirtsImagePathList);
        shirtsViewpager.setAdapter(shirtsPagerAdapter);

        shirtsViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                currentShirtPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        pantsImagePathList = new ArrayList<>();
        getPantImagesFromDatabase();
        pantsPagerAdapter = new ImagePagerAdapter(MainActivity.this, pantsImagePathList);
        pantsViewpager.setAdapter(pantsPagerAdapter);

        pantsViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                currentPantPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


    }


    // Generating a random combo
    public void generateRandomCombo(ImagePagerAdapter shirtsPagerAdapter, ImagePagerAdapter pantsPagerAdapter)
    {
        if (shirtsPagerAdapter.getCount() > 0 && pantsPagerAdapter.getCount() > 0)
        {
            int min = 0;
            int shirtsMax = shirtsPagerAdapter.getCount() - 1;
            int pantsMax = pantsPagerAdapter.getCount() - 1;

            Random rShirt = new Random();
            int shirtNumber = rShirt.nextInt(shirtsMax - min + 1) + min;

            Random rPant = new Random();
            int pantNumber = rPant.nextInt(pantsMax - min + 1) + min;

            shirtsViewpager.setCurrentItem(shirtNumber, true);
            pantsViewpager.setCurrentItem(pantNumber, true);
        }
    }


    private void getShirtImagesFromDatabase()
    {
        RealmResults<WardrobeItem> wardrobeItems = realm.where(WardrobeItem.class)
                .contains("clothType", Util.SHIRT)
                .findAll();


        for (WardrobeItem item : wardrobeItems)
        {
            Log.wtf(TAG, item.getImagePath());
            shirtsImagePathList.add(item.getImagePath());
        }
    }

    private void getPantImagesFromDatabase()
    {
        RealmResults<WardrobeItem> wardrobeItems = realm.where(WardrobeItem.class)
                .contains("clothType", Util.PANT)
                .findAll();


        for (WardrobeItem item : wardrobeItems)
        {
            Log.wtf(TAG, item.getImagePath());
            pantsImagePathList.add(item.getImagePath());
        }
    }

    // ------------------------------------------------ LISTENERS

    @OnClick(R.id.pager_one_overflow)
    public void showPagerOneOverflow(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_delete_shirt, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (item.getItemId() == R.id.action_delete_shirt)
                {
                    // DELETE THE CURRENT SHIRT
                    if (shirtsImagePathList.size() > 0)
                    {
                        String filePath = shirtsImagePathList.get(currentShirtPosition);
                        deleteFromDatabase(filePath, Util.SHIRT);
                    }
                    return true;
                } else
                {
                    return false;
                }
            }
        });
    }

    @OnClick(R.id.pager_two_overflow)
    public void showPagerTwoOverflow(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_delete_pant, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (item.getItemId() == R.id.action_delete_pant)
                {
                    // DELETE THE CURRENT PANT
                    if (pantsImagePathList.size() > 0)
                    {
                        String filePath = pantsImagePathList.get(currentPantPosition);
                        deleteFromDatabase(filePath, Util.PANT);
                    }
                    return true;
                } else
                {
                    return false;
                }
            }
        });
    }

    @OnClick(R.id.favorite_fab)
    void addToFavoriteTable()
    {
        if (shirtsImagePathList.size() > 0 &&
                pantsImagePathList.size() > 0)
        {
            realm.executeTransaction(new Realm.Transaction()
            {
                @Override
                public void execute(Realm realm)
                {
                    String currentShirtPath = shirtsImagePathList.get(currentShirtPosition);
                    String currentPantPath = pantsImagePathList.get(currentPantPosition);

                    FavoriteCollection favoriteCollection = realm.createObject(FavoriteCollection.class, UUID.randomUUID().toString());
                    favoriteCollection.setShirtImagePath(currentShirtPath);
                    favoriteCollection.setPantImagePath(currentPantPath);
                }
            });
            int unicode = 0x1F601;
            Toast.makeText(MainActivity.this, getResources().getString(R.string.favorite_confirmation) + " " + getEmojiByUnicode(unicode), Toast.LENGTH_LONG).show();
        } else
        {
            int unicode = 0x1F610;
            Toast.makeText(MainActivity.this, getResources().getString(R.string.favorite_err) + " " + getEmojiByUnicode(unicode), Toast.LENGTH_LONG).show();
        }

    }

    @OnClick(R.id.random_fab)
    public void showRandomCombo()
    {
        generateRandomCombo((ImagePagerAdapter) shirtsViewpager.getAdapter(), (ImagePagerAdapter) pantsViewpager.getAdapter());
    }

    @OnClick(R.id.add_new_fab)
    void selectImagesForWardrobe()
    {
        String shirts = "Shirts";
        String pants = "Pants";

        if (!Util.getGenderPreferenceForShirts(MainActivity.this))
        {
            shirts = "Tops";
        }
        if (!Util.getGenderPreferenceForPants(MainActivity.this))
        {
            pants = "Bottoms";
        }

        final CharSequence[] items = {
                shirts, pants
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What you want to add?");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                // Do something with the selection
                if (item == 0)
                {
                    openCameraOrGallery(Util.SHIRT);
                } else if (item == 1)
                {
                    openCameraOrGallery(Util.PANT);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void openCameraOrGallery(String selectedClothType)
    {
        clothType = selectedClothType;
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
                addToDatabase(imagesFiles.get(0).getAbsolutePath(), clothType);

            }
        });
    }

    private void addToDatabase(final String filePath, final String clothType)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {

                Log.d(TAG, filePath);
                WardrobeItem wardrobeItem = realm.createObject(WardrobeItem.class, UUID.randomUUID().toString());
                wardrobeItem.setImagePath(filePath);
                wardrobeItem.setClothType(clothType);
            }
        });

        if (clothType.equals(Util.SHIRT))
        {
            notifyShirtAdapter();
        } else if (clothType.equals(Util.PANT))
        {
            notifyPaintsAdapter();
        }
        Toast.makeText(MainActivity.this, "Clothes added to your wardrobe!", Toast.LENGTH_LONG).show();
    }


    private void deleteFromDatabase(final String filePath, final String clothType)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {

                Log.d(TAG, filePath);
                RealmResults<WardrobeItem> result = realm.where(WardrobeItem.class)
                        .equalTo("imagePath", filePath).findAll();
                result.deleteAllFromRealm();
            }
        });

        if (clothType.equals(Util.SHIRT))
        {
            notifyShirtAdapter();
            //shirtsViewpager.setAdapter(shirtsPagerAdapter);
        } else if (clothType.equals(Util.PANT))
        {
            notifyPaintsAdapter();
            // pantsViewpager.setAdapter(pantsPagerAdapter);
        }

        Toast.makeText(MainActivity.this, "Clothes removed from your wardrobe!", Toast.LENGTH_LONG).show();
    }

    void notifyShirtAdapter()
    {
        RealmResults<WardrobeItem> wardrobeItems = realm.where(WardrobeItem.class)
                .contains("clothType", Util.SHIRT)
                .findAll();

        shirtsImagePathList.clear();

        for (WardrobeItem item : wardrobeItems)
        {
            shirtsImagePathList.add(item.getImagePath());
        }

        shirtsPagerAdapter.notifyDataSetChanged();
    }

    void notifyPaintsAdapter()
    {
        RealmResults<WardrobeItem> wardrobeItems = realm.where(WardrobeItem.class)
                .contains("clothType", Util.PANT)
                .findAll();

        pantsImagePathList.clear();

        for (WardrobeItem item : wardrobeItems)
        {
            pantsImagePathList.add(item.getImagePath());
        }

        pantsPagerAdapter.notifyDataSetChanged();
    }


    // ------------------------------------------------ OVERFLOW


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (item.getItemId() == R.id.favorite_collection)
        {
            startActivity(new Intent(this, FavoriteCollectionActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    // ------------------------------------------------ ALARM

    private void setRecurringAlarm(Context context)
    {
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getDefault());
        updateTime.set(Calendar.HOUR_OF_DAY, 6);
        updateTime.set(Calendar.MINUTE, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public String getEmojiByUnicode(int unicode)
    {
        return new String(Character.toChars(unicode));
    }


}
