package com.gridnine.testing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Command {

    FLIGHT_BEFORE_NOW("flightbeforenow", "вылет до текущего момента времени", 1),
    FLIGHT_DEPARTURE_AFTER_ARRIVAL("flightdepartureafterarrival", "имеются сегменты с датой прилёта раньше даты вылета", 2),
    FLIGHT_MORE_2_HOURS_ON_GROUND("flightmore2hoursonground", "общее время, проведённое на земле превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)", 3),
    EXIT("exit", "закончить работу", -1),
    HELP("help", "вывести команды", 0),
    NEW_FLIGHTS("newflights", "обновить список полетов", 4);
    private final String name;

    private final String description;
    private final int secondName;

    @Override
    public String toString() {

        return "Type " + name + " or " + secondName + " - " + description;
    }
}
