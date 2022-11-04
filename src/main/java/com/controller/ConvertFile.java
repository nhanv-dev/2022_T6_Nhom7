package com.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertFile {
    public static void main(String[] args) throws IOException {
        String src1 = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\test";
        String src2 = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\test\\businessinsider-20221104030826.csv";

        File sfile = new File(src2);
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sfile))) {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\cate_dim.csv", true)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                list.add(values);
                writer.println(values[4]);
                writer.flush();
            }
        }

    }
}
