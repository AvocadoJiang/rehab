package xin.nbjzj.rehab.core.entity.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="clinicalInfoReq",description="临床医疗信息")
@Data
public class ClinicalInfoReq {
	
	@ApiModelProperty(value="病人用户的ID")
	@NotBlank
	private String patient_id;
	
	@ApiModelProperty(value="医生用户的ID")
	@NotBlank
	private String doctor_id;
	
	@ApiModelProperty(value="摘要",example="病人情况良好，情绪稳定")
	private String summary;
	
	@ApiModelProperty(value="诊断内容",example="多器官功能障碍，重度营养不良")
	private String diagnosis;
	
	@ApiModelProperty(value="诊疗计划",example="黑天吃白片，不瞌睡；白天吃黑片，睡得香。")
	private String medical_plan;
	
	@ApiModelProperty(value="诊疗时间",example="2018-11-16")
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date clinical_time;
}
