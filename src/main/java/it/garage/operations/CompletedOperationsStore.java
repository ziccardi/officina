package it.garage.operations;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class listens for events on the `Operation` object to store a list of the completed operations.
 */
public class CompletedOperationsStore implements PropertyChangeListener {
    private static CompletedOperationsStore INSTANCE = new CompletedOperationsStore();

    private Map<Date, Operation> reportContainer = new TreeMap<>();

    public static CompletedOperationsStore getInstance() {
        return INSTANCE;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Operation op = (Operation) evt.getSource();
        this.reportContainer.put(new Date(), op);
    }

    /**
     * @return a report of the completed operations divided by date
     */
    public Map<Date, Operation> getReport() {
        // copy to avoid returing mutable internals
        return new TreeMap<>(this.reportContainer);
    }
}
