package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/{collectionName}/{documentName}/fields")
public class FieldController {
    private FieldService fieldService;
    private String API_KEY = "aaa12345bbb";

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    public ResponseEntity<?> createField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @PathVariable String projectId,
                                         @PathVariable String collectionName,
                                         @PathVariable String documentName,
                                         @RequestBody FieldData fieldData) {

//        String APIKey = authorization.split(" ")[1].trim();
        fieldService.createField(API_KEY, projectId, collectionName, documentName, fieldData);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getFields(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                       @PathVariable String projectId,
                                       @PathVariable String collectionName,
                                       @PathVariable String documentName) {
//        System.out.println(fieldService.getFields(API_KEY, projectId, collectionName, documentName));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(fieldService.getFields(API_KEY, projectId, collectionName, documentName)));
    }
}
