package com.honeygo.app.hackster.service;

import com.honeygo.app.hackster.config.ApplicationProperties;
import com.honeygo.app.hackster.model.NodeData;
import com.honeygo.app.hackster.model.Person;
import com.honeygo.app.hackster.util.PersonUtil;
import lombok.Value;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class CommandExecuter {

    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private Neo4jService neo4jService;

    public String processCommand(String command){
    String output = "";
    String s = "";
        try {
            log.info("command: "+command);
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                output += s + "<br>";
            }

            if ((s = stdError.readLine()) != null) {
                log.error("Here is the standard error of the command (if any):\n");
                log.error(s);
                while ((s = stdError.readLine()) != null) {
                    log.error(s);
                }
            }

        }catch (Exception e){
            e.getStackTrace();
        }
        return output;
    }

    public String callVideoPipeline(String videoName){
        String output = processCommand("/home/durga/jaggu/neo4j/hackster/processVideo.sh "+videoName);
        return output;
    }

}
