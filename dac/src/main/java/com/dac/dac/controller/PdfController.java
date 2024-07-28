package com.dac.dac.controller;

import com.dac.dac.service.GenerateBarCodeService;
import com.dac.dac.service.GeneratePDFService;
import com.dac.dac.utils.ManifestModel;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/qrcode")
public class PdfController {

    @Autowired
    GeneratePDFService pdfService = new GeneratePDFService();

    @Autowired
    private GenerateBarCodeService generateBarCodeService;

    @Autowired
    private GeneratePDFService generatePDFService;
    /*@GetMapping("/")
    public ResponseEntity getPdf() throws Exception {
        byte[] pdfBytes = pdfService.generatePDF();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }*/

   /* @GetMapping
    public ResponseEntity<String> generateQRCode() {
        try{
            return new ResponseEntity<>(convertToBase64(generateBarCodeService.generateBarCode128("mohamed")), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    public static String convertToBase64(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Read the input stream into the byte array output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // Get the byte array from the byte array output stream
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Encode the byte array to Base64
        String base64String = Base64.getEncoder().encodeToString(byteArray);

        return base64String;
    }*/

//    @GetMapping("/pdf")
//    public ResponseEntity generatePDF() throws JRException, IOException {
//        List<ManifestModel> list = new ArrayList<>();
//        byte[] pdfBytes = generatePDFService.generateManifest();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .contentLength(pdfBytes.length)
//                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
//    }

}
