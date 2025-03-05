package ConfHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessJsonResponse extends ResponseEntity<Object> {
    public SuccessJsonResponse(Object object) {
        super(object, HttpStatus.OK);
    }


}
