import com.hospital.util.AgeUtils;

public class AgeTest {
    public static void main(String[] args) {
        String dateOfBirth = "01/28/2015";
        int age = AgeUtils.getAgeFromBirthTime(dateOfBirth);
        System.out.println("age:" + age);
    }
}
