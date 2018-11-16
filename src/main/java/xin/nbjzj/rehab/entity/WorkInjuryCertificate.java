package xin.nbjzj.rehab.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import xin.nbjzj.rehab.entity.request.WorkInjuryCertificateReq;

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
	
	/** 病人 **/
	@DBRef
	private User patient;
	
	@DBRef
	/** 受理机关 **/
	private User admin;
	
	@DBRef
	/** 临床诊疗信息 **/
	private ClinicalInfo clinicalInfo;
	
	private String patient_id;
	private String admin_id;
	private String clinicalInfo_id;
	
	
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
		this.patient_id = req.getPatient_id();
		this.admin_id = req.getAdmin_id();
		this.clinicalInfo_id = req.getClinicalInfo_id();
		this.accidentTime = req.getAccident_time();
		this.accidentProcess = req.getAccident_process();
		this.injuredPart = req.getInjured_part();
		this.institutionOpinion = req.getInstitution_opinion();
		this.adminOpinion = req.getAdmin_opinion();
		this.workInjuryCertificateResult = req.isWorkInjuryCertificate_result();
		this.disabilityLevel = req.getDisability_level();
	}
}
