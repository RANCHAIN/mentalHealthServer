package service;

import dao.BlockChainDAO;
import data.BlockChainPersistenceData;
import data.ChatSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.util.List;

public class BlockChainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockChainService.class);
    private static BlockChainService single_instance = null;
    private BlockChainDAO blockChainDAO;


    // private constructor restricted to this class itself
    private BlockChainService() {
        this.blockChainDAO = BlockChainDAO.getInstance();

    }

    // static method to create instance of Singleton class
    public static BlockChainService getInstance() {
        if (single_instance == null)
            single_instance = new BlockChainService();

        return single_instance;
    }

    public boolean saveMessages(List<String> mesg, ChatSession chatSession) {
        this.blockChainDAO.save(Utils.getBeautifiedJson.apply(new BlockChainPersistenceData(chatSession, mesg)));
        return true;
    }

}
