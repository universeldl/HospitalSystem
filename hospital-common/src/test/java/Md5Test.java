import com.hospital.util.Md5Utils;

public class Md5Test {
    public static void main(String[] args) {
        String pwd = "123456";
        String encPwd = Md5Utils.md5(pwd);
        System.out.println("encPwd: " + encPwd);
    }
}