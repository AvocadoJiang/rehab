package xin.nbjzj.rehab.core.entity.request;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="rehabilitationApplicationReq",description="工伤康复申请")
@Data
public class RehabApplicationReq {
	
	@ApiModelProperty(value="工伤认定ID")
	@NotBlank
	private String workInjuryCertificate_id;
	
	@ApiModelProperty(value="申请康复治疗理由")
	private String rehabilitation_reason;
	
	@ApiModelProperty(value="主治医生ID")
	private String doctor_id;
	
	@ApiModelProperty(value="主治医生意见")
	private String doctor_opinion;
	
	@ApiModelProperty(value="工伤康复申请结果")
	private boolean rehabilitationApplication_result;

	
	
	
}
