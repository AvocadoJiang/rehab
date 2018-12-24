package xin.nbjzj.rehab.core.entity.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.nbjzj.rehab.core.entity.ClinicalInfo;

@ApiModel(value="clinicalInfoResp",description="临床医疗信息")
@Data
public class ClinicalInfoResp implements Serializable {
	
	@ApiModelProperty(value="临床医疗信息ID")
	private String clinicalInfo_id;
	
	@ApiModelProperty(value="病人姓名（要病人其他信息和蒋周杰说）")
	private String patient_name;
	
	@ApiModelProperty(value="医生姓名（要医生其他信息和蒋周杰说）")
	private String doctor_name;
	
	@ApiModelProperty(value="摘要")
	private String summary;
	
	@ApiModelProperty(value="诊断内容")
	private String diagnosis;
	
	@ApiModelProperty(value="诊疗计划")
	private String medical_plan;
	
	@ApiModelProperty(value="诊疗时间")
	private Date clinical_time;
	
	public ClinicalInfoResp(ClinicalInfo entity) {
		super();
		this.clinicalInfo_id = entity.getClinicalInfoID();
		this.patient_name = entity.getPatient().getUserName();
		this.doctor_name = entity.getDoctor().getUserName();
		this.summary = entity.getSummary();
		this.diagnosis = entity.getDiagnosis();
		this.medical_plan = entity.getMedicalPlan();
		this.clinical_time = entity.getClinicalTime();
	}
	
}
