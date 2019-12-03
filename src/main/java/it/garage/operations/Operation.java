package it.garage.operations;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Operation {
    private static int OPCODE = 0;

    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private boolean complete = false;

    private final String vehicleType;
    private final String opType;
    private final long duration;
    private final int code;

    public Operation(String vehicleType, String opType, long duration) {
        this.vehicleType = vehicleType;
        this.opType = opType;
        this.duration = duration;
        this.code = ++OPCODE;
    }

    public final boolean isComplete() {
        return this.complete;
    }

    public final void complete() {
        if (complete) {
            return;
        }
        this.complete = true;
        this.changes.firePropertyChange("complete", false, true);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.changes.addPropertyChangeListener(listener);
    }

    /**
     * @return the type of the operation
     */
    public String getOperationType() {
        return this.opType;
    }

    /**
     * @return an estimation of the operation duration (minutes)
     */
    public long getDuration() {
        return this.duration;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public int getCode() {
        return this.code;
    }
}
