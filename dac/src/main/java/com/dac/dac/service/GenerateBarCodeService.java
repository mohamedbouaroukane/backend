package com.dac.dac.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateBarCodeService {

    private final int QR_IMAGE_WIDTH = 600;
    private final int QR_IMAGE_HEIGHT = 600;

    private final int BAR_CODE_IMAGE_WIDTH = 444;
    private final int BAR_CODE_IMAGE_HEIGHT = 60;

    public InputStream generateQRCode(String text) throws IOException, WriterException {
        return generateBarCodeImage(BarcodeFormat.QR_CODE,text,QR_IMAGE_WIDTH,QR_IMAGE_HEIGHT);
    }

    public InputStream generateBarCode128(String text) throws IOException, WriterException {
        return generateBarCodeImage(BarcodeFormat.CODE_128,text,BAR_CODE_IMAGE_WIDTH,BAR_CODE_IMAGE_HEIGHT);
    }

    private InputStream generateBarCodeImage(BarcodeFormat format,String text,int width,int height) throws WriterException, IOException {

        Map<EncodeHintType,Object> hints = new HashMap<>();
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, format, width, height,hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());

    }

}
