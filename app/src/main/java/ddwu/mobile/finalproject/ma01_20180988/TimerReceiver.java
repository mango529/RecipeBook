package ddwu.mobile.finalproject.ma01_20180988;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerReceiver extends BroadcastReceiver {
    private static final String TAG = "TimerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notiIntent = new Intent(context, MainActivity.class);
        notiIntent.setAction(Intent.ACTION_MAIN);
        notiIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notiIntent.putExtra("notiTimer", "notiTimer");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(context, context.getString(R.string.CHANNEL_ID))
                .setSmallIcon(R.drawable.ic_recipe_svgrepo_com2)
                .setContentTitle("Recipe Book")
                .setContentText("타이머가 종료되었습니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 100;
        notificationManager.notify(notificationId, builder.build());
    }
}
