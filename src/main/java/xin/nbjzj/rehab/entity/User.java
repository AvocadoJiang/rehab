package xin.nbjzj.rehab.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.Data;
import xin.nbjzj.rehab.entity.request.UserReq;



/**
 * 用户实体类
 * @author Jason Chiang
 *
 */
@Document(collection="user")
@Data
public class User {
	
	@Id
	private String userID;
	
	
	/** 姓名 **/
	private String userName;
	
	/** 密码 **/
	private String password;
	
	/** 身份 **/
	private String identity;
	
	/** 性别 **/
	private String gender;
	
	/** 联系方式 **/
	private String phone;
	
	/** 身份证号码 **/
	private String idNumber;
	
	/** 所属机构 **/
	private String institution;
	
	/** 联系地址 **/
	private String address;
	
	
	/** 用户账户身份 **/
	public static enum USER_IDENTITY
	{
		/** 学生账号 **/
		PATIENT,
		/** 医生账号 **/
		DOCTOR,
		/** 学院管理账号 **/
		ADMIN
	}
	
	/**
	 * 获取用户身份枚举参数名称
	 * @param utype
	 * 		用户类型请求值
	 * @return
	 * 		枚举变量
	 */
	public static USER_IDENTITY getUserTypeEnum(String utype)
	{
		return USER_IDENTITY.values()[USER_IDENTITY.valueOf(utype.toUpperCase().trim()).ordinal()];
	}

	public User() {
		super();
	}
	
	public User(UserReq req) {
		super();
		this.address = req.getAddress();
		this.gender = req.getGender();
		this.identity = req.getIdentity();
		this.idNumber = req.getId_number();
		this.institution = req.getInstitution();
		this.password = req.getPassword();
		this.phone = req.getPhone();
		this.userName = req.getUser_name();
		
	}
}
