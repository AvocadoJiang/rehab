package xin.nbjzj.rehab.core.mysql.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import xin.nbjzj.rehab.blockchain.common.CommonUtil;
import xin.nbjzj.rehab.core.entity.ClinicalInfo;
import xin.nbjzj.rehab.core.entity.User;

@Entity
@Table(name="sync")
@Data
public class Sync{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long syncID;
    /**
     * 已同步的区块hash
     */
    private String hash;
    /**
     * 创建时间
     */
    private Long createTime = CommonUtil.getNow();
}
