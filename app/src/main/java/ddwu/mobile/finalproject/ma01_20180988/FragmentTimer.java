package ddwu.mobile.finalproject.ma01_20180988;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class FragmentTimer extends Fragment {
    NumberPicker npHour, npMinute, npSecond;
    Button btnTimerStart,  btnTimerStop, btnTimerCancel, btnTimerContinue;
    TextView tvTimer;
    CountDownTimer countDownTimer;
    ConstraintLayout clTimerSet, clTimerStart;

    int hour, minute, second;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        npHour = view.findViewById(R.id.npHour);
        npMinute = view.findViewById(R.id.npMinute);
        npSecond = view.findViewById(R.id.npSecond);
        btnTimerStart = view.findViewById(R.id.btnTimerStart);
        btnTimerStop = view.findViewById(R.id.btnTimerStop);
        btnTimerCancel = view.findViewById(R.id.btnTimerCancel);
        btnTimerContinue = view.findViewById(R.id.btnTimerContinue);
        tvTimer = view.findViewById(R.id.tvTimer);
        clTimerSet = view.findViewById(R.id.clTimerSet);
        clTimerStart = view.findViewById(R.id.clTimerStart);

        npHour.setMaxValue(12);
        npHour.setMinValue(0);
        npMinute.setMaxValue(59);
        npMinute.setMinValue(0);
        npSecond.setMaxValue(59);
        npSecond.setMinValue(0);

        //앱 껐켰시 유지시키기
        btnTimerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clTimerSet.setVisibility(View.INVISIBLE);
                clTimerStart.setVisibility(View.VISIBLE);
                hour = npHour.getValue();
                minute = npMinute.getValue();
                second = npSecond.getValue();
                long millis = dateToMill(String.format("%02d : %02d : %02d", hour, minute, second));

                countDownTimer = new CountDownTimer(millis,1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvTimer.setText(String.format("%02d : %02d : %02d", hour, minute, second));
                        if (second == 0 ) {
                            minute--;
                            second = 59;
                        }
                        if (second == 0 && minute == 0) {
                            hour--;
                            second = 59;
                        }
                        second--;
                    }

                    @Override
                    public void onFinish() {
                        // 알림 보내기
                        clTimerSet.setVisibility(View.VISIBLE);
                        clTimerStart.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "타이머 종료", Toast.LENGTH_SHORT).show();
                    }
                };
                countDownTimer.start();
            }
        });

        btnTimerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                clTimerSet.setVisibility(View.VISIBLE);
                clTimerStart.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    public long dateToMill(String date) {
        String pattern = "HH : mm : ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date trans_date = null;
        try {
            trans_date = formatter.parse(date);
        } catch (ParseException e) {
             e.printStackTrace();
        } return trans_date.getTime();
    }
}