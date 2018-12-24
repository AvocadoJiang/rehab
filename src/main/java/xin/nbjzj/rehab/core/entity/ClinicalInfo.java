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
import xin.nbjzj.rehab.core.entity.request.ClinicalInfoReq;

/**
 * 临床医疗信息实体类
 * @author Jason Chiang
 *
 */

@Entity
@Table(name="clinicalinfo")
@Data
public class ClinicalInfo {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String clinicalInfoID;
	
	/** 病人 **/
	@ManyToOne( fetch = FetchType.LAZY,optional=false)
	@JoinColumn(name="patientID")
	private User patient;
	@Transient
	private String patientID;
	
	
	/** 医生 **/
	@ManyToOne( fetch = FetchType.LAZY,optional=false)
	@JoinColumn(name="doctorID")
	private User doctor;
	@Transient
	private String doctorID;
	
	/** 摘要 **/
	private String summary;
	
	/** 诊断内容 **/
	private String diagnosis;
	
	/** 诊疗计划 **/
	private String medicalPlan;
	
	/** 诊疗时间 **/
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date clinicalTime;

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
