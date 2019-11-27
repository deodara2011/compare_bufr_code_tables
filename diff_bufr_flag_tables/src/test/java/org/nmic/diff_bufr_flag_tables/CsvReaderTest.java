package org.nmic.diff_bufr_flag_tables;

import java.io.*;
import java.util.*;

import org.junit.Test;

import com.opencsv.CSVReader;

public class CsvReaderTest {
    @Test
    public void test(){
        try {
            String fileName = "E:\\NMIC\\common\\bufr\\wmotables\\va_BUFRCREX_32_0_0\\test.txt";
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            List<String[]> data = csvReader.readAll();
            for(String[] dd : data){
                System.out.println(Arrays.toString(dd));
            }
            csvReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
