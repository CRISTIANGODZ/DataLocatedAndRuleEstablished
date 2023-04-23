package com.cqupt.case_data_location.utils;

import com.csvreader.CsvReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @auther DyingZhang
 * @Create 2022-10-13 下午 10:40
 * @Discriptioon
 */
public class ReaderCSV {
    public static StringBuffer printCSVFile(String filePath, char Separator) {
        // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvReader csvReader = null;
        try {
            csvReader = new CsvReader(filePath, Separator, Charset.forName(com.cqupt.case_data_location.utils.Charset.charset(filePath)));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 读取每行的内容
        StringBuffer stringBuffer = new StringBuffer();
        try {
            while (csvReader.readRecord()) {
                int i = 0;
                while (csvReader.get(i) != ""){
                    stringBuffer.append(csvReader.get(i));
                    stringBuffer.append(";");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

}
