package com.mega.cicilan.cicilan.Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

import org.apache.catalina.connector.Request;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega.cicilan.cicilan.Models.AsccendResponse;
import com.mega.cicilan.cicilan.Models.MapInput;
import com.mega.cicilan.cicilan.Models.PPMERLHistory;
import com.mega.cicilan.cicilan.Models.Requests;
import com.mega.cicilan.cicilan.Repositories.ChannelRepository;
import com.mega.cicilan.cicilan.Repositories.PPMERLHistoryRepository;
import com.mega.cicilan.cicilan.Repositories.ProductsRepository;
import com.mega.cicilan.cicilan.Repositories.RequestRepository;
import com.mega.cicilan.cicilan.Repositories.RulesRepository;
import com.mega.cicilan.cicilan.Services.AESEncryptDecrypt;
import com.mega.cicilan.cicilan.Services.CheckAndCalculate;
import com.mega.cicilan.cicilan.Services.EncryptAndSign;
import com.mega.cicilan.cicilan.Services.EncryptAndSign1;
import com.mega.cicilan.cicilan.Services.FTPComponents;
import com.mega.cicilan.cicilan.Services.FTPService;
import com.mega.cicilan.cicilan.Services.FileReadWrite;
import com.mega.cicilan.cicilan.Services.HTTPRequest;

@RestController
@RequestMapping("/api/")
public class ApiMainController {

    @Autowired
    public ChannelRepository channelRepository;
    @Autowired
    public ProductsRepository productsRepository;
    @Autowired
    public RequestRepository requestRepository;
    @Autowired
    public RulesRepository rulesRepository;
    @Autowired
    public PPMERLHistoryRepository ppmerlHistoryRepository;
    @Autowired
    public FTPComponents ftpComponents;

    @GetMapping("enc")
    private ResponseEntity<Map> enc() throws Exception {
        EncryptAndSign enc1 = new EncryptAndSign();
        Map hasil = enc1.enc();
        return new ResponseEntity<Map>(hasil, null, 200);
    }

    @GetMapping("dec")
    private String dec(@RequestParam(name = "base64") String base64) throws Exception {
        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        String cKey = "QZgYXcYkZV0e0h5NWTokgJGKGJgaaCA3";
        // System.out.println(base64);
        // base64 = base64.replace(" ", "+");
        // base64 = base64.replaceAll("\\s", "");
        System.out.println(base64);

        // base64 = base64.replace("2b", "+").replace("2f", "/").replace("2d",
        // "-").replace("5f", "_");
        // byte[] decodedBytes = Base64.getUrlDecoder().decode(base64);

        String decrypt = aesEncryptDecrypt.decrypt(base64, cKey, cKey.substring(0,
                16));
        return decrypt;
        // return new ResponseEntity<Map>(hasil, null, 200);
        // return base64;

        // String base64String =
        // "2kpbm5sGt6LOLvly6du2B9rj4y1ZO7IL4nlW0NH4zlzuYKmeNFjnS6a9RM2qph54tmUPlS14PngtYuQiT3BQ1nVN3uW8943o/lhvgvDvm+vJgvOxtpWo4WdHqd6yHnNb";

        // // Decode the Base64 string
        // byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        // String decodedString = new String(decodedBytes);

        // // Print the decoded string
        // System.out.println("Decoded String: " + decodedString);
        // return decodedString;
    }

