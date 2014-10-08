package simpzan.Todo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import simpzan.Todo.infrastructure.NotificationController;


/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/22/13
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationController notificationController = new NotificationController(context);
        notificationController.makeVibration();
        notificationController.makeNotification("Alarm fired!");
    }
}
