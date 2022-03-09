package com.honeygo.app.hackster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultDto {
    private D3ForceSimulationJson json;
    private List<PersonDAO> personList;
}
