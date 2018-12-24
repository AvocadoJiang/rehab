package xin.nbjzj.rehab.core.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import xin.nbjzj.rehab.core.entity.request.WorkInjuryCertificateReq;

/**
 * 工伤认定实体类
 * @author Jason Chiang
 *
 */
@Entity
@Table(name="workinjurycertificate")
@Data
public class WorkInjuryCertificate {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String workInjuryCertificateID;

	/** 受理机关 **/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="adminID")
	private User admin;
	@Transient
	private String adminID;
	
	/** 临床诊疗信息 **/
	@ManyToOne(fetch = FetchType.LAZY,optional=false)
	@JoinColumn(name="clinicalInfoID")
	private ClinicalInfo clinicalInfo;
	@Transient
	private String clinicalInfoID;
	
	/** 事故地点 **/
	private String accidentPlace;
	
	/** 事故时间 **/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date accidentTime;
	
	/** 受伤害经过简述（事故时间、地点、受伤原因、受伤过程、诊断结论） **/
	private String accidentProcess;
	
	/** 受伤害部位 **/
	private String injuredPart;
	
	/** 用人单位意见 **/
	private String institutionOpinion;
	
	
	//认定结果
	/** 受理机关意见 **/
	private String adminOpinion;
	
	/** 工伤认定决定书结果 **/
	private boolean workInjuryCertificateResult;

	
	/** 伤残等级 **/
	private String disabilityLevel;

	public WorkInjuryCertificate() {
		super();
	}
	
	public WorkInjuryCertificate(WorkInjuryCertificateReq req) {
		super();
		this.adminID = req.getAdmin_id();
		this.clinicalInfoID = req.getClinicalInfo_id();
		this.accidentTime = req.getAccident_time();
		this.accidentProcess = req.getAccident_process();
		this.accidentPlace = req.getAccident_place();
		this.injuredPart = req.getInjured_part();
		this.institutionOpinion = req.getInstitution_opinion();
		this.adminOpinion = req.getAdmin_opinion();
		this.workInjuryCertificateResult = req.isWorkInjuryCertificate_result();
		this.disabilityLevel = req.getDisability_level();
	}
}
