package dao;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

public class BlockChainDAOTest {
    @Test
    public void save() throws Exception {
        BlockChainDAO blockChainDAO = BlockChainDAO.getInstance();
        List<String> forM = Lists.newArrayList();
        forM.add("aaa");
        forM.add("bbbb");
        forM.add("ccc");
        System.out.println(blockChainDAO.save(forM));
    }

    @Test
    public void read() throws Exception {
        System.out.println(BlockChainDAO.getInstance().read());
    }

    @Test
    public void save1() throws Exception {
        BlockChainDAO blockChainDAO = BlockChainDAO.getInstance();
        System.out.println(blockChainDAO.save("forsss"));
    }

}