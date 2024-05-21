package ConfHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class SuccessJsonResponse extends ResponseEntity<Object> {
    public SuccessJsonResponse(Object object) {
        super(object, HttpStatus.OK);
    }


}
