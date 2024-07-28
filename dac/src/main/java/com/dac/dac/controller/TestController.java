package com.dac.dac.controller;

import com.dac.dac.service.InternationalParcelService;
import com.dac.dac.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ParcelService parcelService;
    @GetMapping
    public void test() {
        parcelService.getAllParcelStatics();
    }
}
