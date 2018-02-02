import com.hospital.util.DateUtils;

import java.text.SimpleDateFormat;

public class DateTest {
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayBegin = simpleDateFormat.format(DateUtils.getDayBegin());
        System.out.println(todayBegin);
    }
}
