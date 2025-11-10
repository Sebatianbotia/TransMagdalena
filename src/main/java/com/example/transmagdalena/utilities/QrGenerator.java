package com.example.transmagdalena.utilities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.Map;

public class QrGenerator {

    public static void main(String[] args) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode("yeilin", BarcodeFormat.QR_CODE, 20, 20);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        File qrCodeFile = new File("dhdh/qrcodess.jpg");
        if (!Files.exists(qrCodeFile.toPath())) {
            Files.createDirectories(qrCodeFile.toPath());
        }
        ImageIO.write(image, "PNG", qrCodeFile);




    }
}
