package tsayyongbot.task;

import tsayyongbot.core.Dates;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event extends Task {
    protected String from;
    protected String to;

    private final LocalDateTime fromDt, toDt;
    private final LocalDate fromDate, toDate;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
        assert from != null && to != null : "Event times must not be null";
        assert !from.trim().isEmpty() && !to.trim().isEmpty() : "Event times must not be empty";

        this.fromDt = Dates.tryParseDateTime(from);
        this.toDt = Dates.tryParseDateTime(to);

        this.fromDate = (fromDt == null) ? Dates.tryParseDate(from) : null;
        this.toDate = (toDt == null) ? Dates.tryParseDate(to) : null;
        assert !toDate.isBefore(fromDate) : "Event end must be >= start";
    }

    public void setSchedule(String from, String to) {
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
        String fromDisp = (fromDt != null) ? Dates.format(fromDt)
                : (fromDate != null) ? Dates.format(fromDate)
                        : from;
        String toDisp = (toDt != null) ? Dates.format(toDt)
                : (toDate != null) ? Dates.format(toDate)
                        : to;
        return super.toString() + " (from: " + fromDisp + " to: " + toDisp + ")";
    }
}