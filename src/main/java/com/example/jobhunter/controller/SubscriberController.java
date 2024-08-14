package com.example.jobhunter.controller;

import com.example.jobhunter.entity.Subscriber;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    @Autowired
    SubscriberService subscriberService;

    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> createSubcriber(@RequestBody Subscriber req) throws IdInvalidException {
        return ResponseEntity.ok(subscriberService.handlerCreateSubcriber(req));
    }
    @PutMapping("/subscribers")
    public ResponseEntity<Subscriber> updateSubcriber(@RequestBody Subscriber req) throws IdInvalidException{
        return ResponseEntity.ok(subscriberService.handleUpdateSubscriber(req));
    }
}
