package xin.nbjzj.rehab.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import xin.nbjzj.rehab.core.entity.request.LoginReq;
import xin.nbjzj.rehab.core.entity.request.UserReq;



/**
 * 用户实体类
 * @author Jason Chiang
 *
 */
@Entity
@Table(name="user")
@Data
public class User {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
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
	@Column(unique=true)
	private String phone;
	
	/** 身份证号码 **/
	private String idNumber;
	
	/** 所属机构 **/
	private String institution;
	
	/** 联系地址 **/
	private String address;
	
	/** 公钥 **/
	private String publicKey;
	
	/** 私钥 **/
	private String privateKey;
	
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
	
	public User(LoginReq req) {
		super();
		this.phone = req.getPhone();
		this.password = req.getPassword();
	}
}
