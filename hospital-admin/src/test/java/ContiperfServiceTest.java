import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.Patient;
import com.hospital.service.DeliveryService;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.databene.contiperf.junit.ParallelRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
主要参数介绍
    1）PerfTest参数
        @PerfTest(invocations = 300)：执行300次，和线程数量无关，默认值为1，表示执行1次；
        @PerfTest(threads=30)：并发执行30个线程，默认值为1个线程；
        @PerfTest(duration = 20000)：重复地执行测试至少执行20s。

        　　 Invocations：方法的执行次数，例：@PerfTest(invocations = 300)重复执行300次;

        　　 Threads：同时执行的线程数，例：@PerfTest(invocations = 30, threads = 2)两个线程并发执行，每个线程执行15次，总共执行30次;

        　　 Duration：在指定时间范围内一直执行测试，例：@PerfTest(duration = 300)在300毫秒内反复执行。

        　　 三个属性可以组合使用，其中Threads必须和其他两个属性组合才能生效。当Invocations和Duration都有指定时，以执行次数多的为准。
            　　 例，@PerfTest(invocations = 300, threads = 2, duration = 100)，如果执行方法300次的时候执行时间还没到100ms，则继续执行
                到满足执行时间等于100ms，如果执行到50次的时候已经100ms了，则会继续执行之100次。

        　　 如果你不想让测试连续不间断的跑完，可以通过注释设置等待时间，
                例， @PerfTest(invocations = 1000, threads = 10, timer = RandomTimer.class, timerParams = { 30, 80 }) ，
                每执行完一次会等待30~80ms然后才会执行下一次调用。

        　　 在开多线程进行并发压测的时候，如果一下子达到最大进程数有些系统可能会受不了，ContiPerf还提供了“预热”功能，
                例，@PerfTest(threads = 10, duration = 60000, rampUp = 1000) ，
                启动时会先起一个线程，然后每个1000ms起一线程，到9000ms时10个线程同时执行，那么这个测试实际执行了69s，如果只想衡量全力压测的结果，那么可以在注释中加入warmUp，
                即@PerfTest(threads = 10, duration = 60000, rampUp = 1000, warmUp = 9000) ，那么统计结果的时候会去掉预热的9s。

    2）Required参数
        @Required(throughput = 20)：要求每秒至少执行20个测试；
        @Required(average = 50)：要求平均执行时间不超过50ms；
        @Required(median = 45)：要求所有执行的50%不超过45ms；
        @Required(max = 2000)：要求没有测试超过2s；
        @Required(totalTime = 5000)：要求总的执行时间不超过5s；
        @Required(percentile90 = 3000)：要求90%的测试不超过3s；
        @Required(percentile95 = 5000)：要求95%的测试不超过5s；
        @Required(percentile99 = 10000)：要求99%的测试不超过10s;
        @Required(percentiles = "66:200,96:500")：要求66%的测试不超过200ms，96%的测试不超过500ms。
*/


//@RunWith(ParallelRunner.class)    //to run all test functions in this class in parallel, instead of testing one by one.
public class ContiperfServiceTest extends BaseTesting{
    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Autowired
    private DeliveryService deliveryService;


    /*
    test1() and test2() are combination for testing parallel testing methodology.
     */
    @Test
	@PerfTest(invocations = 10, threads = 2)
	@Required(max = 1200, average = 1000, totalTime = 60000)
	public void test1() throws Exception {
        System.out.println(" 123 ") ;
		Thread.sleep(200);
	}

    @Test
	@PerfTest(invocations = 10, threads = 2)
	@Required(max = 1200, average = 1000, totalTime = 60000)
	public void test2() throws Exception {
        System.out.println(" 456 ") ;
		Thread.sleep(200);
	}


	/*
	getDeliveryInfosByPatientIdTest is to test spring+hibernate correctness
	 */
    @Test
    @PerfTest(invocations = 10, threads = 2)
	@Required(max = 2200, average = 1000, totalTime = 60000)
    public void getDeliveryInfosByPatientIdTest() {
        Patient patient = new Patient();
        patient.setPatientId(52);
        List<DeliveryInfo> DIs = deliveryService.getDeliveryInfosByPatientId(patient);
        System.out.println(" deliveryInfo number for Patient ID=52 is :      " + DIs.size()) ;
    }



	/*
	sendTemplateMessageTest is to test spring+hibernate correctness
	NOTE: currently NOT working since sendTemplateMessage() needs something from wechat
	 */
    @Test
    @PerfTest(invocations = 10, threads = 5)
	@Required(max = 10000, average = 5000, totalTime = 60000)
    public void sendTemplateMessageTest() {
        Patient patient = new Patient();
        patient.setPatientId(52);
        List<DeliveryInfo> DIs = deliveryService.getDeliveryInfosByPatientId(patient);
        for(DeliveryInfo di : DIs) {
            boolean sent = deliveryService.sendTemplateMessage(di);
            if(sent) {
                System.out.println(" sending TemplateMessage to patient 52 with deliveryId = " + di.getDeliveryId());
            } else {
                System.out.println(" send TemplateMessage failed!");
            }
        }
    }



	/*
	addDeliveryTest is to test adding deliveryInfo in parallel
	 */
    @Test
    @PerfTest(invocations = 10, threads = 5)//共执行100次，50个线程同时执行
	@Required(max = 10000, average = 5000)   //最长每个不多于10s,平均时间不超过5s
    public void addDeliveryTest() {
        Patient patient = new Patient();
        patient.setPatientId(52);
        List<DeliveryInfo> DIs = deliveryService.getDeliveryInfosByPatientId(patient);
        DeliveryInfo di = DIs.get(0);
        int success = deliveryService.addDelivery(di);
        if(success != 0) {
            System.out.println(" add new deliveryInfo to patient 52");
        } else {
            System.out.println(" add new deliveryInfo failed!");
        }
    }
}