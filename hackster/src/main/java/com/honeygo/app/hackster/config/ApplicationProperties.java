package com.honeygo.app.hackster.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "file")
public class ApplicationProperties {
    private String uploadDir;
    private String outDir;
    private String dockerInputFilePath;
    private String dockerOutputFilePath;
    private String dockerOutputPath;
    private String projectDir;
}
