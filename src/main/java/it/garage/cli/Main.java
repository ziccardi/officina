package it.garage.cli;

import it.garage.Garage;
import it.garage.Platform;
import it.garage.operations.CompletedOperationsStore;
import it.garage.operations.Operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class Main {

    private static String readLine() throws Exception {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private static void printAllPlatforms(Garage grg) {
        grg.getAllPlatforms().forEach( platform -> {
            System.out.println("PLATFORM: " + platform.getName());
            System.out.println("===============================================");
            System.out.println("VEHICLES:");
            Arrays.stream(platform.getAllSupportedVehicles()).forEach(vehicle -> {
                System.out.println("    * " + vehicle);
            });
            System.out.println("SERVICES");
            Arrays.stream(platform.getAllSupportedServices()).forEach(service -> {
                System.out.println("    * " + service);
            });
            System.out.println("\n");
        });
    }

    private static void printDailySchedule(Garage grg) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

        grg.getAllPlatforms().forEach( platform -> {
            System.out.println("PLATFORM: " + platform.getName());
            System.out.println("===============================================");

            Map<Date, Map<Date, Operation>> schedule = platform.getScheduleMap();
            schedule.entrySet().forEach(entry -> {
                System.out.println(dateFormatter.format(entry.getKey()));
                System.out.println("====================================");
                entry.getValue().entrySet().forEach(hourlyEntry -> {
                    Date startTime = hourlyEntry.getKey();
                    Date endTime = new Date(startTime.getTime() + hourlyEntry.getValue().getDuration() * 60000L);
                    System.out.println(String.format("  %tR - %tR %03d %s (%s)", startTime, endTime, hourlyEntry.getValue().getCode(), hourlyEntry.getValue().getOperationType(), hourlyEntry.getValue().getVehicleType()));
                });
            });

            System.out.println("\n");
        });
    }

    private static void insertNewOperation(Garage grg) throws Exception {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        System.out.print("Please enter the vehicle type: ");
        String vehicle = readLine();
        System.out.print("Please enter the operation to do: ");
        String operation = readLine();
        Platform.Estimation estimation = grg.scheduleOperation(vehicle, operation);
        System.out.println(operation + " will be executed provisionally at " + dateFormatter.format(estimation.getStartTime()) + " and will be finished at " + dateFormatter.format(estimation.getEndTime()) + "\n\n");
    }

    private static void completeOperation(Garage grg) throws Exception {
        int code = 0;

        while(true) {
            System.out.print("Please insert the code of the operation to complete: ");
            try {
                code = Integer.parseInt(readLine());
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Please insert an integer value");
            }
        }

        for( Platform platform : grg.getAllPlatforms()) {
            Map<Date, Map<Date, Operation>> schedule = platform.getScheduleMap();

            for (Map<Date, Operation> dailySchedule : schedule.values()) {
                for (Operation op: dailySchedule.values()) {
                    if (op.getCode() == code) {
                        op.complete();
                        System.out.println(String.format("\nOperation %03d - %s (%s) completed now\n", op.getCode(), op.getOperationType(), op.getVehicleType()));
                        return;
                    }
                }
            }

            System.out.println(String.format("\nNo operations with code %03d has been found\n", code));
        };
    }

    private static void printCompletedOperationReport(Garage grg) {
        Map<Date, Operation> report = CompletedOperationsStore.getInstance().getReport();
        if (report.isEmpty()) {
            System.out.println ("No operations have been completed");
            return;
        }

        System.out.println("List of services completed so far:");
        report.entrySet().forEach(entry -> {
            Date endDate = entry.getKey();
            Operation op = entry.getValue();
            System.out.println(String.format("* %tR %s (%s)", endDate, op.getOperationType(), op.getVehicleType()));
        });
        System.out.println();
    }

    private static void printMenu(Garage grg) throws Exception {
        boolean keepRunning = true;

        do {
            System.out.println("1) List all the available platforms and operations");
            System.out.println("2) List the daily schedule for each platform");
            System.out.println("3) Schedule a new operation");
            System.out.println("4) Set an operation as completed");
            System.out.println("5) Print completed operations report");
            System.out.println("6) Exit");
            System.out.println();
            System.out.print("Please make your choice: ");

            String val = readLine();

            try {
                switch(Integer.parseInt(val)) {
                    case 1:
                        printAllPlatforms(grg);
                    case 2:
                        printDailySchedule(grg);
                        break;
                    case 3:
                        insertNewOperation(grg);
                        break;
                    case 4:
                        completeOperation(grg);
                        break;
                    case 5:
                        printCompletedOperationReport(grg);
                        break;
                    case 6:
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("\n<=EE=> Invalid selection\n");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("\n<=EE=> Invalid selection\n");
            } catch (Exception e) {
                System.out.println("\n<=EE=> " + e.getMessage());
            }
        } while(keepRunning);


    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java -jar garage.jar path/toi/config/file.xml");
        }

        File configFile = new File(args[0]);
        if (!configFile.exists() || !configFile.canRead()) {
            System.err.println("Unable to access file " + configFile.getAbsolutePath());
        }

        Garage grg = ConfigLoader.parseConfig(configFile);
        grg.scheduleOperation("CAMION", "CAMBIO FILTRO");
        grg.scheduleOperation("CAMION", "CAMBIO FILTRO");
        grg.scheduleOperation("CAMION", "CAMBIO FILTRO");
        grg.scheduleOperation("CAMION", "CAMBIO FILTRO");
        grg.scheduleOperation("AUTO", "CAMBIO GOMME");
        grg.scheduleOperation("CAMION", "CAMBIO OLIO");
        printMenu(grg);
    }
}
