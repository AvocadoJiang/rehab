package xin.nbjzj.rehab.blockchain.block.db;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * 配置启用哪个db，部分Windows机器用不了rocksDB，可以选择levelDB
 * @author wuweifeng wrote on 2018/3/13.
 */
@Configuration
public class DbInitConfig {

    @Bean
    public RocksDB rocksDB() {
        RocksDB.loadLibrary();
        Options options = new Options().setCreateIfMissing(true);
        try {
            return RocksDB.open(options, "./rocksDB");
        } catch (RocksDBException e) {
            e.printStackTrace();
            return null;
        }
    }

}
