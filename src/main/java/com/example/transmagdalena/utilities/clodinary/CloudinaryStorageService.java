package com.example.transmagdalena.utilities.clodinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryStorageService {

        @Value("${cloudinary.cloud-name}")
        private String cloudName;

        @Value("${cloudinary.api-key}")
        private String apiKey;

        @Value("${cloudinary.api-secret}")
        private String apiSecret;

        private Cloudinary cloudinary;

        private Cloudinary getCloudinary() {
            if (cloudinary == null) {//inicializacion del servicio en la nube con mis credenciales
                cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret
                ));
            }
            return cloudinary;
        }

        /**
         *
         *
         * @param qrCodeBytes Los bytes de la imagen PNG del QR
         * @param ticketCode El código del ticket (para nombrar el archivo)
         * @return URL pública del QR (ej: https://res.cloudinary.com/...)
         */
        public String uploadQRCode(byte[] qrCodeBytes, String ticketCode) {
            try {
                // Nombre unico para el archivo
                // Ejemplo: tickets/qr-TKT-20241110-ABC123-uuid
                String publicId = String.format("tickets/qr-%s-%s", // tiene formato /QR-String-String
                        ticketCode,//codigo del tiquete generado en el service
                        UUID.randomUUID().toString().substring(0, 8)//genera 8 caracteres aleatorios
                );

                // Subir a la nube
                Map uploadResult = getCloudinary().uploader().upload(
                        qrCodeBytes,//primer argumento: bytes (primer parametro)
                        ObjectUtils.asMap(//creamos un map (llave- valor)
                                "public_id", publicId,//"public_id", "tickets/qr-TKT-20241110-ABC123-a3f8b2c1" por ejemplo
                                "folder", "qr-codes", // Carpeta en Cloudinary
                                "resource_type", "image"//aja xd
                        )
                );// al final, uploadResult va a tener informacion del archivo suibido inclutendo dos url publicas, una http y otra https que es la que vamos a usar
                //esar url publica es la que vamos a retornar
                //retorno de url

                return (String) uploadResult.get("secure_url");//sube el QR a Cloudinary y retorna la Uurl publica

            } catch (IOException e) {// por si se estalla
                throw new RuntimeException("Error al subir QR a Cloudinary", e);
            }
        }

        /**
         * Elimina el QR de Cloudinary cuando se elimina el ticket
         */
        public void deleteQRCode(String qrCodeUrl) {
            try {
                String publicId = extractPublicId(qrCodeUrl);
                getCloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());//simplemente borrar
            } catch (IOException e) {
                System.err.println("Error eliminando QR: " + e.getMessage());
            }
        }

        /**
         * Extrae el public_id del URL de Cloudinary
         * URL: https://res.cloudinary.com/CLOUD/image/upload/v123/qr-codes/qr-TKT-ABC.png
         * Public ID: qr-codes/qr-TKT-ABC
         */
        private String extractPublicId(String url) {
            String[] parts = url.split("/");//esto es para  el elete
            String filename = parts[parts.length - 1].replace(".png", "");
            return "qr-codes/" + filename;
        }

}
