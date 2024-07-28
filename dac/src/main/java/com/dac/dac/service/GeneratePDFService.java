package com.dac.dac.service;

import com.dac.dac.entity.*;
import com.dac.dac.utils.CustomDate;
import com.dac.dac.utils.ManifestModel;
import com.google.zxing.WriterException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class GeneratePDFService {
    @Autowired
    private GenerateBarCodeService generateBarCodeService;

    public byte[] generateManifest(List<ManifestModel> parcels, User user,String from,String to,String title) {
        try{
        JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource("templates/mainfest.jrxml").getInputStream());
        JRBeanCollectionDataSource parcelCollection = new JRBeanCollectionDataSource(parcels);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("params", parcelCollection);
        parameters.put("nowDate", new Date());
        parameters.put("from", from);
        parameters.put("numParcels",parcels.size());
        parameters.put("to", to);
        parameters.put("manifestType", title);
        parameters.put("courierName", user.getFullName());
        parameters.put("courierPhone",user.getPhone());
        parameters.put("courierMail", user.getEmail());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new JREmptyDataSource());

        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("report.pdf"));

        System.out.println("File Generated");
    return JasperExportManager.exportReportToPdf(jasperPrint);
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }

    public byte[] generateParcelLabel(Parcel parcel) throws JRException, IOException, WriterException {
            return JasperExportManager.exportReportToPdf(generatePrintedLabel(parcel));
        }

    public byte[] generateStaticPDF() throws JRException, IOException, WriterException {

        JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource("templates/label.jrxml").getInputStream());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("qrcode",generateBarCodeService.generateQRCode("1323"));
        parameters.put("barcode",generateBarCodeService.generateBarCode128("1324684"));
        parameters.put("traceCode","parcel.getTrackingCode()");
        parameters.put("senderName","parcel.getSender().getFullName()");
        parameters.put("senderEmail","parcel.getSender().getEmail()");
        parameters.put("senderPhone","parcel.getSender().getEmail()");
        parameters.put("receiverName","parcel.getReceiverName()");
        parameters.put("receiverEmail","parcel.getReceiverEmail()");
        parameters.put("receiverPhone","parcel.getReceiverPhone()");
        parameters.put("date","Date: "+ CustomDate.getCurrentDate());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public byte[] generateMultiParcelLabel(List<Parcel> parcels) throws JRException, IOException, WriterException {
        List<JasperPrint> jpList = new ArrayList<>();

        for (Parcel parcel : parcels) {
            jpList.add(generatePrintedLabel(parcel));
        }

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jpList));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            exporter.exportReport();
            return baos.toByteArray();
        }
    }

    private JasperPrint generatePrintedLabel(Parcel parcel) throws JRException, IOException, WriterException {

        JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource("templates/"+getLabelType(parcel)+".jrxml").getInputStream());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("qrcode",generateBarCodeService.generateQRCode(parcel.getLockerCode()));
        parameters.put("barcode",generateBarCodeService.generateBarCode128(parcel.getTrackingCode()));
        parameters.put("traceCode",parcel.getTrackingCode());
        if(parcel instanceof LocalParcel localParcel){
            parameters.put("senderName",localParcel.getSender().getFullName());
            parameters.put("senderEmail",localParcel.getSender().getEmail());
            parameters.put("senderPhone",localParcel.getSender().getPhone());
            if(localParcel.getSenderLocker() != null){
                parameters.put("lockerNumber",""+localParcel.getSenderLocker().getPin());
            }
        }else  if(parcel instanceof InternationalParcel internationalParcel){
            parameters.put("senderCompany",internationalParcel.getSendingCompany());
            parameters.put("senderCountry",internationalParcel.getSendingCountry());
            parameters.put("parcelInternationalTrackingCode",internationalParcel.getInternationalTrackingCode());
        }

        parameters.put("receiverName",parcel.getReceiverName());
        parameters.put("receiverEmail",parcel.getReceiverEmail());
        parameters.put("parcelDetail","("+
                parcel.getParcelDetail().getDimensions().getWidth() +"*"+
                parcel.getParcelDetail().getDimensions().getHeight() +
                       ") /"+
                        parcel.getParcelDetail().getDimensions().getWeight());
        parameters.put("parcelPrice",String.valueOf(parcel.getParcelDetail().getPrice()));
        parameters.put("parcelDescription",parcel.getParcelDescription());
//        Address address = parcel.getReceiverParcelLocker().getAddress();
//        parameters.put("address",address.getPostName() +" " +address.getStateName() +"("+address.getZipCode() +")");


        parameters.put("receiverPhone",parcel.getReceiverPhone());
        parameters.put("date",CustomDate.getCurrentDate());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        return jasperPrint;
    }

    private String getLabelType(Parcel parcel){
        if(parcel instanceof LocalParcel localParcel){
            return "local-label";
        }else if(parcel instanceof InternationalParcel internationalParcel){
            return "international-label";
        }
        return "local-label";
    }

}
