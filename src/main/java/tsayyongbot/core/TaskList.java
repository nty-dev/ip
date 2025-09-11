package tsayyongbot.core;

import tsayyongbot.task.Task;
import tsayyongbot.task.Todo;
import tsayyongbot.task.Deadline;
import tsayyongbot.task.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable list of tasks with operations to add, update, delete, and search.
 */
public class TaskList {

    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        assert initial != null : "TaskList initial list must not be null";
        for (Task t : initial) {
            assert t != null : "TaskList must not contain null tasks";
        }
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

    /**
     * Returns tasks whose descriptions contain the given keyword
     * (case-insensitive).
     * 
     * @param keyword term to search for
     * @return matching tasks in original order
     */
    public List<Task> find(String keyword) {
        String k = keyword.toLowerCase();
        List<Task> res = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(k))
                res.add(t);
        }
        return res;
    }
}
