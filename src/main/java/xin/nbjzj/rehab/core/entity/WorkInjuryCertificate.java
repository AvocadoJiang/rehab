package xin.nbjzj.rehab.core.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import xin.nbjzj.rehab.core.entity.request.WorkInjuryCertificateReq;

/**
 * 工伤认定实体类
 * @author Jason Chiang
 *
 */
@Document(collection="workInjuryCertificate")
@Data
public class WorkInjuryCertificate {
	
	@Id
	private String workInjuryCertificateID;

	/** 受理机关 **/
	@DBRef
	private User admin;
	private String adminID;
	
	/** 临床诊疗信息 **/
	@DBRef
	private ClinicalInfo clinicalInfo;
	private String clinicalInfoID;
	
	
	/** 事故地点 **/
	private String accidentPlace;
	
	/** 事故时间 **/
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
