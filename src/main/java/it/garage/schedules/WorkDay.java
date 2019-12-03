package it.garage.schedules;

import it.garage.operations.CompletedOperationsStore;
import it.garage.operations.Operation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Manages the schedule for a given day.
 */
public class WorkDay implements PropertyChangeListener {

    /**
     * The working day starts at 8 in the morning
     */
    private final static int startTime = 8;

    /**
     * The working day ends at 5 in the evening
     */
    private final static int endTime = 17;

    /**
     * Utility constanst to convert to/from milliseconds
     */
    private final static long SECOND = 1000;
    private final static long MINUTE = 60 * SECOND;
    private final static long HOUR = 60 * MINUTE;

    private final static long DAYLENGTH = (endTime - startTime) * HOUR;

    private final Date date;
    private List<Operation> dailySchedule = new ArrayList<>();

    /**
     * Creates a new workday initialized with the 'today' date.
     */
    public WorkDay() {
        this(null);
    }

    // Workday starts at 8.00 and ends at 17.00
    public WorkDay(WorkDay previous) {
        Calendar c = Calendar.getInstance();
        if (previous == null) {
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY, startTime);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            this.date = c.getTime();
        } else {
            c.setTime(previous.date);
            c.set(Calendar.HOUR_OF_DAY, startTime);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.add(Calendar.DATE, 1);
            this.date = c.getTime();
        }
    }

    /**
     * Returns the date/time where the operation could be inserted or null if there is no available slots in this day.
     * @param newOp the operation to be evaluated.
     * @return the date/time where the operation could be inserted or null if there is no available slots in this day.
     */
    public Date fits(Operation newOp) {
        long total = 0;
        for (Operation op: this.dailySchedule) {
            total += (op.getDuration() * MINUTE);
        }
        if (total + newOp.getDuration() * MINUTE <= DAYLENGTH) {
            return new Date(this.date.getTime() + total);
        }

        return null;
    }

    /**
     * Adds an operation to the schedule for the current day if possible.
     * @param newOp the operation to be scheduled.
     * @return the date/time where the operation could be inserted or null if there is no available slots in this day.
     */
    public Date add(Operation newOp) {
        Date executionTime = this.fits(newOp);
        if (executionTime != null) {
            this.dailySchedule.add(newOp);
            newOp.addPropertyChangeListener(this);
            newOp.addPropertyChangeListener(CompletedOperationsStore.getInstance());
            return executionTime;
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // In this exercise, this method is called only when an operation is completed, so no checks on the received event are done
        this.dailySchedule.remove((Operation)evt.getSource());
    }

    public Date toDate() {
        return this.date;
    }

    public Map<Date, Operation> getSchedule() {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(this.date);

        Map<Date, Operation> res = new TreeMap<>();

        for (Operation op: this.dailySchedule) {
            res.put(startTime.getTime(), op);
            startTime.add(Calendar.MINUTE, (int) op.getDuration());
        }

        return res;
    }
}
