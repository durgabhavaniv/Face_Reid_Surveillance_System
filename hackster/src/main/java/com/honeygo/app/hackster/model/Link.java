package com.honeygo.app.hackster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Link {
    private String source;
    private String target;
    private int value;

}
