package simpzan.Todo.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import simpzan.Todo.domain.Todo;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/20/13
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 *     Date mCreatedTime;
 Date mAlertTime;
 String mTitle;
 String mContent;
 boolean mFinished;
 */
public class TodoRepository extends SQLiteOpenHelper {
    private static final String ALARM = "alertTime";
    static final String DB_NAME = "Todo.sqlite";
    static final String TABLE_TODO = "Todo";
    public final static String CREATED = "createdTime";
    public final static String MODIFIED = "modifiedTime";
    static final String TITLE = "title";
    public final static String CONTENT = "content";
    static final String ID = "_id";
    private static final String IS_FINISHED = "finished";

    public TodoRepository(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_TODO +
                "(" + ID + " integer primary key autoincrement, " +
                CREATED + " integer, " +
                MODIFIED + " integer, " +
                ALARM + " integer, " +
                CONTENT + " text, " +
                TITLE + " text, " +
                IS_FINISHED + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void insertItem(Todo todo) {
        todo.updateModifiedTime();
        Utilities.print("inserting " + todo.toString());
        ContentValues cv = semiSerialize(todo);
        getWritableDatabase().insert(TABLE_TODO, null, cv);
    }

    public TodoItemCursor findAllItems() {
        Cursor wrapped = getReadableDatabase().query(TABLE_TODO,
                null, null, null, null, null, IS_FINISHED + " asc, " + MODIFIED + " desc");
        return new TodoItemCursor(wrapped);
    }

    public Todo findItemById(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_TODO,
                null,
                ID + "=" + id,
                null,
                null,
                null,
                null,
                "1");
        if (cursor.getCount() < 1)  return null;

        cursor.moveToFirst();
        Todo todo = deserialize(cursor);
        return todo;
    }

    public void updateItem(Todo todo) {
        todo.updateModifiedTime();
        ContentValues cv = semiSerialize(todo);
        getWritableDatabase().update(TABLE_TODO, cv, ID + "=" + todo.getId(), null);
    }

    public void deleteItem(Todo todo) {
        Utilities.print("deleting: " + todo.toString());
         getWritableDatabase().delete(TABLE_TODO, ID + "=" + todo.getId(), null);
    }

    static ContentValues semiSerialize(Todo todo) {
        ContentValues cv = new ContentValues();
        cv.put(CREATED, todo.getCreatedTime().getTime());
        cv.put(MODIFIED, todo.getModifiedTime().getTime());
        cv.put(TITLE, todo.getTitle());
        if (todo.getContent() != null) cv.put(CONTENT, todo.getContent());

        Long ts = 0l;
        if (todo.getAlarmTime() != null) ts = todo.getAlarmTime().getTime();
        cv.put(ALARM, ts);
        cv.put(IS_FINISHED, todo.isFinished());

        return cv;
    }

    static Todo deserialize(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ID));
        Date created = new Date(cursor.getLong(cursor.getColumnIndex(CREATED)));
        Date modified = new Date(cursor.getLong(cursor.getColumnIndex(MODIFIED)));
        String title = cursor.getString(cursor.getColumnIndex(TITLE));

        String content = cursor.getString(cursor.getColumnIndex(CONTENT));
        long alarmTS = cursor.getLong(cursor.getColumnIndex(ALARM));
        Date alarm = alarmTS == 0 ? null : new Date(alarmTS);
        boolean finished = cursor.getInt(cursor.getColumnIndex(IS_FINISHED)) != 0;

        Todo todo = new Todo(id, created, modified, title, content, alarm, finished);
        todo.setContent(content);
        todo.setAlarmTime(alarm);
        return todo;
    }

    public static class TodoItemCursor extends CursorWrapper {

        public Todo getTodoItem() {
            if (isBeforeFirst() || isAfterLast())
                return null;

            return deserialize(this);
        }

        public int getId() {
            if (isBeforeFirst() || isAfterLast())
                return -1;

            int id = getInt(getColumnIndex(ID));
            return id;
        }

        public TodoItemCursor(Cursor cursor) {
            super(cursor);
        }
    }

}
