package it.garage;

import java.util.ArrayList;
import java.util.List;

public class Garage {
    private List<Platform> platforms = new ArrayList<>();

    public void addPlatform(Platform p) {
        this.platforms.add(p);
    }

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