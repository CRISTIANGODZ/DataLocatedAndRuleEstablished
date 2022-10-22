import com.cqupt.electroniccase.pojo.Diseases;
import com.cqupt.electroniccase.pojo.FirstCategory;
import com.cqupt.electroniccase.pojo.Patients;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:06
 * @Discriptioon
 */
public class ParseStringTest {
    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer("入院记录：;编号：123456;姓名：张三;性别：男;年龄：18岁;民族：汉族;职业：学生;婚姻：未婚;病史陈述者：张三;床号：001;科室：内科;病种：肝病;病史：;主诉：;现病史：;既往史：;个人史：;家族史：;一般情况：;皮肤粘膜：;头颅五官：;颈部：;入院诊断：1.XXX诊断结果;");
        String firstCategoryName = stringBuffer.substring(0, stringBuffer.indexOf(";")-1);
        stringBuffer.delete(0,stringBuffer.indexOf(";")+1);
        String patientsContext = stringBuffer.substring(0, stringBuffer.indexOf("病种"));
        stringBuffer.delete(0,stringBuffer.indexOf("病种"));
        String diseaseName = stringBuffer.substring(stringBuffer.indexOf("病种"), stringBuffer.indexOf(";"));
        stringBuffer.delete(stringBuffer.indexOf("病种"), stringBuffer.indexOf(";")+1);
        //1.将第一种类，病人信息，病种加入实体类对象中
        FirstCategory firstCategory = null;
        Patients patients = null;
        Diseases diseases = null;
        //获取第一种类的实体类对象
        if ((firstCategoryName != "") && (firstCategoryName != null)){
            firstCategory = new FirstCategory(firstCategoryName);
        }
        //获取病人信息的对象
        if ((patientsContext != "") && (patientsContext != null) && patientsContext.contains("姓名")){
            String temporaryStr = patientsContext.substring(patientsContext.indexOf("姓名"), patientsContext.length());
            String name = temporaryStr.substring(temporaryStr.indexOf("姓名")+3, temporaryStr.indexOf(";"));
//            patients = new Patients(name);
        }
        //获取病种的对象
        if ((diseaseName != "") && (diseaseName != null)){
            String diseasesName = diseaseName.substring(diseaseName.indexOf("病种") + 3, diseaseName.length());
            diseases = new Diseases(diseasesName);
        }
    }

}
