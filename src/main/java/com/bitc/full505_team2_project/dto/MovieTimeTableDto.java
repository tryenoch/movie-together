package com.bitc.full505_team2_project.dto;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class MovieTimeTableDto {
    String hall;
    String totalSeat;
    LinkedHashMap<String, String> startTimeAndSeatRemain = new LinkedHashMap<>();

    public void addTimeAndSeat(String time, String seat) {
        this.startTimeAndSeatRemain.put(time, seat);
    }
}
