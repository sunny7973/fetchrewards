package com.exam.fetchrewards.points.controller;


import com.exam.fetchrewards.points.service.PointServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.exam.fetchrewards.points.model.UserRewards;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    PointServiceV2 service;

    @PostMapping("")
    public ResponseEntity addPoints(@RequestBody UserRewards user){
        if(service.addPoints(user))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/deduct/{deduct-points}")
    public ResponseEntity<String> deductPoints(@PathVariable("deduct-points") Integer points) {
        return new ResponseEntity<String>(service.deductPoints(points).toString(), HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<List<UserRewards>> getUserBalance() {
        return new ResponseEntity<List<UserRewards>>(service.getUserBalance(), HttpStatus.OK);
    }

    @GetMapping("/onlybalance")
    public Map getUserBalancePoints(){
        return service.calculateBalancePoints();
    }

}
