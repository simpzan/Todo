package simpzan.Todo.infrastructure;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    static public void print(Object obj) {
        Log.d("simpzan", obj.toString());
    }

    static public String readableDateTime(Date time) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy - HH:mm");
        String formatted = format.format(time);
        return formatted;
    }
}