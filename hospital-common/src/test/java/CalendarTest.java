import com.hospital.util.CalendarUtils;

public class CalendarTest {
    public static void main(String[] args) {
        String lastMonth = CalendarUtils.getLastMonth("20150423");
        System.out.println(lastMonth);
        String timeNow = CalendarUtils.getNowTime();
        System.out.println(timeNow);
    }
}
