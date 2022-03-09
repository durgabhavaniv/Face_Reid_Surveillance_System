package com.honeygo.app.hackster.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class CamInfoDto {
    private String camLocation;
    private String datetime;
    private MultipartFile video;
    private String[] connections;
}
