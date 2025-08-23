package tsayyongbot.core;
import tsayyongbot.task.Task;
import tsayyongbot.task.Todo;
import tsayyongbot.task.Deadline;
import tsayyongbot.task.Event;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public List<Task> asList() {
        return tasks;
    }

    public int size() {
        return tasks.size();
    }

    private void ensureInRange(int idx) throws TsayYongBotException {
        if (idx < 1 || idx > tasks.size()) {
            throw new TsayYongBotException("Usage: <cmd> <task-number>");
        }
    }

    public Task addTodo(String desc) {
        Task t = new Todo(desc);
        tasks.add(t);
        return t;
    }

    public Task addDeadline(String desc, String by) {
        Task t = new Deadline(desc, by);
        tasks.add(t);
        return t;
    }

    public Task addEvent(String desc, String from, String to) {
        Task t = new Event(desc, from, to);
        tasks.add(t);
        return t;
    }

    public Task delete(int idx1) throws TsayYongBotException {
        ensureInRange(idx1);
        return tasks.remove(idx1 - 1);
    }

    public Task mark(int idx1) throws TsayYongBotException {
        ensureInRange(idx1);
        Task t = tasks.get(idx1 - 1);
        t.markAsDone();
        return t;
    }

    public Task unmark(int idx1) throws TsayYongBotException {
        ensureInRange(idx1);
        Task t = tasks.get(idx1 - 1);
        t.markAsNotDone();
        return t;
    }
}
