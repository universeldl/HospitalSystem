import com.hospital.util.CalendarUtils;

import java.text.SimpleDateFormat;

public class CalendarTest {
    public static void main(String[] args) {
        String lastMonth = CalendarUtils.getLastMonth("20150423");
        System.out.println(lastMonth);
    }
}
