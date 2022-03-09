package com.honeygo.app.hackster.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"cam", "time", "name"})
public class Person {
    private String cam;
    private String time;
    private String name;
}
