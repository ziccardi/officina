package it.garage.operations;

import java.util.HashMap;
import java.util.Map;

public class OperationFactory {

    private static final Map<String, Long> operationDurations = new HashMap<>();

    private OperationFactory() {
    }

    /**
     * Creates an operation of the given type. The specified operation must exist in the current factory configuration.
     * @param operationType The type of the operation to be created
     * @param vehicle The vehicle that will be serviced
     * @return
     */
    public static Operation create(String operationType, String vehicle) {
        Long duration = operationDurations.get(operationType);
        if (duration == null) {
            throw new IllegalArgumentException("Operation of type " + operationType + " is unknown");
        }
        return new Operation(vehicle, operationType, duration);
    }

    /**
     * Adds a new operation
     * @param opType The type of the operation
     * @param duration The duration of the operation in minutes
     */
    public static void configureOp(String opType, long duration) {
        operationDurations.put(opType, duration);
    }
}
