import com.hospital.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class RedisTest {
    @Resource
    private RedisUtil redisUtil;

    @Test
    public void redisPutTest() {
        //添加
        redisUtil.set("name", "hospital");
    }

    @Test
    public void redisGetTest() {
        //获取
        String str = (String) redisUtil.get("name");
        System.out.println(str);
    }

    @Test
    public void redisDeleteTest() {
        redisUtil.remove("name");
    }


    @Test
    public void redisPutListTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("github");
        stringList.add("13");
        stringList.add("hospital");
        //添加
        redisUtil.set("stringList", stringList);
    }

    @Test
    public void redisGetListTest() {
        //获取
        List<String> stringList = (List<String>) redisUtil.get("stringList");
        if (stringList.size() > 0) {
            for (String string : stringList
                    ) {
                System.out.println(string);
            }
        }
    }

}
 */
