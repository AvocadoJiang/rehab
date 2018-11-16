package xin.nbjzj.rehab.entity.response;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xin.nbjzj.rehab.entity.RehabApplication;

@ApiModel(value="rehabilitationApplicationResp",description="工伤康复申请")
@Data
public class RehabApplicationResp implements Serializable {
	@ApiModelProperty(value="工伤康复申请ID")
	@NotBlank
	private String rehabilitationApplication_id;
	
	@ApiModelProperty(value="申请康复治疗理由")
	private String rehabilitation_reason;
	
	@ApiModelProperty(value="主治医生姓名")
	private String doctor_name;
	
	@ApiModelProperty(value="主治医生意见")
	private String doctor_opinion;
	
	@ApiModelProperty(value="工伤康复申请结果")
	private boolean rehabilitationApplication_result;
	
	@ApiModelProperty(value="患者姓名")
	private String patient_name;
	
	
	//工伤认定信息
	@ApiModelProperty(value="工伤认定决定书结果")
	private boolean workInjuryCertificate_result;
	
	@ApiModelProperty(value="伤残等级")
	private String disability_level;

	public RehabApplicationResp(RehabApplication entity) {
		super();
		this.rehabilitationApplication_id = entity.getRehabilitationApplicationID();
		this.rehabilitation_reason = entity.getRehabilitationReason();
		this.doctor_name = entity.getDoctor().getUserName();
		this.doctor_opinion = entity.getDoctorOpinion();
		this.rehabilitationApplication_result = entity.isRehabilitationApplicationResult();
		this.patient_name = entity.getWorkInjuryCertificate().getPatient().getUserName();
		this.workInjuryCertificate_result = entity.getWorkInjuryCertificate().isWorkInjuryCertificateResult();
		this.disability_level = entity.getWorkInjuryCertificate().getDisabilityLevel();
	}
	
	
}
