package com.honeygo.app.hackster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NodeData {
    private String name;
    private String videoName;
    private String uri;
    private String fileType;
    private String startDateTime;
    private String fileExtension;

//    public String getVideoNameOnly(){
//        return videoName.split("\\.")[0];
//    }
}
