package org.nmic.diff_bufr_flag_tables;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Comparator {
    private final String baseTableFileName;
    private final String compTableFileName;
    private final String outPath;
    
    private BufferedWriter statWriter;
    private BufferedWriter diffWriter;
    
    public Comparator(String baseTableFileName, String cmpTableFileName, String outPath){
        this.baseTableFileName = baseTableFileName;
        this.compTableFileName = cmpTableFileName;
        this.outPath = outPath;
    }
    
    public void compare(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        String datestr = sdf.format(new Date());
        String statFileName = outPath + File.separator + "stat_" + datestr + ".csv";
        String diffFileName = outPath + File.separator + "diff_" + datestr + ".csv";
        try {
            statWriter = new BufferedWriter(new FileWriter(statFileName));
            diffWriter = new BufferedWriter(new FileWriter(diffFileName));
            write(statWriter,"代码,含义,一致性情况");
            write(diffWriter,"代码,含义,代码值,参照版本含义,对比版本含义");
            Map baseTable = getCodeTable(baseTableFileName);
            Map compTable = getCodeTable(compTableFileName);
            compareCodeTable(baseTable, compTable);
            statWriter.close();
            diffWriter.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private Map getCodeTable(String fileName) throws IOException, CsvValidationException{
        Map table = new HashMap();
        CSVReader rd = new CSVReader(new FileReader(fileName));
        String[] lines;
        rd.readNext();
        while((lines = rd.readNext()) != null){
            if (lines.length < 5){
                continue;
            }
            String key = lines[1] + "," + lines[2].replace(',', '_');
            Map codeMap = (Map) table.get(key);
            if (codeMap == null){
                codeMap = new HashMap();
                table.put(key, codeMap);
            }
            codeMap.put(lines[3], lines[4]);
        }
        rd.close();
        return table;
    }
    
    private void compareCodeTable(Map baseTable, Map compTable) throws IOException{
        Set<Map.Entry<String, Map>> compEnts = compTable.entrySet();
        for(Map.Entry<String, Map> compEnt : compEnts){
            String key = compEnt.getKey();
            Map compCode = compEnt.getValue();
            Map baseCode = (Map)baseTable.get(key);
            if (baseCode == null){
                write(statWriter, "'" + key + "," + DiffType.ADD_ELE.getVal());
            }else{
                compareCode(key, baseCode, compCode);
                baseTable.remove(key);
            }
        }
        
        Iterator<String> it = baseTable.keySet().iterator();
        while(it.hasNext()){
            write(statWriter, "'" + it.next() + "," + DiffType.RED_ELE.getVal());
        }
    }
    
    private void compareCode(String code, Map baseCode, Map compCode) throws IOException{
        boolean isSame = true, isAdd = false, isRed = false, isDiff = false;
        Set<Map.Entry> ents = compCode.entrySet();
        for(Map.Entry ent : ents){
            String key = (String)ent.getKey();
            String compVal = (String)ent.getValue();
            String baseVal = (String)baseCode.get(key);
            if (baseVal != null){
                if(!compVal.equals(baseVal)){
                    isSame = false;
                    isDiff = true;
                    write(diffWriter,String.format("'%s,'%s,%s,%s", code, key, baseVal, compVal));
                }
                baseCode.remove(key);
            }else{
                isSame = false;
                isAdd = true;
                write(diffWriter,String.format("'%s,'%s,%s,%s", code, key, "", compVal));
            }
        }
        
        if(baseCode.size() > 0){
            isSame = false;
            isRed = true;
            Iterator it = baseCode.keySet().iterator();
            while(it.hasNext()){
                String key = (String)it.next();
                String baseVal = (String)baseCode.get(key);
                write(diffWriter,String.format("'%s,'%s,%s,%s", code, key, baseVal, ""));
            }
        }
        
        String diffInfo = "";
        if (isSame){
            diffInfo = DiffType.SAME.getVal();
        }else{
            if (isAdd){
                diffInfo += " " +  DiffType.ADD_CODE.getVal();
            }
            if (isDiff){
                diffInfo += " " +  DiffType.DIFF_VAL.getVal();
            }
            if (isRed){
                diffInfo += " " +  DiffType.RED_CODE.getVal();
            }
        }
        write(statWriter, "'" + code + "," + diffInfo);
    }
    
    private void write(BufferedWriter wr, String ss) throws IOException{
        wr.write(ss);
        wr.newLine();
    }
    
}
