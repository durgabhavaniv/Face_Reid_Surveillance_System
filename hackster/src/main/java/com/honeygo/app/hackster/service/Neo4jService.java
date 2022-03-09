package com.honeygo.app.hackster.service;

import com.honeygo.app.hackster.model.*;
import com.honeygo.app.hackster.util.PersonUtil;
import com.honeygo.app.hackster.util.RecordToPojoMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.beans.factory.annotation.Autowired;
import com.honeygo.app.hackster.config.Neo4jConfig;
import org.neo4j.driver.Result;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Neo4jService implements AutoCloseable {

    @Autowired
    private Neo4jConfig neo4jConfig;
    @Autowired
    private PersonUtil personUtil;

    @Autowired
    private RecordToPojoMapperUtil<NodeData> nodeDataMapper;

    private static AtomicInteger group = new AtomicInteger(1);

    public String createCamNode(String name, String uri, long fileSize, String fileType, String videoName, String startDateTime) {
        List<Record> camNode;
        String matchQuery = "MATCH (cam:cctv) where cam.name='" + name + "' RETURN cam";
        log.info("match query : " + matchQuery);
        String query = "CREATE (n:cctv{name:'" + name + "', videoName:'" + videoName.split("\\.")[0] + "', fileExtension:'"+videoName.split("\\.")[1]+"', uri:'" + uri + "', fileType:'" + fileType + "', fileSize:" + fileSize + ", startDateTime:'" + startDateTime + "', group: " + group.getAndIncrement() + "})";
        log.info("node create query : " + query);
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            boolean check = session.writeTransaction(tx ->
            {
                Result result = tx.run(matchQuery);
                return result.list().size() == 0;
            });
            if (check) {
                camNode = session.writeTransaction(tx ->
                {
                    Result result = tx.run(query);
                    return result.list();
                });
                System.out.println(camNode);
            } else {
                return name + " already exists";
            }
        }
        log.info("result : " + camNode);
        if (group.get() == 20)
            group.set(1);
        return name;
    }

    public List<NodeData> getNodes() {
        List<Record> records = null;
        List<NodeData> nodeList = new ArrayList<>();
        String matchQuery = "MATCH (cam:cctv) RETURN cam";
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            records = session.writeTransaction(tx ->
            {
                Result result = tx.run(matchQuery);
                return result.list();
            });
        }
        nodeList = records.stream().map(record -> nodeDataMapper.valueToPojoMapper(record.get("cam"), new NodeData())).collect(Collectors.toList());
        nodeList.stream().forEach(nodeData -> log.debug("nodeData : " + nodeData));
        return nodeList;
    }

    public void createPath(String nodeName1, String nodeName2) {
        List<Record> records = null;
        String query = "MATCH (cam1:cctv) where cam1.name='" + nodeName1 + "' with cam1  " +
                "MATCH (cam2:cctv) where cam2.name='" + nodeName2 + "' with cam1,cam2  " +
                "MERGE (cam1)-[p:PATH{source:'" + nodeName1 + "', target:'" + nodeName2 + "', value:20 }]-(cam2) RETURN cam1,cam2,p";
        log.info("path query : " + query);
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            records = session.writeTransaction(tx ->
            {
                Result result = tx.run(query);
                return result.list();
            });
        }
        records.forEach(record -> {
            record.values()
                    .forEach(value -> value.asMap()
                            .forEach((s, o) -> log.debug("key : " + s + " value : " + o)));
        });

    }

    public D3ForceSimulationJson getD3nodes() {
        List<Record> records = null;
        String query = "MATCH (n:cctv)-[p:PATH]-() RETURN n,p";
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            records = session.writeTransaction(tx ->
            {
                Result result = tx.run(query);
                return result.list();
            });
        }
        D3ForceSimulationJson json = new D3ForceSimulationJson();
        Set<Node> nodes = new HashSet<>();
        Set<Link> links = new HashSet<>();
        records.forEach(record -> {
//            record.values().forEach(value -> value.asMap().forEach((s, o) -> System.out.println("key : " + s + " value : " + o)));
            Value cam = record.get("n");
            Node node = new Node(cam.get("name").asString(), cam.get("group").asInt());
            log.debug("node : " + node);
            nodes.add(node);
            Value path = record.get("p");
            Link link = new Link(path.get("source").asString(), path.get("target").asString(), path.get("value").asInt());
            log.debug("link : " + link);
            links.add(link);
        });
        json.setNodes(new ArrayList<>(nodes));
        json.setLinks(new ArrayList<>(links));
        log.debug("data d3 : " + json);
        return json;
    }

    public String getNodeName() {
        List<Record> records = null;
        List<String> nodeName = new ArrayList<>();
        String matchQuery = "MATCH (cam:cctv) RETURN cam";
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            records = session.writeTransaction(tx ->
            {
                Result result = tx.run(matchQuery);
                return result.list();
            });
        }
