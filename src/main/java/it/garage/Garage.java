package it.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the entire garage.
 */
public class Garage {
    private List<Platform> platforms = new ArrayList<>();

    /**
     * Adds a new platform to the garage
     * @param p platform to be added.
     */
    public void addPlatform(Platform p) {
        this.platforms.add(p);
    }

    /**
     * Schedules a new operation in the garage at the best available time.
     * @param vehicle The vehicle to be serviced
     * @param operationType The type of service to be performed
     * @return An estimation of when the service will be executed
     * @throws OperationNotSupportedException if the specified service is not supported for the specified vehicle.
     */
    public Platform.Estimation scheduleOperation(String vehicle, String operationType) throws OperationNotSupportedException {
        // find the earliest possible time
        Platform.Estimation earliestTime = null;
        Platform platform = null;

        for (Platform p: platforms) {
            if (p.canDo(vehicle, operationType)) {
                Platform.Estimation estimation = p.estimate(vehicle, operationType);
                if (estimation.isEarlier(earliestTime)) {
                    earliestTime = estimation;
                    platform = p;
                }
            }
        }

        if (platform != null) {
            return platform.addOperation(vehicle, operationType);
        } else {
            throw new OperationNotSupportedException("No platforms support operation " + operationType + " for vehicle " + vehicle);
        }
    }

    public List<Platform> getAllPlatforms() {
        // Return a copy to avoid returning mutable internals
        return new ArrayList<>(platforms);
    }

}