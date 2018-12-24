package xin.nbjzj.rehab.blockchain.member;


import java.util.Date;

import lombok.Data;

/**
 * 联盟的成员
 * @author wuweifeng wrote on 2018/3/5.
 */
@Data
public class Member {
    /**
     * 成员id，用于校验该客户是否合法，客户端启动时需要带着该值。一个公司可能有多个appId，相当于多个服务器节点
     */
    private String appId;
    /**
     * 成员名
     */
    private String name;
    /**
     * ip（可不设置，由该成员客户端启动后自行检测）
     */
    private String ip;

    private Date createTime;

    private Date updateTime;


}
