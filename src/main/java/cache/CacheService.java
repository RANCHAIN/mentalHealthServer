package cache;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheService {


    private static volatile CacheService single_instance = null;
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private ConcurrentHashMap<String, List<String>> cacheMap;
    private ReadWriteLock lock;
    private Lock writeLock;
    private Lock readLock;

    // private constructor restricted to this class itself
    private CacheService() {
        this.cacheMap = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.writeLock = this.lock.writeLock();
        this.readLock = this.lock.readLock();
    }

    // static method to create instance of Singleton class
    public static synchronized CacheService getInstance() {
        if (single_instance == null)
            single_instance = new CacheService();

        return single_instance;
    }

    public void putInCache(String key, String message) {
        this.writeLock.lock();
        if (this.cacheMap.containsKey(key)) {
            List<String> curr = this.cacheMap.get(key);
            curr.add(message);
            this.cacheMap.put(key, curr);
        } else {
            List<String> curr = Lists.newArrayList();
            curr.add(message);
            this.cacheMap.put(key, curr);
        }
        this.writeLock.unlock();
    }

    public List<String> getValueAsList(String key) {
        this.readLock.lock();

        if (this.cacheMap.containsKey(key)) {
            List<String> res = this.cacheMap.get(key);
            this.readLock.unlock();
            return res;
        }
        this.readLock.unlock();
        return Lists.newArrayList();
    }

    public void clearKey(String key) {
        this.writeLock.lock();
        this.cacheMap.remove(key);
        this.writeLock.unlock();
    }


}
