package com.example.ibeproject.controller;

import com.example.ibeproject.service.GraphQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GraphQLController {
    GraphQLService graphQLService;

    @Autowired
    public GraphQLController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }


    @GetMapping("/sampleAPI")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok("testing APIs");
    }

    @GetMapping("/graphQlAPI")
    public ResponseEntity<String> getDataFromGraphQL() {
        return graphQLService.getRooms();
    }
}
