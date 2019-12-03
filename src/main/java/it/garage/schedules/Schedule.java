package it.garage.schedules;

import it.garage.operations.Operation;

import java.util.*;

/**
 * Mantains the garage day by day schedule.
 */
public class Schedule {
    private List<WorkDay> workdays = new ArrayList<>();

    private WorkDay getFirstAvailableWorkday(Operation op, boolean insertIfNew) {
        if (workdays.isEmpty()) {
            WorkDay wd = new WorkDay();
            if (insertIfNew) {
                this.workdays.add(wd);
            }
            return wd;
        } else {
            for (WorkDay wd : this.workdays) {
                // try to insert the operation at the earliest availability
                Date executionTime = wd.fits(op);
                if (executionTime != null) {
                    return wd;
                }
            }
            // None of the available workday had enough time for the operation.
            WorkDay wd =  new WorkDay(this.workdays.get(this.workdays.size() - 1));
            if (insertIfNew) {
                this.workdays.add(wd);
            }
            return wd;
        }
    }

    /**
     * @param op The operation that we'd like to schedule.
     * @return And estimation of when the operation can be executed.
     */
    public Date estimate(Operation op) {
        return getFirstAvailableWorkday(op, false).fits(op);
    }

    /**
     * Schedule a new operation as soon as possible.
     * @param op The operation to be scheduled.
     * @return the date when the operation will be executed.
     */
    public Date add(Operation op) {
        return getFirstAvailableWorkday(op, true).add(op);
    }

    /**
     * @return a day by day report of the operation to be executed in a platform.
     */
    public Map<Date, Map<Date, Operation>> getScheduleMap() {
        Map<Date, Map<Date, Operation>> res = new TreeMap<>();

        this.workdays.forEach(workDay -> res.put(workDay.toDate(), workDay.getSchedule()));
        return res;
    }
}
