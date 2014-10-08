package simpzan.Todo.domain;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/20/13
 * Time: 10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Todo {
    private int Id;
    private Date mCreatedTime;
    private Date mModifiedTime;
    private String mTitle;

    private String mContent;
    private Date mAlarmTime;
    private boolean mFinished;

    public Todo(Date createdTime, String title) {
        mCreatedTime = createdTime;
        mTitle = title;
        mModifiedTime = mCreatedTime;
    }

    public Todo(int id, Date created, Date modified, String title, String content, Date alarm,
                boolean finished) {
        this(created, title);
        this.Id = id;
        mModifiedTime = modified;
        mContent = content;
        mAlarmTime = alarm;
        mFinished = finished;
    }

    @Override
    public String toString() {
        return "id:" + Id + " created:" + mCreatedTime.toString() + " content:" + mContent;
    }

    public Date getCreatedTime() {
        return mCreatedTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public int getId() {
        return Id;
    }

    public Date getAlarmTime() {
        return mAlarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        mAlarmTime = alarmTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean finished) {
        mFinished = finished;
    }

    public Date getModifiedTime() {
        return mModifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        mModifiedTime = modifiedTime;
    }
    public void updateModifiedTime() {
        setModifiedTime(new Date());
    }
}
