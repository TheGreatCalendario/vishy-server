package org.tbk.vishy.web;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH, method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> error() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\n" +
                        "  \"error\": true,\n" +
                        "  \"message\": \"That did not work.\"\n" +
                        "}");
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}