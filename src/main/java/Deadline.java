public class Deadline extends Task {
    protected final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    protected TaskType getType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}