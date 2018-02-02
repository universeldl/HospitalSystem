

import static com.hospital.util.TimeUtils.getLast12Months;

public class TimeTest {
   public static void main(String[] args){
       String[] last12Months = getLast12Months("2017-02");
       System.out.println(last12Months[0]);
   }
}
