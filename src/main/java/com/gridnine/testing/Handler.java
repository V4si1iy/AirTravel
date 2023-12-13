package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Handler {
    private final Map<Command, UnaryOperator<List<Flight>>> commandExecute = new HashMap<>();
    boolean flag = false;

    public Handler() {
        commandExecute.put(Command.FLIGHT_BEFORE_NOW, this::flightBeforeNow); // Добавление команд в хранилище (новые делать по примеру)
        commandExecute.put(Command.FLIGHT_DEPARTURE_AFTER_ARRIVAL, this::flightDepartureAfterArrival);
        commandExecute.put(Command.FLIGHT_MORE_2_HOURS_ON_GROUND, this::flightMore2HoursOnGround);
        commandExecute.put(Command.EXIT, this::exit);
        commandExecute.put(Command.HELP, this::help);
        commandExecute.put(Command.NEW_FLIGHTS, this::newFlights);
    }

    public void handle(List<Flight> flights) {
        String userCommand;
        Command[] commands = Command.values();
        try (Scanner in = new Scanner(System.in)) {
            System.out.println(Command.HELP);
            do {
                System.out.print("/");
                userCommand = in.nextLine().trim();
                for (Command command : commands) {
                    if ((command.name().toLowerCase()).equals(userCommand) || command.getSecondName() == Integer.parseInt(userCommand)) {
                        flights = commandExecute.get(command).apply(flights);
                        break;
                    }
                }
                if (!(Command.HELP.name().toLowerCase()).equals(userCommand) && Command.HELP.getSecondName() != Integer.parseInt(userCommand) && flag != true)
                    System.out.println(flights);
            } while (flag != true);
        }
    }

    public List<Flight> flightBeforeNow(List<Flight> flights) {
        return flights.parallelStream()
                .filter(e -> e.getSegments()
                        .get(0)
                        .getDepartureDate()
                        .isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

    }

    public List<Flight> flightDepartureAfterArrival(List<Flight> flights) {
        List<Flight> newFlights = new ArrayList<>();
        List<Segment> checkSegment;
        for (Flight flight : flights) {
            checkSegment = flight.getSegments();
            checkSegment = checkSegment.parallelStream()
                    .filter(e -> e.getDepartureDate()
                            .isAfter(e.getArrivalDate()))
                    .collect(Collectors.toList());
            if (checkSegment.size() != 0) {
                newFlights.add(flight);
            }
        }
        return newFlights;
    }

    public List<Flight> flightMore2HoursOnGround(List<Flight> flights) {
        List<Flight> newFlights = new ArrayList<>();
        LocalDateTime lastArrival;
        long hours;
        for (Flight flight : flights) {
            hours = 0;
            lastArrival = flight.getSegments().get(0).getArrivalDate();

            for (Segment segment : flight.getSegments()) {
                if (segment.getArrivalDate().equals(lastArrival) || lastArrival.isAfter(segment.getDepartureDate()))
                    continue;
                Duration duration = Duration.between(lastArrival, segment.getDepartureDate());
                hours = hours + duration.toHours();
                if (hours > 2) {
                    newFlights.add(flight);
                    break;
                }
                lastArrival = segment.getArrivalDate();
            }
        }
        return newFlights;
    }

    private List<Flight> exit(List<Flight> flights) {
        flag = true;
        return flights;
    }

    private List<Flight> help(List<Flight> flights) {
        for (Command command : Command.values()) {
            System.out.println(command.toString());
        }
        return flights;
    }

    private List<Flight> newFlights(List<Flight> flights) {
        return FlightBuilder.createFlights();
    }
}
