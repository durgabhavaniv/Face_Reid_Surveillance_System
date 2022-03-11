package com.honeygo.app.hackster.controller;

import com.honeygo.app.hackster.model.D3ForceSimulationJson;
import com.honeygo.app.hackster.model.NodeData;
import com.honeygo.app.hackster.model.Person;
import com.honeygo.app.hackster.model.ResultDto;
import com.honeygo.app.hackster.service.CommandExecuter;
import com.honeygo.app.hackster.service.FileStorageService;
import com.honeygo.app.hackster.service.Neo4jService;
import com.honeygo.app.hackster.util.PersonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AppController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Neo4jService neo4jService;
    @Autowired
    private CommandExecuter commandExecuter;
    @Autowired
    private PersonUtil personUtil;


    @GetMapping("/cams")
    public String getCamNames() {
        return neo4jService.getNodeName();
    }

    @GetMapping("/camData")
    public List<NodeData> getCamData() {
        return neo4jService.getNodes();
    }

    @GetMapping("/camsGraph")
    public D3ForceSimulationJson getD3Json() {
        return neo4jService.getD3nodes();
    }

    @GetMapping("/deleteAll")
    public void deleteAllNodes() {
        neo4jService.deleteAllData();
    }

    @GetMapping("/processVideos")
    public String addAllPerson() {
        personUtil.deleteOldFileIfExists();
        List<NodeData> dataList = neo4jService.getNodes();
        dataList.parallelStream().forEach(nodeData -> {
            CompletableFuture<String> future
                    = CompletableFuture.supplyAsync(() -> commandExecuter
                    .callVideoPipeline(nodeData.getVideoName() + "." + nodeData.getFileExtension()));

            future.join();
            log.info(" execution details : " + future.isDone());
        });
        List<Person> personList = personUtil.processCSVOutputFile();
        Map<String, List<Person>> personMap = new HashMap<>();
        for (Person person : personList) {
            if (!personMap.containsKey(person.getName())) {
                personMap.put(person.getName(), new ArrayList<>());
            }
            personMap.get(person.getName()).add(person);
        }
        personUtil.updatePersonDateTime(personMap, dataList)
                .stream()
                .forEach(person -> neo4jService.addPerson(person));
        return "Processing is done and ready for search";
    }

    @GetMapping("/findPerson/{person}")
    public ResultDto findPersonByName(@PathVariable("person") String person) {
        return neo4jService.findPersonD3(person);
    }

    @GetMapping("/findCam/{cam}")
    public ResultDto findCamByName(@PathVariable("cam") String cam) {
        return neo4jService.findCamD3(cam);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCam(@RequestParam("video") MultipartFile video,
                                            @RequestParam("camLocation") String camLocation,
                                            @RequestParam("datetime") String datetime,
                                            @RequestParam("connections") String[] connections) {
//        log.info("video: "+video);
//        log.info("camLocation: "+camLocation);
//        log.info("datetime: "+datetime);
        log.info("connections: " + Arrays.toString(connections));
        String videoName = fileStorageService.storeFile(null, video);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(videoName)
                .toUriString();

        String responce = neo4jService.createCamNode(camLocation, fileDownloadUri, video.getSize(), video.getContentType(), videoName, datetime);
        Arrays.stream(connections).forEach(connection -> neo4jService.createPath(camLocation, connection));
        return new ResponseEntity<>(responce, HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(null, fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/process")
    public ResponseEntity<String> processVideoForFaceReid() {
        List<NodeData> nodeData = neo4jService.getNodes();
        nodeData.parallelStream().forEach(node -> commandExecuter.callVideoPipeline(node.getVideoName()));
        return new ResponseEntity<>("processed", HttpStatus.OK);
    }

}
