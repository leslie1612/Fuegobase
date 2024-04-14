package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.FieldKeyData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections/{collectionId}/documents/{documentId}/fields")
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
                                         @PathVariable String collectionId,
                                         @PathVariable String documentId,
                                         @RequestBody FieldData fieldData) {

//        String APIKey = authorization.split(" ")[1].trim();
        fieldService.createField(API_KEY, projectId, collectionId, documentId, fieldData);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> getFields(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                       @PathVariable String projectId,
                                       @PathVariable String collectionId,
                                       @PathVariable String documentId) {
//        System.out.println(fieldService.getFields(API_KEY, projectId, collectionName, documentName));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(fieldService.getFields(API_KEY, projectId, collectionId, documentId)));
    }

    // update field info
//    @PatchMapping("/{fieldId}")
//    public ResponseEntity<?> renameField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
//                                         @PathVariable String projectId,
//                                         @PathVariable String collectionId,
//                                         @PathVariable String documentId,
//                                         @PathVariable String fieldId,
//                                         @RequestBody FieldKeyData updateKeyData) {
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new GenericResponse<>(fieldService.renameField(API_KEY, projectId, collectionId, documentId, fieldId, updateKeyData)));
//    }

    @PostMapping("/{fieldId}")
    public ResponseEntity<?> postField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                       @PathVariable String projectId,
                                       @PathVariable String collectionId,
                                       @PathVariable String documentId,
                                       @PathVariable String fieldId,
                                       @RequestBody ValueInfoData valueInfoData) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(fieldService.postField(API_KEY, projectId, collectionId, documentId, fieldId, valueInfoData)));

    }
    // add new row of field which type is array or map


    // update a value key or value value for one row of field
}
