public abstract class Task {
    protected final String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() { 
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    protected abstract TaskType getType();

    public String pretty() {
        return String.format("[%s][%s] %s", getType().symbol(), getStatusIcon(), description);
    }

    @Override
    public String toString() {
        return pretty();
    }
}