package xin.nbjzj.rehab.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Data;
import xin.nbjzj.rehab.entity.request.ClinicalInfoReq;

/**
 * 临床医疗信息实体类
 * @author Jason Chiang
 *
 */

@Document(collection="clinicalInfo")
@Data
public class ClinicalInfo {
	@Id
	private String clinicalInfoID;
	
	
	/** 病人 **/
	@DBRef
	private User patient;
	private String patientID;
	
	
	/** 医生 **/
	@DBRef
	private User doctor;
	private String doctorID;
	
	/** 摘要 **/
	private String summary;
	
	/** 诊断内容 **/
	private String diagnosis;
	
	/** 诊疗计划 **/
	private String medicalPlan;
	
	/** 诊疗时间 **/
	private String clinicalTime;

	public ClinicalInfo() {
		super();
	}
	
	public ClinicalInfo(ClinicalInfoReq req) {
		super();
		this.doctorID = req.getDoctor_id();
		this.patientID = req.getPatient_id();
		this.summary = req.getSummary();
		this.diagnosis = req.getDiagnosis();
		this.medicalPlan = req.getMedical_plan();
		this.clinicalTime = req.getClinical_time();
	}
	
	
}
