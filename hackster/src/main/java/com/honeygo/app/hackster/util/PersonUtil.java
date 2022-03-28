package com.honeygo.app.hackster.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.honeygo.app.hackster.config.ApplicationProperties;
import com.honeygo.app.hackster.model.NodeData;
import com.honeygo.app.hackster.model.Person;
import com.honeygo.app.hackster.model.PersonDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

@Slf4j
@Service
public class PersonUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ApplicationProperties properties;

    public List<Person> processCSVOutputFile(){

        try {
            Reader csvFile = new FileReader(properties.getDockerOutputFilePath());
            MappingIterator<Person> personIter = new CsvMapper()
                    .readerWithTypedSchemaFor(Person.class)
                    .readValues(csvFile);

            return personIter.readAll();
        } catch (IOException e) {
            log.error("failed with error message : "+e.getMessage());
        }
        return new ArrayList<>();
    }

    public String praceSecToDateTime(String sec, String cam, String dateTime){
        return parseString(dateTime)
                    .plusSeconds(Long.parseLong(sec)).format(formatter);
    }



    public List<PersonDAO> updatePersonDateTime(Map<String, List<Person>> personMap, List<NodeData> nodeDataList){
        Map<String,NodeData> dataMap = nodeDataList.parallelStream()
                .collect(Collectors.toMap(NodeData::getVideoName, Function.identity()));
        List<PersonDAO> personDAOList = new ArrayList<>();
        personMap.forEach((s, personList) -> {
            Collections.sort(personList, Comparator.comparing(p -> Long.parseLong(p.getTime())));
            personList.forEach(person -> log.info("person : "+person));
            AtomicReference<String> startTime = new AtomicReference<>(personList.get(0).getTime());
            AtomicReference<String> endTime = new AtomicReference<>(personList.get(0).getTime());
            AtomicReference<String> cam = new AtomicReference<>(personList.get(0).getCam());
            for(Person person : personList){
                if(!person.getCam().equals(cam.get())){
                    log.info("cam :"+cam.get()+" person cam: "+person.getCam()+" not matched");
                    personDAOList.add(new PersonDAO(cam.get(),
                            praceSecToDateTime(startTime.get(),
                                    person.getCam(),
                                    dataMap.get(person.getCam()).getStartDateTime()),
                            praceSecToDateTime(endTime.get(),
                                    person.getCam(),
                                    dataMap.get(person.getCam()).getStartDateTime()),
                            person.getName()));
                    cam.set(person.getCam());
                    startTime.set(person.getTime());
                    endTime.set(person.getTime());
                }else{
                    endTime.set(person.getTime());
                }
            };
            personDAOList.add(new PersonDAO(cam.get(),
                    praceSecToDateTime(startTime.get(),
                            cam.get(),
                            dataMap.get(cam.get()).getStartDateTime()),
                    praceSecToDateTime(endTime.get(),
                            cam.get(),
                            dataMap.get(cam.get()).getStartDateTime()),
                    s));
        });
        personDAOList.forEach(personDAO -> log.info("personDao : "+personDAO));
        return personDAOList;
    }

    public LocalDateTime parseString(String datetime){
        return LocalDateTime
                .parse(datetime, formatter);
    }

    public void deleteOldFileIfExists(){
        File file = new File(properties.getDockerOutputPath());
        try {
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
