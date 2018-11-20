package xin.nbjzj.rehab.core.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="userReq",description="用户")
@Data
public class UserReq {

	@ApiModelProperty(value="用户姓名",example="蒋周杰")
	private String user_name;
	
	@ApiModelProperty(value="用户密码,在前段用32位MD5加密",example="e10adc3949ba59abbe56e057f20f883e")
	@NotBlank
	private String password;
	
	
	@ApiModelProperty(value="用户身份,取值范围{'PATIENT','DOCTOR','ADMIN'}",example="PATIENT")
	@NotBlank
	private String identity;
	
	@ApiModelProperty(value="用户性别,取值范围{'男','女'}",example="男")
	private String gender;
	
	@ApiModelProperty(value="手机号码",example="15757469199")
	@NotBlank
	private String phone;
	
	@ApiModelProperty(value="用户身份证号码",example="330501199702030038")
	private String id_number;
	
	@ApiModelProperty(value="用户所属机构",example="宁波易享健康有限公司")
	private String institution;
	
	@ApiModelProperty(value="用户联系地址",example="浙江省宁波市风华路201号宁波工程学院东校区")
	private String address;
}
