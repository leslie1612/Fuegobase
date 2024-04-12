package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/{collectionName}/{documentName}/fields")
public class FieldController {

    private FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    public void createField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                            @PathVariable String projectId,
                            @PathVariable String collectionName,
                            @PathVariable String documentName,
                            @RequestBody FieldData fieldData) {

//        String APIKey = authorization.split(" ")[1].trim();
        String APIKey = "aaa12345bbb";

        fieldService.createField(APIKey, projectId, collectionName, documentName, fieldData);
    }
}
