package com.dac.dac.controller;

import com.dac.dac.service.EmailService;
import com.dac.dac.service.GeneratePDFService;
import com.google.zxing.WriterException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private GeneratePDFService generatePDFService;

    @GetMapping("/")
    public void sendMail() throws JRException, IOException, WriterException {
        emailService.sendParcelLabelAttachmentsEmail("mohamed.bouaroukane@univ-biskra.dz","pdf",generatePDFService.generateStaticPDF());
    }
}
