package dao;

import data.BlockChainRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.RestCaller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BlockChainDAO {
    private static final Logger logger = LoggerFactory.getLogger(BlockChainDAO.class);
    private static BlockChainDAO single_instance = null;
    private RestCaller restCaller;


    // private constructor restricted to this class itself
    private BlockChainDAO() {

        this.restCaller = RestCaller.getInstance();

    }

    // static method to create instance of Singleton class
    public static BlockChainDAO getInstance() {
        if (single_instance == null)
            single_instance = new BlockChainDAO();

        return single_instance;
    }

    public boolean save(String str) {
        logger.info("save " + str);
        boolean re = false;
        BlockChainRequestEntity blockChainRequestEntity = new BlockChainRequestEntity();
        blockChainRequestEntity.setSender("sender");
        blockChainRequestEntity.setReceiver("receiver");
        blockChainRequestEntity.setMessageId(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        blockChainRequestEntity.setContent(str);
        try {
            re = this.restCaller.postToBc(blockChainRequestEntity);
            logger.info("save to blockchain with " + re);
            return re;
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("save to blockchain with " + re);
        return re;
    }


    public String read() {
        try {
            return this.restCaller.getFromBc();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean save(List<String> forDaoList) {
        boolean res = true;
        int i = 0;
        for (String s : forDaoList) {
            boolean re = save(s);
            if (!re) res = false;
        }
        return res;

    }
}
