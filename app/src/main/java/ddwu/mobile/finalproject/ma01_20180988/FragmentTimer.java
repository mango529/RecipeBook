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
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class FragmentTimer extends Fragment {
    NumberPicker npHour, npMinute, npSecond;
    Button btnTimerStart;
    TextView tvTimer;
    CountDownTimer countDownTimer;

    int hour, minute, second;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        npHour = view.findViewById(R.id.npHour);
        npMinute = view.findViewById(R.id.npMinute);
        npSecond = view.findViewById(R.id.npSecond);
        btnTimerStart = view.findViewById(R.id.btnTimerStart);
        tvTimer = view.findViewById(R.id.tvTimer);

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
                // 피커 숨기고 tvTimer, 정지, 취소, 재시작 버튼 띄우기
                hour = npHour.getValue();
                minute = npMinute.getValue();
                second = npSecond.getValue();
                long millis = dateToMill(String.format("%2d:%2d:%2d", hour, minute, second));

                countDownTimer = new CountDownTimer(millis,1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvTimer.setText(String.format("%2d:%2d:%2d", hour, minute, second));
                        if (second == 0 ) {
                            minute--;
                        }
                        if (second == 0 && minute == 0) {
                            hour--;
                        }
                        second--;
                    }

                    @Override
                    public void onFinish() {
                        // 피커 띄우고 알림 보내기
                        Toast.makeText(getContext(), "타이머 종료", Toast.LENGTH_SHORT).show();
                    }
                };
                countDownTimer.start();
            }
        });




//        TimerTask timerTask = new TimerTask() {
//            int hour = npHour.getValue();
//            int minute = npMinute.getValue();
//            int second = npSecond.getValue();
//
//            @Override
//            public void run() {
//                tvTimer.setText(String.format("%2d : %2d : %2d", hour, minute, second));
//
//            }
//        };

        return view;
    }

    public long dateToMill(String date) {
        String pattern = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date trans_date = null;
        try {
            trans_date = formatter.parse(date);
        } catch (ParseException e) {
             e.printStackTrace();
        } return trans_date.getTime();
    }
}