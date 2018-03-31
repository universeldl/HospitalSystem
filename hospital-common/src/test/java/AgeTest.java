import com.hospital.util.AgeUtils;

public class AgeTest {
    public static void main(String[] args) {
        String dateOfBirth = "3/31/2016";
        int age = AgeUtils.getAgeFromBirthTime(dateOfBirth);
        System.out.println("age:" + age);
    }
}
