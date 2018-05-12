package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.util.List;

public class BlockChainDAO {
    private static final Logger logger = LoggerFactory.getLogger(BlockChainDAO.class);
    private static BlockChainDAO single_instance = null;


    // private constructor restricted to this class itself
    private BlockChainDAO() {


    }

    // static method to create instance of Singleton class
    public static BlockChainDAO getInstance() {
        if (single_instance == null)
            single_instance = new BlockChainDAO();

        return single_instance;
    }

    public boolean save(String str) {
        logger.info("save");
        logger.info(str);
        return true;
    }

    public boolean save(List<String> lists) {

        String forSave = Utils.getBeautifiedJson.apply(lists);
        logger.info("save " + forSave);
        return true;
    }

    public String read(String str) {
        logger.info("read");
        logger.info(str);
        return "";
    }
}
