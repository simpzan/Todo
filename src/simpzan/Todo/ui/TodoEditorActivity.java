package simpzan.Todo.ui;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import simpzan.Todo.R;
import simpzan.Todo.domain.Todo;
import simpzan.Todo.domain.TodoManager;
import simpzan.Todo.infrastructure.ReminderManager;
import simpzan.Todo.infrastructure.TodoRepository;
import simpzan.Todo.infrastructure.Utilities;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/21/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoEditorActivity extends Activity {
    public static String EXTRA_TODO_ITEM_ID = "id";
    private TodoRepository _repository;
    private Todo _todo;

    private ReminderManager reminderManager;

    private EditText _titleView;
    private EditText _contentView;
    AlarmSettingFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        setupData();

        reminderManager = ReminderManager.getInstance(getApplicationContext());

        setupUI();
    }

    private void setupData() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_TODO_ITEM_ID, -1);
        assert id != -1;

        _repository = TodoManager.getInstance(getApplicationContext()).getRepository();
        _todo = _repository.findItemById(id);
    }

    private void setupUI() {
        setContentView(R.layout.todo_editor);
        _contentView = (EditText)findViewById(R.id.contentView);
        _titleView = (EditText)findViewById(R.id.titleView);

        updateViews();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        fragment = new AlarmSettingFragment(_todo.getAlarmTime());
        transaction.add(R.id.containerView, fragment);
        transaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onEditDoneClick(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEditDoneClick(View v) {
        Date lastAlarm = _todo.getAlarmTime();
        Date thisAlarm = fragment.getAlarmTime();

        _todo.setTitle(_titleView.getText().toString());
        _todo.setContent(_contentView.getText().toString());
        _todo.setAlarmTime(thisAlarm);
        _repository.updateItem(_todo);

        if (thisAlarm == null && lastAlarm != null) {
            reminderManager.cancelAlarm(_todo.getId());
        } else if (thisAlarm != null && !thisAlarm.equals(lastAlarm)) {
            reminderManager.scheduleAlarm(thisAlarm, _todo.getId());
        }

        finish();
    }

    private void updateViews() {
        _contentView.setText(_todo.getContent());
        _titleView.setText(_todo.getTitle());

    }

}
