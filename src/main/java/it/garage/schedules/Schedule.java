package it.garage.schedules;

import it.garage.operations.Operation;

import java.util.*;

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

    public Date estimate(Operation op) {
        return getFirstAvailableWorkday(op, false).fits(op);
    }

    public Date add(Operation op) {
        return getFirstAvailableWorkday(op, true).add(op);
    }

    public Map<Date, Map<Date, Operation>> getScheduleMap() {
        Map<Date, Map<Date, Operation>> res = new TreeMap<>();

        this.workdays.forEach(workDay -> res.put(workDay.toDate(), workDay.getSchedule()));
        return res;
    }
}
