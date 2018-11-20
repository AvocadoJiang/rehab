package xin.nbjzj.rehab.blockchain.block.db;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * rocksDB对于存储接口的实现
 * @author wuweifeng wrote on 2018/3/13.
 */
@Component
public class RocksDbStoreImpl implements DbStore {
    @Resource
    private RocksDB rocksDB;

    @Override
    public void put(String key, String value) {
        try {
            rocksDB.put(key.getBytes("UTF-8"), value.getBytes("UTF-8"));
        } catch (RocksDBException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String get(String key) {
        try {
            byte[] bytes = rocksDB.get(key.getBytes("UTF-8"));
            if (bytes != null) {
                return new String(bytes, "UTF-8");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(String key) {
        try {
            rocksDB.delete(rocksDB.get(key.getBytes("UTF-8")));
        } catch (RocksDBException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
