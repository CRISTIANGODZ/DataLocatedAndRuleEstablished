package com.cqupt.electroniccase.utils;

import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @auther DyingZhang
 * @Create 2022-10-13 下午 10:59
 * @Discriptioon
 */
public class WriteCSV {
    public static void write(String file) throws IOException {
        // 第一参数：新生成文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvWriter csvWriter = new CsvWriter(file, ',', Charset.forName("UTF-8"));

        // 表头和内容
        String[] headers = {"姓名", "年龄", "性别"};
        String[] content = {"张三", "18", "男"};

        // 写表头和内容，因为csv文件中区分没有那么明确，所以都使用同一函数，写成功就行
        csvWriter.writeRecord(headers);
        csvWriter.writeRecord(content);

        // 关闭csvWriter
        csvWriter.close();
    }
}
