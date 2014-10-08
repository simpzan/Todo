package simpzan.Todo.infrastructure;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import simpzan.Todo.R;
import simpzan.Todo.ui.AlarmReceiver;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/21/13
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReminderManager {
    public static final String INTENT_ACTION = "alarm.simpzan";
    Context context;
    Class receiverClass;
    AlarmManager alarmManager;
    NotificationController notificationController;
    Intent intent;

    static ReminderManager instance = null;
    static public ReminderManager getInstance(Context context) {
        if (instance == null) {
               instance = new ReminderManager(context, AlarmReceiver.class);
        }
        return instance;
    }

    public ReminderManager(Context context, Class receiver) {
        this.context = context;
        this.receiverClass = receiver;
        notificationController = new NotificationController(context);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, receiverClass);
    }

    public void cancelAlarm(int id) {
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);

        notificationController.makeToast("Alarm Cancelled");
    }

    public void scheduleAlarm(Date date, int id) {
        Long time = date.getTime();
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pi);

        notificationController.makeToast("Alarm at " + Utilities.readableDateTime(date) + " created ");
    }
}
