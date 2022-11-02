//package com.controller;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class ConvertFile {
//    public static void main(String[] args) throws IOException {
//        String src1 = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\test";
//        String src2 = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\businessinsider-20221102051403.csv";
////        File sfile = new File(src);
////        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dir, true)));
////        List<String> arr = new ArrayList<>();
////        try (BufferedReader br = new BufferedReader(new FileReader(src1))) {
////            String line;
////            while ((line = br.readLine()) != null) {
////                String[] values = line.split(",");
////                if (!arr.contains(values[5].toLowerCase().trim())) arr.add(values[5].toLowerCase().trim());
////            }
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////        try (BufferedReader br = new BufferedReader(new FileReader(src2))) {
////            String line;
////            while ((line = br.readLine()) != null) {
////                String[] values = line.split(",");
////                if (!arr.contains(values[5].toLowerCase().trim())) arr.add(values[5].toLowerCase().trim());
////            }
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////        for (String str : arr) {
////            writer.println(str);
////            writer.flush();
////        }
//
//        File sfile = new File(src2);
//        List<String[]> list = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(sfile))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(",");
//                list.add(values);
//            }
//        }
//        File dir = new File(src1);
//        for (File file : dir.listFiles()) {
//            String path = file.getPath().replaceAll("test", "data-2");
//            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
//            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String[] values = line.split(",");
//                    for (String[] vals : list) {
//                        if (vals[1].equalsIgnoreCase(values[1])) {
//                            String str = vals[0] + "," + values[1] + "," + values[2] + "," + values[3] + "," + vals[4] + "," + vals[5] + "," + values[6];
//                            writer.println(str);
//                            writer.flush();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
