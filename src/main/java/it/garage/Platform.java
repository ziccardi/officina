package it.garage;

import it.garage.operations.Operation;
import it.garage.operations.OperationFactory;
import it.garage.schedules.Schedule;

import java.util.*;

public class Platform {
    private final List<String> supportedVehicles;
    private final List<String> supportedServices;

    private final static long MINUTE = 60 * 1000;

    private Schedule schedule = new Schedule();
    private final String name;

    public Platform(String name, String[] supportedVehicles, String[] supportedServices) {
        this.supportedVehicles = Arrays.asList(supportedVehicles);
        this.supportedServices = Arrays.asList(supportedServices);
        this.name = name;
    }

    public Platform(String name, List<String> supportedVehicles, List<String> supportedServices) {
        // Copy the lists to avoid exposing mutable internals
        this.supportedVehicles = new ArrayList<>(supportedVehicles);
        this.supportedServices = new ArrayList<>(supportedServices);
        this.name = name;
    }

    public boolean canDo(String vehicle, String operationType) {
        return this.supportedVehicles.contains(vehicle) && this.supportedServices.contains(operationType);
    }

    public Estimation estimate(String vehicle, String newOpType) throws OperationNotSupportedException{
        if (!this.canDo(vehicle, newOpType)) {
            throw new OperationNotSupportedException("This platform does not supports operation " + newOpType + " for vehicle " + vehicle);
        }
        Operation newOp = OperationFactory.create(newOpType, vehicle);
        Date executionTime = schedule.estimate(newOp);
        return new Estimation(this.name, executionTime.getTime(), executionTime.getTime() + newOp.getDuration() * MINUTE);
    }

    public Estimation addOperation(String vehicle, String newOpType) throws OperationNotSupportedException {
        if (!this.canDo(vehicle, newOpType)) {
            throw new OperationNotSupportedException("This platform does not supports operation " + newOpType + " for vehicle " + vehicle);
        }
        Operation newOp = OperationFactory.create(newOpType, vehicle);
        Date executionTime = this.schedule.add(newOp);
        return new Estimation(this.name, executionTime.getTime(), executionTime.getTime() + newOp.getDuration() * MINUTE);
    }

    public String[] getAllSupportedVehicles() {
        return this.supportedVehicles.toArray(new String[this.supportedVehicles.size()]);
    }

    public String[] getAllSupportedServices() {
        return this.supportedServices.toArray(new String[this.supportedServices.size()]);
    }

    public static class Estimation {
        private final Date startTime;
        private final Date endTime;
        private final String platformName;

        private Estimation(String platformName, long start, long end) {
            this.startTime = new Date(start);
            this.endTime = new Date(end);
            this.platformName = platformName;
        }

        public Date getStartTime() {
            return startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public boolean isEarlier(Estimation other) {
            if (other == null) {
                return true;
            }

            return (this.startTime.before(other.startTime));
        }

        @Override
        public String toString() {
            return "Estimation{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", platformName='" + platformName + '\'' +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

    public Map<Date, Map<Date, Operation>> getScheduleMap() {
        return this.schedule.getScheduleMap();
    }
}