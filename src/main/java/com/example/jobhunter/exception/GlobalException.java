package com.example.jobhunter.exception;

import com.example.jobhunter.dto.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException idexception){
        RestResponse<Object> res=new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idexception.getMessage());
        res.setMessage(idexception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? String.valueOf(errors) : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    @ExceptionHandler(value = StoreFileException.class)
    public ResponseEntity<RestResponse<Object>> UploadFileException(StoreFileException storeFileException){
        RestResponse<Object> res=new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(storeFileException.getMessage());
        res.setMessage(storeFileException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
