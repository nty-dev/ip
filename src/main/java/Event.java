public class Event extends Task {
    protected final String from;
    protected final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }
    
    public String getTo() {
        return to;
    }


    @Override
    protected TaskType getType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}