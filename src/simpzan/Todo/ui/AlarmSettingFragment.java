package simpzan.Todo.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import simpzan.Todo.R;
import simpzan.Todo.infrastructure.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/22/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmSettingFragment extends Fragment {
    GregorianCalendar _dateTime = new GregorianCalendar();
    boolean isAlarmSet = false;

    private Button _setAlarmButton;
    private Button _unsetAlarmButton;
    private Button _dateButton;
    private Button _timeButton;

    public AlarmSettingFragment(Date time) {
        setAlarmTime(time);
    }

    void setAlarmTime(Date time) {
        if (time == null)  {
            isAlarmSet = false;
        }  else {
            _dateTime.setTime(time);
            isAlarmSet = true;
        }
    }

    Date getAlarmTime() {
        if (!isAlarmSet)  return null;
        return _dateTime.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.alarm_setting, container, false);

        _setAlarmButton = (Button)view.findViewById(R.id.setAlarmButton);
        _setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetAlarm(v);
            }
        });
        _unsetAlarmButton = (Button)view.findViewById(R.id.unsetAlarmButton);
        _unsetAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUnsetAlarm(v);
            }
        });
        _dateButton = (Button)view.findViewById(R.id.dateButton);
        _dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowDatePicker(v);
            }
        });
        _timeButton = (Button)view.findViewById(R.id.timeButton);
        _timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowTimePicker(v);
            }
        });

        updateViews();

        return view;
    }

    // make views reflect data.
    private void updateViews() {
        if (getAlarmTime() != null) {
            updateButtonsVisibility(false);
            updateDateTimeButtons();
        } else {
            updateButtonsVisibility(true);
        }
    }

    private void updateButtonsVisibility(boolean isSetButtonVisible) {
        int setButtonVisibility = isSetButtonVisible ? View.VISIBLE : View.GONE;
        int unsetButtonVisibility = isSetButtonVisible ? View.GONE : View.VISIBLE;

        _setAlarmButton.setVisibility(setButtonVisibility);
        _dateButton.setVisibility(unsetButtonVisibility);
        _timeButton.setVisibility(unsetButtonVisibility);
        _unsetAlarmButton.setVisibility(unsetButtonVisibility);
    }

    private void updateDateTimeButtons() {
        Date time = getAlarmTime();
        String formatted = Utilities.readableDateTime(time);
        String[] parts = formatted.split(" - ");
        _dateButton.setText(parts[0]);
        _timeButton.setText(parts[1]);
    }

    public void onSetAlarm(View v) {
        int oneDay = 24 * 60 * 60 * 1000;
        Date alarmTime = new Date(new Date().getTime() + 6000);
        setAlarmTime(alarmTime);

        updateViews();
    }

    public void onUnsetAlarm(View v) {
        setAlarmTime(null);
        updateViews();
    }

    public void onShowTimePicker(View v) {
        final Calendar c = _dateTime;
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Context context = getActivity();
        final TimePicker timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        new AlertDialog.Builder(context)
                .setTitle("Time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _dateTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        _dateTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                        updateViews();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).setView(timePicker).show();
    }

    public void onShowDatePicker(View v) {
        final Calendar c = _dateTime;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final DatePicker datePicker = new DatePicker(getActivity());
        datePicker.init(year, month, day, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setCalendarViewShown(false);
        }

        new AlertDialog.Builder(getActivity())
                .setTitle("Date")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _dateTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        updateViews();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).setView(datePicker).show();
    }
}
