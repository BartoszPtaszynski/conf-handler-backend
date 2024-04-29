package ConfHandler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ErrorJsonResponse extends ResponseEntity<Map<String,Object>> {
    public ErrorJsonResponse(Object object, HttpStatusCode status) {
        super( Map.of("error",object), status);
    }


}
