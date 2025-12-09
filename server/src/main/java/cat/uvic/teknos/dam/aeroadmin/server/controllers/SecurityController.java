package cat.uvic.teknos.dam.aeroadmin.server.controllers;

import cat.uvic.teknos.dam.aeroadmin.utilities.security.EncryptionUtils;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.BadRequestException;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class SecurityController {
    private final KeyStore keyStore;

    public SecurityController(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public HandshakeResult generateKeyForClient(String clientId) throws Exception {
        Certificate clientCert = keyStore.getCertificate(clientId);
        if (clientCert == null) {
            throw new BadRequestException("Client not found or not trusted: " + clientId);
        }

        SecretKey secretKey = EncryptionUtils.generateSecretKey();

        String encryptedKey = EncryptionUtils.encryptRSA(secretKey.getEncoded(), clientCert.getPublicKey());

        return new HandshakeResult(secretKey, encryptedKey);
    }

    public static class HandshakeResult {
        public final SecretKey serverKey;
        public final String encryptedClientKey;

        public HandshakeResult(SecretKey serverKey, String encryptedClientKey) {
            this.serverKey = serverKey;
            this.encryptedClientKey = encryptedClientKey;
        }
    }
}