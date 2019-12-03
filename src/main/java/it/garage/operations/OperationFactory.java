package it.garage.operations;

import java.util.HashMap;
import java.util.Map;

public class OperationFactory {

    private static final Map<String, Long> operationDurations = new HashMap<>();

    private OperationFactory() {
    }

    public static Operation create(String operationType, String vehicle) {
        Long duration = operationDurations.get(operationType);
        if (duration == null) {
            throw new IllegalArgumentException("Operation of type " + operationType + " is unknown");
        }
        return new Operation(vehicle, operationType, duration);
    }

    public static void configureOp(String opType, long duration) {
        operationDurations.put(opType, duration);
    }
}
