package simpzan.Todo.infrastructure;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;
import simpzan.Todo.R;

public class NotificationController {
    Context context;
    public NotificationController(Context context1) {
        this.context = context1;
    }

    public void playRingtone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.play();
    }

    public void makeVibration() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public void makeToast(String text) {
        Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
        t.setDuration(10);
        t.show();
    }

    public void makeNotification(String s) {
        Notification noti = new Notification.Builder(context)
                .setContentTitle("")
                .setContentText(s)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        NotificationManager mgr = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        mgr.notify(2, noti);
    }
}