    public void coba1() throws Exception {
        // Base64 encoded private and public keys
        String privateKeyBase64 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCd0FH6eMmYnSoToKkSGSDdWx2Y1guMWbeAx6VOWH83ARQIOJYMofMyjE+/P4D15t47mfeWjNZ6zAjRljkcQZF4m9v8x7uTfWLAZpvQRppB9hKtH0x76kyCIL/CoWkzGqdVhWYbtTAMPOJNNRLCbgs1VSHYAgHDuaLxN8sJsGJUYyZ88WBwbDuqVM+DvZKMyd+bxUZ0B3u/PxU+ktAUoW7dneyme0mTreagR/aJHrLWMBzxMCCizYKVGchTF4TSJ5yO0qVLQo6kk9+tQLB25bRwYHhe5OWEgy7j6VRkL85XZ+KNi3XyLuvYkTiZDbatp97QNnhkK4e5MBnsqZMPQvIjAgMBAAECggEBAJF17coXwYHZA2qaAJy86qz9ihdQ284e9+RMoRLIwWF6rfMx7SRxtg9Wgz79kqmVhiubrDlpUVxYapx8geGIkkSALNIfa8bzoK1FJsNgAFliLi7ClaX1/VVISFhmnOfM3oYWclqBtGrjR5fwnnz2H4CmGo7WsresiQMb8RraKJgsZPREsOOjcjR95vT/Yn2RXooX9fhw7vbx8YMeG+HpOmegw0MyDc03+v/iXXGSPcUuKkWkI18mZU7IC3CL6XTbNfMmGmdSE++GuidVGrbF7jPd+iMKZo11tXbWDwrFtOVHdVcxkRUBmanhk26MyPIcZ3TnosK8jZlO7XXaVVZs3qkCgYEA0bHHODvQ5rl51huTFvhq7bBhmRaNRvOXbiVqWD4qujmGhbeX/WTsIEFsc0Psso9/Ejy6S/c1K7bUCVIzNffcntyBuEH8jKrYhchYYKUx70exk9zDlshc3QWuADAE9qiHs1zS+X08cbmp8n+rQOIKYge/xVw2hKuePq46d6oSukcCgYEAwKmtmptS8/o97YHa0gvJngG4mKtwRD9b5MxCJmBaN40JUv1b6bwQE/eoiaYM0O2mH34kILode/3ebuQ/CaGq0dUid66kvbeO42SpplQMpvKZHBSAVyOsJ1NXF5BpNYpVupgKoOreXpfoX9Xj/XOdAZKdxBH3OQLCxX7tc4hE20UCgYB+XCa0mDb2EeZ9QviL6v5kZNkrKw/PJZKm6R0AN9X/ihKqGUwZgetYNJRpkRDdOONV1gpdJIaB5RY417PMXaQwhqgUpI7PJVR8qZG4C1Pf4M/9Thb+kZaECZwkuMsE5+F+D/2CIhl+XmcunghQfOXM1OaWw06G+/PLyamp2m6WmQKBgH63Gtd1ETomfFSZ87Hyzsh9mUvQdiZtBaOWNGvJM40V1ewfdzRj3K9FVDQ0YnmH1Jsh++EKiBnk/0cwF8Xd7tvTsrfExdWBxRkKPAqJfUjQhqNRWJf3fmLfDYkSEiCEu53eIh90Wo0cnFlr9GfegoVmSR2MM23rwu7ibIpwNdmBAoGBAIIwJ0hAoAYkZ/fb2nqRynjuXepacdWTHftV8mvdHxPwrhIRP75X7+GXuu3s9t/i+pgShMH98jmF1meM3GwLwWctkN6O54WrROthrwtp2MDQShtC8ShQ1Hr/yZFDLRhXPKhXPIyaIsjhoA4GtuyNFT28m7FIhlVNDdtntAgVE0ov";
        String publicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAndBR+njJmJ0qE6CpEhkg3VsdmNYLjFm3gMelTlh/NwEUCDiWDKHzMoxPvz+A9ebeO5n3lozWeswI0ZY5HEGReJvb/Me7k31iwGab0EaaQfYSrR9Me+pMgiC/wqFpMxqnVYVmG7UwDDziTTUSwm4LNVUh2AIBw7mi8TfLCbBiVGMmfPFgcGw7qlTPg72SjMnfm8VGdAd7vz8VPpLQFKFu3Z3spntJk63moEf2iR6y1jAc8TAgos2ClRnIUxeE0iecjtKlS0KOpJPfrUCwduW0cGB4XuTlhIMu4+lUZC/OV2fijYt18i7r2JE4mQ22rafe0DZ4ZCuHuTAZ7KmTD0LyIwIDAQAB";
        // Decode base64 keys
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // Convert bytes to private and public keys
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        // Message to be encrypted
        String originalMessage = "Hello, RSA Encryption with OAEP Padding!";
        byte[] messageBytes = originalMessage.getBytes();

        // Encryption
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = encryptCipher.doFinal(messageBytes);

        System.out.println("Encrypted message: " + Base64.getEncoder().encodeToString(encryptedBytes));

        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);

