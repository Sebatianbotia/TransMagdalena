package com.example.transmagdalena.utilities.QR;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeGenerator {

    @Value("${qr.size:50}")
    private int size;

    //vamos a generar el QR como array de bytes (ya que el servicio que vamos a usar para subirlo a la nube necesita los bytes
    // no un arcivo fisico en el disco)
    public byte[] generateQRCodeImage(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            //codificamos el contenido en una matriz de bits, como en SSOO
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    size,
                    size
            );

            // matriz->imagen
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Convertir la imagen a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error al generar código QR", e);
        }
    }

    /**
     * Genera el contenido que irá codificado en el QR
     *
     * Formato: TICKET|ID|CODE|TIMESTAMP
     * ¿Por qué este formato? Para poder validar el ticket después escaneando el QR
     */

    public String generateTicketQRContent(Long ticketId, String ticketCode) {
        return String.format("TICKET|%d|%s|%d",
                ticketId,
                ticketCode,
                System.currentTimeMillis()
        );
    }
}
