package com.honeygo.app.hackster.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@ToString
@Data
public class D3ForceSimulationJson {
    private ArrayList<Node> nodes;
    private ArrayList<Link> links;
}
