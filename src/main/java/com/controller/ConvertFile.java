package com.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertFile {
    public static void main(String[] args) throws IOException {
        String dir = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\test\\unit_dim1.csv";
        String src1 = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\test\\businessinsider-20221030091450.csv";
        String src2 = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\test\\tradingeconomics-20221030084146.csv";
//        File sfile = new File(src);
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dir, true)));
        List<String> arr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(src1))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!arr.contains(values[5].toLowerCase().trim())) arr.add(values[5].toLowerCase().trim());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(src2))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!arr.contains(values[5].toLowerCase().trim())) arr.add(values[5].toLowerCase().trim());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String str : arr) {
            writer.println(str);
            writer.flush();
        }

//        File dirFile = new File(dir);
//        for (File file : dirFile.listFiles()) {
//            String path = file.getPath().replaceAll("new", "new1");
//            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
//            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String[] values = line.split(",");
//                    String str = null;
//                    for (String[] arr : units) {
//                        if (values[0].equalsIgnoreCase(arr[0])) {
//                            str = values[0] + "," + values[1] + "," + values[2] + "," + values[3] + "," + arr[1] + "," + values[4];
//                            writer.println(str);
//                            writer.flush();
//                        }
//                    }
//                    if (str == null) {
//                        str = values[0] + "," + values[1] + "," + values[2] + "," + values[3] + "," + "," + values[4];
//                        System.out.println(str);
//                        writer.println(str);
//                        writer.flush();
//                    }
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
