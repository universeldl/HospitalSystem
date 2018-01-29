import com.hospital.util.AgeUtils;

public class AgeTest {
    public static void main(String[] args) {
        String dateOfBirth = "2015-1-28";
        int age = AgeUtils.getAgeFromBirthTime(dateOfBirth);
        System.out.println("age:" + age);
    }
}