        System.out.println("Decrypted message: " + new String(decryptedBytes));
    }

    // @PostMapping("shopeeScore")
    // public ResponseEntity<Map> shopeeScore(@RequestBody Map input) throws
    // Exception {
    // Map result = new TreeMap<>();
    // HTTPRequest httpRequest = new HTTPRequest();
    // JSONObject jsonObject = new JSONObject(input);
    // String url = "https://api-tob.uat.scoring.co.id/openapi/v1/gateway";

    // String http_result = httpRequest.postRequestWithProxy(url,
    // jsonObject.toString(), "10.14.21.65", 8084);
    // JSONObject http_res_json = new JSONObject(http_result);
    // result = http_res_json.toMap();
    // return new ResponseEntity<Map>(result, null, 200);
    // }

    @GetMapping("coba2")
    private Object[] coba2() {
        List<String> coba = new ArrayList<>();
        coba.add("TAI");
        return coba.toArray();
    }

    @GetMapping("cicilan")
    private ResponseEntity<Map> cicilan(@RequestParam(name = "refId") String refId) throws Exception {
        Map hasil = new TreeMap<>();
        HTTPRequest httpRequest = new HTTPRequest();

        Optional<Requests> requestId = this.requestRepository.findById(refId);
        Requests dataIn = requestId.get();
        // System.out.println(dataIn.getCard_nbr());

        if (dataIn.getLoan_status() == null) {
            // List<String> list1 = new ArrayList<>();
            // list1.add(dataIn.getPlan_code());
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(dataIn.getPlan_code());
            String uri = "http://" + this.ftpComponents.getLoc_url() + "/asc/api/SCNMORDP";
            String data = "{\"cardnum\":\"" + dataIn.getCard_nbr() + "\",\"plancode\":"
                    + jsonArray2.toString() + "}";
            System.out.println(uri + "\n");
            System.out.println(data + "\n");
            String responseHasil = httpRequest.postRequest(uri, data);
            ObjectMapper mappers = new ObjectMapper();
            JSONObject jsonObjHasil = new JSONObject(responseHasil);
            JSONArray jsonArray = jsonObjHasil.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                AsccendResponse asccendResponse = mappers.readValue(jsonArray.get(i).toString(), AsccendResponse.class);
                if (asccendResponse.getAuthCode().equals(dataIn.getAuth_code())) {

                    Optional<Requests> rOptional = this.requestRepository.findByCardAuth(dataIn.getCard_nbr(),
                            asccendResponse.getAuthCode());
                    Requests req_data = rOptional.get();
                    req_data.setLoan_status(asccendResponse.getStatus());
                    this.requestRepository.save(req_data);
                    hasil.put("rc", 200);
                    hasil.put("rd", "Berhasil");
                    hasil.put("card_num", req_data.getCard_nbr());
                    hasil.put("ref_num", req_data.getReff_nbr());
                    hasil.put("status", asccendResponse.getStatus());
                }
            }
        } else {
            hasil.put("rc", 200);
            hasil.put("rd", "Berhasil");
            hasil.put("card_num", dataIn.getCard_nbr());
            hasil.put("ref_num", dataIn.getReff_nbr());
            hasil.put("loan_status", dataIn.getLoan_status());
        }
        return new ResponseEntity<Map>(hasil, null, 200);

    }

    @PostMapping("check")
    private ResponseEntity<Map> cek(@RequestBody Map input) {
        // Map map = new HashMap<>();
        // System.out.println(input.toString());
        ModelMapper modelMapper = new ModelMapper();
        MapInput mapInput = modelMapper.map(input, MapInput.class);
        CheckAndCalculate checkAndCalculate = new CheckAndCalculate(mapInput, input, channelRepository,
                productsRepository,
                rulesRepository, requestRepository);
        Map hasil = checkAndCalculate.run();
        return new ResponseEntity<Map>(hasil, null, 200);
        // return input;
    }

    @PostMapping("save")
    private ResponseEntity<Map> save(@RequestBody Requests input) {
        Map hasil = new HashMap<>();
        try {
            AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
            LocalDateTime ldt = LocalDateTime.now();
            String str_ldt = ldt.toString();
            String encString = aesEncryptDecrypt.encrypt(str_ldt, "AIUEOAIUEOAIUEOX", "AIUEOAIUEOAIUEOX");
            input.setId(encString);
            input.setIs_generated(false);
            this.requestRepository.save(input);
            hasil.put("rc", 200);
            hasil.put("rd", "Berhasil menyimpan request");
            hasil.put("req_id", encString);

        } catch (Exception e) {
            hasil.put("rc", 400);
            hasil.put("rd", "Gagal menyimpan request");
            e.printStackTrace();
            return new ResponseEntity<Map>(hasil, null, 400);
        }
        return new ResponseEntity<Map>(hasil, null, 200);
    }

    @GetMapping("generatePPMERL")
    private ResponseEntity<Map> generatePPMERL() {
        Map hasil = new HashMap<>();

        List<Requests> list = this.requestRepository.findByIs_generated(false);

        if (list.isEmpty()) {
            hasil.put("rc", 200);
            hasil.put("rd", "Belum ada data untuk di generate");
            return new ResponseEntity<Map>(hasil, null, 200);
        }
        FileReadWrite frw = new FileReadWrite();
        String hasil_write = frw.ppmrl(list, requestRepository);
        if (hasil_write.contains("FAIL")) {
            hasil.put("rc", 400);
            hasil.put("rd", "Gagal membuat PPMERL");
        } else {
            hasil.put("rc", 200);
            hasil.put("rd", hasil_write);

        }

        String[] namaFileArr = hasil_write.split("'");
        String[] namaFileArr1 = namaFileArr[namaFileArr.length - 1].split("/");
        String namaFile = namaFileArr1[namaFileArr1.length - 1];
        System.out.println("namaFile: " + namaFile);

        PPMERLHistory ppmerlHistory = this.ppmerlHistoryRepository.findByNamafile(namaFile);
        if (ppmerlHistory == null) {
            ppmerlHistory = new PPMERLHistory();
        }

        ppmerlHistory.setDate_created(LocalDate.now());
        ppmerlHistory.setDate_time_created(LocalDateTime.now());
        ppmerlHistory.setNamafile(namaFile);

        for (Requests requests : list) {
            requests.setDate_created(LocalDate.now());
            requests.setDate_time_created(LocalDateTime.now());
            requests.setIs_generated(true);
            requests.setNamafile(namaFile);
        }
        // ppmerlHistory.setIs_sent(false);

        this.ppmerlHistoryRepository.save(ppmerlHistory);
        this.requestRepository.saveAll(list);

        return new ResponseEntity<Map>(hasil, null, (int) hasil.get("rc"));
    }

    @GetMapping("sendPPMERL")
    public ResponseEntity<Map> sendPPMERL() {
        Map hasil = new HashMap<>();
        FTPService ftpService = new FTPService(ftpComponents);
        List<PPMERLHistory> list = this.ppmerlHistoryRepository.findByIs_sent(null);
        List<String> errList = new ArrayList<>();
        System.out.println(list.size());
        if (list.isEmpty()) {
            hasil.put("rc", 200);
            hasil.put("rd", "Belum ada file untuk di kirim");
            return new ResponseEntity<Map>(hasil, null, 200);

        }
        for (PPMERLHistory ppmerlHistory : list) {
            Boolean ftp_send = ftpService.upload(ppmerlHistory.getDate_created().toString(),
                    ppmerlHistory.getNamafile());
            if (ftp_send) {
                ppmerlHistory.setIs_sent(true);
            } else {
                ppmerlHistory.setIs_sent(false);
                errList.add("Error on file '" + ppmerlHistory.getNamafile() + "'");
            }
        }
        this.ppmerlHistoryRepository.saveAll(list);
        hasil.put("rc", 200);
        if (errList.size() == list.size()) {
            hasil.put("rd", "Gagal upload file");
            hasil.put("error", errList);
        } else {
            hasil.put("rd", "Berhasil upload file");
            hasil.put("error", errList);
        }
        return new ResponseEntity<Map>(hasil, null, 200);
    }
}
