package com.example.root.wardrobetask.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import com.example.root.wardrobetask.R;

import com.example.root.wardrobetask.util.AlarmReceiver;
import com.example.root.wardrobetask.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    @BindView(R.id.wardrobe_notification_toggle)
    SwitchCompat wardrobeNotificationToggle;
    private boolean notificationPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        notificationPreference = Util.getPreferenceForNotification(SettingsActivity.this);

        wardrobeNotificationToggle.setChecked(notificationPreference);
        wardrobeNotificationToggle.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked)
    {
        switch (compoundButton.getId())
        {
            case R.id.wardrobe_notification_toggle:
            {
                if(ischecked)
                {
                    Util.setPreferenceForNotifcarion(SettingsActivity.this, true);
                }
                else
                {
                    Util.setPreferenceForNotifcarion(SettingsActivity.this, false);
                    cancelRecurringAlarm();
                }
            }
            break;

        }

    }

    //Stop Notification service for User
    private void cancelRecurringAlarm()
    {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(pendingIntent);
    }
}
