package xin.nbjzj.rehab.blockchain.member;

import java.util.List;

import lombok.Data;

/**
 * @author wuweifeng wrote on 2018/3/19.
 */
@Data
public class MemberData {
    private int code;
    private String message;
    private List<Member> members;

}