//        records.forEach(record -> {
//            record.values().forEach(value -> value.asMap().forEach((s, o) -> System.out.println("key : "+s+" value : "+o)));
//            record.keys().forEach(s -> System.out.println("key: "+s));
//            record.values().forEach(value -> System.out.println("value: "+value.get("name")));});
        records.forEach(record -> record.values().forEach(value -> nodeName.add(0, value.get("name").asString())));
        log.debug("node list : " + Arrays.toString(nodeName.toArray(new String[0])));
        return String.join(",", nodeName.toArray(new String[0]));

    }

    public String addPerson(PersonDAO person) {

        String matchQuery = "MERGE (p:person{name:'" + person.getName() + "'}) with p  " +
                "MATCH (cam:cctv) where cam.videoName='" + person.getCam() + "' with cam,p " +
                "MERGE (p)-[:TIME {startTime:'" + person.getStartTime() + "' , " +
                "endTime:'"+person.getEndTime()+"'}]->(cam)";
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            session.writeTransaction(tx ->
            {
                Result result = tx.run(matchQuery);
                return result.list();
            });
        } catch (Exception e) {
            e.getStackTrace();
            return "failed to insert person";
        }
        return "person added";
    }

    public ResultDto findPersonD3(String name) {
        String matchQuery = "MATCH (p:person) where p.name='" + name + "'with p " +
                "MATCH (p)-[t:TIME]->(c:cctv) return p,t,c";
        List<Record> records = null;
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            records = session.writeTransaction(tx ->
            {
                Result result = tx.run(matchQuery);
                return result.list();
            });
        }
        D3ForceSimulationJson json = new D3ForceSimulationJson();
        Set<Node> nodes = new HashSet<>();
        Set<Link> links = new HashSet<>();
        records.forEach(record -> {
//            record.values().forEach(value -> value.asMap().forEach((s, o) -> System.out.println("key : " + s + " value : " + o)));
            Value cam = record.get("c");
            Node node1 = new Node(cam.get("name").asString(), cam.get("group").asInt());
            log.debug("node : " + node1);
            nodes.add(node1);
            Value person = record.get("p");
            Node node2 = new Node(person.get("name").asString(), 19);
            log.debug("node : " + node2);
            nodes.add(node2);
            Link link = new Link(person.get("name").asString(), cam.get("name").asString(), 20);
            log.debug("link : " + link);
            links.add(link);
        });
        json.setNodes(new ArrayList<>(nodes));
        json.setLinks(new ArrayList<>(links));
        log.debug("data d3 : " + json);
        List<PersonDAO> personList = records.stream().map(record -> new PersonDAO(record.get("c").get("name").asString(),
                record.get("t").get("startTime").asString(),
                record.get("t").get("endTime").asString(),
                record.get("p").get("name").asString())).collect(Collectors.toList());
        Collections.sort(personList, Comparator.comparing(p -> personUtil.parseString(p.getStartTime())));
        personList.forEach(person -> log.debug("person : "+person));
        return new ResultDto(json,
                personList);
    }

    public ResultDto findCamD3(String name) {
        String matchQuery = "MATCH (c:cctv) where c.name='" + name + "' with c " +
                "MATCH (p:person)-[t:TIME]->(c) return p,t,c";
        List<Record> records = null;
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            records = session.writeTransaction(tx ->
            {
                Result result = tx.run(matchQuery);
                return result.list();
            });
        }
        D3ForceSimulationJson json = new D3ForceSimulationJson();
        Set<Node> nodes = new HashSet<>();
        Set<Link> links = new HashSet<>();
        records.forEach(record -> {
//            record.values().forEach(value -> value.asMap().forEach((s, o) -> System.out.println("key : " + s + " value : " + o)));
            Value cam = record.get("c");
            Node node1 = new Node(cam.get("name").asString(), cam.get("group").asInt());
            log.debug("node : " + node1);
            nodes.add(node1);
            Value person = record.get("p");
            Node node2 = new Node(person.get("name").asString(), 19);
            log.debug("node : " + node2);
            nodes.add(node2);
            Link link = new Link(person.get("name").asString(), cam.get("name").asString(), 20);
            log.debug("link : " + link);
            links.add(link);
        });
        json.setNodes(new ArrayList<>(nodes));
        json.setLinks(new ArrayList<>(links));
        log.debug("data d3 : " + json);
        List<PersonDAO> personList = records.stream().map(record -> new PersonDAO(record.get("c").get("name").asString(),
                record.get("t").get("startTime").asString(),
                record.get("t").get("endTime").asString(),
                record.get("p").get("name").asString())).collect(Collectors.toList());
        Collections.sort(personList, Comparator.comparing(p -> personUtil.parseString(p.getStartTime())));
        personList.forEach(person -> log.debug("person : "+person));

        return new ResultDto(json, personList);
    }

    public void deleteAllData(){
        String matchQuery = "MATCH (n) with n MATCH p=()-->() with n,p DELETE n,p";
        try (Session session = neo4jConfig.neo4jDriver().session()) {
            session.writeTransaction(tx -> tx.run(matchQuery));
        }
    }

    @Override
    public void close() throws Exception {
        neo4jConfig.neo4jDriver().close();
    }
}
