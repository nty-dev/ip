import java.time.LocalDate;
import java.time.LocalDateTime;

public class Deadline extends Task {
    protected final String by;
    private final LocalDate date;
    private final LocalDateTime dateTime;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.dateTime = Dates.tryParseDateTime(by);
        this.date = (this.dateTime == null) ? Dates.tryParseDate(by) : null;
    }

    public String getBy() {
        return by;
    }

    @Override
    protected TaskType getType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        String display;
        if (dateTime != null) {
            display = Dates.format(dateTime);
        } else if (date != null) {
            display = Dates.format(date);
        } else {
            display = by;
        }
        return super.toString() + " (by: " + display + ")";
    }
}