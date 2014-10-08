package simpzan.Todo.domain;

import android.content.Context;
import simpzan.Todo.infrastructure.TodoRepository;

/**
 * Created with IntelliJ IDEA.
 * User: simpzan
 * Date: 12/21/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoManager {
    TodoRepository _repository;
    public TodoRepository getRepository() {
        return _repository;
    }

    private static TodoManager instance;
    private TodoManager(Context context) {
        _repository = new TodoRepository(context);
    }
    public static TodoManager getInstance(Context context) {
        if (instance == null) {
            instance = new TodoManager(context);
        }
        return instance;
    }

}
