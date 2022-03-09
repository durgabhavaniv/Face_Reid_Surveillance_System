package com.honeygo.app.hackster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonDAO {
    private String cam;
    private String startTime;
    private String endTime;
    private String name;
}
