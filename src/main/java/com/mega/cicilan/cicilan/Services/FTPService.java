package com.mega.cicilan.cicilan.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPService {
    public FTPComponents ftpComponents;

    public FTPService(FTPComponents ftpComponents) {
        this.ftpComponents = ftpComponents;
    }

    public Boolean upload(String tanggal, String namafile) {
        String path = "opt/PPMERL/" + tanggal + "/" + namafile;
        String upload_path = ftpComponents.getFtp_dir();
        Boolean hasil = uploadFunc(path, upload_path);
        return hasil;
    }

    public Boolean uploadFunc(String filePath, String remoteDir) {
        Boolean hasil = false;
        FTPClient ftpClient = new FTPClient();
        String[] filePathArr = filePath.split("/");
        String remoteFileName = filePathArr[filePathArr.length - 1];
        // String ural =
        try {
            ftpClient.connect(ftpComponents.getFtp_url(), ftpComponents.getFtp_port());
            ftpClient.login(ftpComponents.getFtp_username(), ftpComponents.getFtp_password());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File localFile = new File(filePath);
            FileInputStream inputStream = new FileInputStream(localFile);

            ftpClient.changeWorkingDirectory(remoteDir);
            boolean uploaded = ftpClient.storeFile(remoteFileName, inputStream);
            inputStream.close();

            if (uploaded) {
                System.out.println("File uploaded successfully.");
                hasil = true;
            } else {
                System.out.println("Error uploading file.");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return hasil;
    }

}
