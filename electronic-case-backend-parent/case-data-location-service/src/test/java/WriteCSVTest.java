import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 下午 5:05
 * @Discriptioon
 */
public class WriteCSVTest {
    public static void main(String[] args) throws IOException {
        write("F:\\project-workspace\\DataLocatedAndRuleEstablished\\electronic-case-backend-parent\\case-data-location-service\\src\\test\\java\\1.csv");
    }

    public static void write(String file) throws IOException {
        // 第一参数：新生成文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvWriter csvWriter = new CsvWriter(file, ',', Charset.forName("GBK"));

        // 表头和内容
        String[] content = {"病种"+":"+"肝病"};
        String[] content1 = {"病种"+":"+"肝病"};

        // 写表头和内容，因为csv文件中区分没有那么明确，所以都使用同一函数，写成功就行
        csvWriter.writeRecord(content);
        csvWriter.writeRecord(content1);

        // 关闭csvWriter
        csvWriter.close();
    }
}
