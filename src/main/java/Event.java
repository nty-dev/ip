import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event extends Task {
    protected final String from;
    protected final String to;

    private final LocalDateTime fromDt, toDt;
    private final LocalDate fromDate, toDate;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to   = to;

        this.fromDt = Dates.tryParseDateTime(from);
        this.toDt   = Dates.tryParseDateTime(to);

        this.fromDate = (fromDt == null) ? Dates.tryParseDate(from) : null;
        this.toDate   = (toDt   == null) ? Dates.tryParseDate(to)   : null;
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