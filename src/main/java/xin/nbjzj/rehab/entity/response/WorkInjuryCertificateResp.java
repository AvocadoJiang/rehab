package xin.nbjzj.rehab.entity.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.nbjzj.rehab.entity.WorkInjuryCertificate;

@ApiModel(value="workInjuryCertificateResp",description="工伤认定")
@Data
public class WorkInjuryCertificateResp implements Serializable {

	@ApiModelProperty(value="主键")
	private String workInjuryCertificate_id;
	
	@ApiModelProperty(value="病人姓名（要病人其他信息和蒋周杰说）")
	private String patient_name;
	
	@ApiModelProperty(value="受理机构姓名（要受理机构其他信息和蒋周杰说）")
	private String admin_name;
	
	//ClinicalInfo临川治疗信息
	@ApiModelProperty(value="摘要")
	private String summary;
	
	@ApiModelProperty(value="诊断内容")
	private String diagnosis;
	
	@ApiModelProperty(value="诊疗计划")
	private String medical_plan;
	
	@ApiModelProperty(value="诊疗时间")
	private String clinical_time;
	
	//工伤认定信息
	@ApiModelProperty(value="事故地点")
	private String accident_place;
	
	@ApiModelProperty(value="事故时间")
	private Date accident_time;
	
	@ApiModelProperty(value="受伤害经过简述（事故时间、地点、受伤原因、受伤过程、诊断结论）")
	private String accident_process;
	
	@ApiModelProperty(value="受伤害部位")
	private String injured_part;
	
	@ApiModelProperty(value="用人单位意见")
	private String institution_opinion;
	
	
	//认定结果
	@ApiModelProperty(value="受理机关意见")
	private String admin_opinion;
	
	@ApiModelProperty(value="工伤认定决定书结果")
	private boolean workInjuryCertificate_result;
	
	@ApiModelProperty(value="伤残等级")
	private String disability_level;

	public WorkInjuryCertificateResp(WorkInjuryCertificate entity) {
		super();
		this.workInjuryCertificate_id = entity.getWorkInjuryCertificateID();
		this.patient_name = entity.getClinicalInfo().getPatient().getUserName();
		this.admin_name = entity.getAdmin().getUserName();
		this.summary = entity.getClinicalInfo().getSummary();
		this.diagnosis = entity.getClinicalInfo().getDiagnosis();
		this.medical_plan = entity.getClinicalInfo().getMedicalPlan();
		this.clinical_time = entity.getClinicalInfo().getClinicalTime();
		this.accident_place = entity.getAccidentPlace();
		this.accident_time = entity.getAccidentTime();
		this.accident_process = entity.getAccidentProcess();
		this.injured_part = entity.getInjuredPart();
		this.institution_opinion = entity.getInstitutionOpinion();
		this.admin_opinion = entity.getAdminOpinion();
		this.workInjuryCertificate_result = entity.isWorkInjuryCertificateResult();
		this.disability_level = entity.getDisabilityLevel();
	}
	
	
	
}
