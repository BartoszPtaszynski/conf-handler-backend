package ConfHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class SuccessJsonResponse extends ResponseEntity<Map<String,Object>> {
    public SuccessJsonResponse(Object object) {
        super( Map.of("response",object), HttpStatus.OK);
    }


}
