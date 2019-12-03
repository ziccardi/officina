package it.garage.operations;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

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

    public Map<Date, Operation> getReport() {
        // copy to avoid returing mutable internals
        return new TreeMap<>(this.reportContainer);
    }
}
