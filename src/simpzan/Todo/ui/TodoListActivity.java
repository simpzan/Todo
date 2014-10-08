package simpzan.Todo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import simpzan.Todo.R;
import simpzan.Todo.domain.Todo;
import simpzan.Todo.domain.TodoManager;
import simpzan.Todo.infrastructure.ReminderManager;
import simpzan.Todo.infrastructure.TodoRepository;
import simpzan.Todo.infrastructure.TodoRepository.TodoItemCursor;
import simpzan.Todo.infrastructure.Utilities;
import simpzan.Todo.ui.Views.SwipeDismissListViewTouchListener;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/21/13
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class TodoListActivity extends Activity {

    TodoRepository _repository;
    TodoItemCursor _cursor;

    CursorAdapter _listAdapter;

    ListView _listView;
    EditText _textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        setupDomain();
        setupUI();
    }

    private void setupDomain() {
        _repository = TodoManager.getInstance(getApplicationContext()).getRepository();
        _cursor = _repository.findAllItems();
    }

    private void setupUI() {
        setContentView(R.layout.todo_list);

        _textInput = (EditText)findViewById(R.id.editText);
        _textInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        onEditTextEnterPress();
                    }
                    return true;
                }
                return false;
            }
        });
        _listView = (ListView)findViewById(R.id.listView);
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewItemClick((int)position);
            }
        });

        _listAdapter = new TodoItemCursorAdapter(this, _cursor);
        _listView.setAdapter(_listAdapter);

        SwipeDismissListViewTouchListener swipeListener = new SwipeDismissListViewTouchListener(_listView,
            new SwipeDismissListViewTouchListener.DismissCallbacks() {

                @Override
                public boolean canDismiss(int position) {
                    return true;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                    deleteARow(reverseSortedPositions[0]);
                }
            }
        );
        _listView.setOnTouchListener(swipeListener);
        _listView.setOnScrollListener(swipeListener.makeScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_list_menu, menu);
        return true;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear) {
            clearAllFinishedTodos();
            return true;
        }
        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void clearAllFinishedTodos() {
        ArrayList<Todo> finishedTodos = new ArrayList<Todo>();
        TodoItemCursor cursor = _repository.findAllItems();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Todo todo = cursor.getTodoItem();
            if (todo.isFinished())  finishedTodos.add(todo);
        }

        for (Todo todo : finishedTodos) {
            _repository.deleteItem(todo);
        }

        refreshListView();
    }

    void onEditTextEnterPress() {
        String title = _textInput.getText().toString();
        Todo todo = new Todo(new Date(), title);
        _repository.insertItem(todo);
        _textInput.setText("");
        refreshListView();
    }

    void onListViewItemClick(int id) {
        TodoItemCursor cursor = (TodoItemCursor) _listAdapter.getItem(id);
        int todoId = cursor.getId();

        Intent intent = new Intent(this, TodoEditorActivity.class);
        intent.putExtra(TodoEditorActivity.EXTRA_TODO_ITEM_ID, todoId);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshListView();
    }

    void deleteARow(int id) {
        TodoItemCursor cursor = (TodoItemCursor) _listAdapter.getItem(id);
        Todo todo = cursor.getTodoItem();

        if (todo.getAlarmTime() != null) {  // cancel alarm if it's on.
            ReminderManager manager = ReminderManager.getInstance(getApplicationContext());
            manager.cancelAlarm(todo.getId());
        }
        todo.setFinished(!todo.isFinished());
        _repository.updateItem(todo);
        refreshListView();
    }

    private void refreshListView() {
        _cursor.requery();
        _listAdapter.notifyDataSetChanged();
    }

    private static class TodoItemCursorAdapter extends CursorAdapter {

        private TodoItemCursor mRunCursor;

        public TodoItemCursorAdapter(Context context, TodoItemCursor cursor) {
            super(context, cursor, 0);
            mRunCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // use a layout inflater to get a row view
            LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        int strikeThroughPaintFlags = 273;  //textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
        int normalPaintFlags = 257;

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // get the run for the current row
            Todo todo = mRunCursor.getTodoItem();
            int flags = todo.isFinished() ? strikeThroughPaintFlags : normalPaintFlags;

            TextView startDateTextView = (TextView)view;
            startDateTextView.setPaintFlags(flags);
            startDateTextView.setText(todo.getTitle());
        }

    }
}
