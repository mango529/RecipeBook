package ddwu.mobile.finalproject.ma01_20180988;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class FragmentTimer extends Fragment {
    NumberPicker npHour, npMinute, npSecond;
    Button btnTimerStart, btnTimerCancel;
    TextView tvTimer;
    CountDownTimer countDownTimer;
    ConstraintLayout clTimerSet, clTimerStart;

    int hour, minute, second;
    PendingIntent sender;
    AlarmManager alarmManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        createNotificationChannel();

        npHour = view.findViewById(R.id.npHour);
        npMinute = view.findViewById(R.id.npMinute);
        npSecond = view.findViewById(R.id.npSecond);
        btnTimerStart = view.findViewById(R.id.btnTimerStart);
        btnTimerCancel = view.findViewById(R.id.btnTimerCancel);
        tvTimer = view.findViewById(R.id.tvTimer);
        clTimerSet = view.findViewById(R.id.clTimerSet);
        clTimerStart = view.findViewById(R.id.clTimerStart);

        npHour.setMaxValue(12);
        npHour.setMinValue(0);
        npMinute.setMaxValue(59);
        npMinute.setMinValue(0);
        npSecond.setMaxValue(59);
        npSecond.setMinValue(0);

        btnTimerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clTimerSet.setVisibility(View.GONE);
                clTimerStart.setVisibility(View.VISIBLE);

                hour = npHour.getValue();
                minute = npMinute.getValue();
                second = npSecond.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                long currentTime = calendar.getTimeInMillis();
                calendar.add(Calendar.HOUR, +hour);
                calendar.add(Calendar.MINUTE, +minute);
                calendar.add(Calendar.SECOND, +second);
                long setTime = calendar.getTimeInMillis();

                Intent intent = new Intent(getContext(), TimerReceiver.class);
                sender = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                alarmManager.set(AlarmManager.RTC, setTime - currentTime, sender);

                countDownTimer = new CountDownTimer(setTime - currentTime + 1000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvTimer.setText(String.format("%02d : %02d : %02d", hour, minute, second));

                        if (second == 0 ) {
                            minute--;
                            second = 59;
                            if (minute == 0) {
                                hour--;
                                minute = 59;
                            }
                        }
                        second--;
                    }

                    @Override
                    public void onFinish() {
                        clTimerSet.setVisibility(View.VISIBLE);
                        clTimerStart.setVisibility(View.GONE);
                    }
                };
                countDownTimer.start();
            }
        });

        btnTimerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sender != null) alarmManager.cancel(sender);
                countDownTimer.cancel();
                clTimerSet.setVisibility(View.VISIBLE);
                clTimerStart.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}