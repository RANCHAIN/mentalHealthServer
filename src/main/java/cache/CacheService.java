package cache;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import dao.BlockChainDAO;
import data.ConversationEntity;
import data.Verdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheService {


    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private static volatile CacheService single_instance = null;
    private ConcurrentHashMap<String, List<String>> cacheMap;
    private ReadWriteLock lock;
    private Lock writeLock;
    private Lock readLock;

    private List<String> cacheList;
    private List<Verdict> cacheListForVerdicts;

    // private constructor restricted to this class itself
    private CacheService() {
        this.cacheMap = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.writeLock = this.lock.writeLock();
        this.readLock = this.lock.readLock();

        this.cacheList = new LinkedList<>();
        this.cacheListForVerdicts = new LinkedList<>();

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


    public void putSingleElementInCache(String body) {

        this.writeLock.lock();

        this.cacheList.add(body);
        logger.info("cache size now is " + this.cacheList.size());

        if (this.cacheList.size() >= 5) {
            logger.info("this cache list size is more than 5");

            List<String> forDaoList = new ArrayList<>(this.cacheList);
            try {
                CompletableFuture.supplyAsync(() -> {
                    System.out.println(forDaoList);
                    return BlockChainDAO.getInstance().save(forDaoList);
                }).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            this.cacheList.clear();
        }

        this.writeLock.unlock();


    }


    public void putSingleElementInCacheForNLP(String body) {
        this.writeLock.lock();
        this.cacheList.add(body);
        logger.info("cache size now is " + this.cacheList.size());
        this.writeLock.unlock();
    }

    public List<String> getAllMsgsFromCacheForNLPByUser(String user) {
        this.writeLock.lock();
        List<String> res = Lists.newArrayList();

        for (int i = 0; i < this.cacheList.size(); i++) {
            String item = this.cacheList.get(i);
            Gson g = new Gson();
            ConversationEntity conversationEntity = g.fromJson(item, ConversationEntity.class);
            if (conversationEntity != null) {
                if (conversationEntity.getSender().equalsIgnoreCase(user)) {
                    res.add(conversationEntity.getMsg());
                    this.cacheList.remove(i);
                }
            }
        }

        this.writeLock.unlock();
        return res;
    }


    public void putSingleElementInCacheForVerdicts(Verdict verdict) {
        this.writeLock.lock();
        this.cacheListForVerdicts.add(verdict);
        logger.info("cacheListForVerdicts size now is " + this.cacheListForVerdicts.size());
        this.writeLock.unlock();
    }

    public List<Verdict> getAllVerdicts() {
        this.writeLock.lock();
        List<Verdict> re = new ArrayList<>(this.cacheListForVerdicts);
        this.cacheListForVerdicts.clear();
        this.writeLock.unlock();
        return re;
    }
}
