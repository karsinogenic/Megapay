package com.mega.cicilan.cicilan.Services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import com.mega.cicilan.cicilan.Models.Requests;
import com.mega.cicilan.cicilan.Repositories.RequestRepository;

public class FileReadWrite {

    // public static int iterPPMERL;
    // public static String oldDate;

    public Boolean writeFilePPMERL(List<Requests> list, String nama_path, String nama_file, String[] arr_date) {
        // File file = new File("src/main/resources/ChannelResponse/write.txt");
        String filler_header = " ".repeat(129);
        String header = "H" + "PPMERL    " + arr_date[0] + arr_date[1] + arr_date[2] + filler_header;

        String footer = "T000000000000";
        Integer pjgList = list.size();
        // System.out.println(filler_header.length());
        footer = footer.substring(0, footer.length() - pjgList.toString().length()) + pjgList;
        footer = footer + " ".repeat(131);

        Integer i = 1;
        String content = "";
        String date_content = arr_date[0].substring(2) + arr_date[1] + arr_date[2];

        for (Requests data : list) {
            String record_type = "D";
            String prog_code = " ".repeat(8);
            String cc_num = " ".repeat(19);
            Integer year = LocalDate.now().getYear();
            String datePost = year.toString() + data.getDate() + data.getTime() + "00";
            String refNumber = data.getReff_nbr().length() < 23
                    ? data.getReff_nbr() + " ".repeat(23 - data.getReff_nbr().length())
                    : data.getReff_nbr();
            String approval_code = data.getAuth_code();
            String description = " ".repeat(40);
            String dana = "0".repeat(12);
            String dp = "0".repeat(12);
            String tenor = "*000";
            // System.out.println("iter :" + iter);
            // System.out.println(data.getReferenceId());
            // Optional<ChannelResponse> cr_list =
            // crr.getByReferenceId(data.getReferenceId());

            // if (cr_list.isPresent() == false) {
            // System.out.println("tidak ada di reference");
            // return false;
            // } else {

            // prog
            prog_code = data.getPlan_code()
                    + prog_code.substring(data.getPlan_code().length(), prog_code.length());

            // cc_num
            try {
                cc_num = data.getCard_nbr()
                        + " ".repeat(cc_num.length() - data.getCard_nbr().length());
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.getMessage() + " : ccnum");
                return false;
            }

            // dana
            try {
                String temp_dana = data.getAmount().toString() + "00";
                // System.out.println((temp_dana.length()) + ":" + temp_dana);
                // System.out.println((dana.length()) + ":" + dana);
                dana = dana.substring(0, (dana.length() - temp_dana.length())) + temp_dana;
            } catch (Exception e) {
                System.out.println("dana");
                return false;
            }

            // approval_code
            approval_code = data.getAuth_code();

            // tenor
            // System.out.println(data.getPlan_code());

            // Integer plan_tenor =
            // pcr.findByPlanCodePPMERL(data.getPlan_code()).get(0).getTenor();
            tenor = tenor.substring(0, (tenor.length() - data.getTenor().toString().length()))
                    + data.getTenor().toString();

            String constant = record_type + prog_code + cc_num + data.getMid() + datePost + refNumber + approval_code
                    + description + dana + dp + tenor;

            i++;
            content += constant + System.getProperty("line.separator");
            // }
        }

        try {
            FileWriter fw = new FileWriter(nama_path + "/" + nama_file, false);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(header + System.getProperty("line.separator") + content + footer);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return false;
        }

    }

    public String ppmrl(List<Requests> list, RequestRepository requestRepository) {
        LocalDate date = LocalDate.now();
        // if (date.toString().equals(oldDate) == false) {
        // iterPPMERL = 0;
        // }
        // iterPPMERL++;
        String nama_folder = date.toString();
        String nama_path = "opt/PPMERL/" + nama_folder;
        String[] arr_date = nama_folder.split("-");

        String nama_file = "PPMERL" + "MP" + "."
                + arr_date[0].substring(2)
                + arr_date[1] + arr_date[2];
        // System.out.println("nama_file = " + nama_file);

        // Optional<ChannelResponse> cr_list = crr.getByReferenceId("amitest8999");
        // System.out.println(data.getCard_nbr());

        try {
            Files.createDirectories(Paths.get(nama_path));
            Boolean wrBool = writeFilePPMERL(list, nama_path, nama_file, arr_date);
            if (wrBool == false) {
                return "FAIL";
            }
            // date = date.plusDays(1);
            // oldDate = date.toString();
            return "Berhasil membuat file cek pada path:'" + nama_path + "/" + nama_file + "'";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "FAIL";
        }

    }

}
