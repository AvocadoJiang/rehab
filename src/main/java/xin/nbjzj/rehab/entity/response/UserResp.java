package xin.nbjzj.rehab.entity.response;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.nbjzj.rehab.entity.User;

@ApiModel(value="userResp",description="用户")
@Data
public class UserResp implements Serializable {
	@ApiModelProperty(value="用户主键")
	private String user_id;
	
	@ApiModelProperty(value="用户姓名",example="蒋周杰")
	private String user_name;
		
	@ApiModelProperty(value="用户身份,取值范围{'PATIENT','DOCTOR','ADMIN'}")
	private String identity;
	
	@ApiModelProperty(value="用户性别,取值范围{'男','女'}")
	private String gender;
	
	@ApiModelProperty(value="手机号码")
	private String phone;
	
	@ApiModelProperty(value="用户身份证号码")
	private String id_number;
	
	@ApiModelProperty(value="用户所属机构")
	private String institution;
	
	@ApiModelProperty(value="用户联系地址")
	private String address;
	
	public UserResp(User entity) {
		super();
		this.user_id = entity.getUserID();
		this.user_name = entity.getUserName();
		this.gender = entity.getGender();
		this.identity = entity.getIdentity();
		this.phone = entity.getPhone();
		this.id_number = entity.getIdNumber();
		this.institution = entity.getInstitution();
		this.address = entity.getAddress();
	}
	
	public UserResp() {
		super();
	}
}
