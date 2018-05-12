package cache;

import org.junit.Test;

public class CacheServiceTest {

    @Test
    public void test() throws Exception {


        CacheService cacheService = CacheService.getInstance();
        cacheService.putInCache("1", "abc");
        cacheService.putInCache("1", "abcd");
        cacheService.putInCache("2", "abcdfff");

        System.out.println(cacheService.getValueAsList("1"));
        System.out.println(cacheService.getValueAsList("2"));


    }
}