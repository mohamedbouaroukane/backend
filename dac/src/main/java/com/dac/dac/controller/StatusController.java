package com.dac.dac.controller;

import com.dac.dac.entity.Status;
import com.dac.dac.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    @Autowired
    private StatusRepository statusRepo;

    @GetMapping("/")
    private List<Status> getAllStatus(){
        //statusRepo.save(Status.builder().statusLabel("in delivery").build());
        return statusRepo.findAll();
    }
    @GetMapping("/{id}")
    private Status getById(@PathVariable Integer id){
        return statusRepo.findById(id).orElse(null);
    }
}
