package xin.nbjzj.rehab.core.entity.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="workInjuryCertificateReq",description="工伤认定")
@Data
public class WorkInjuryCertificateReq {
	
	
	@ApiModelProperty(value="临床医疗信息ID")
	@NotNull
	private Long clinicalInfo_id;
	
	@ApiModelProperty(value="事故地点",example="万科广场建筑工地")
	private String accident_place;
	
	@ApiModelProperty(value="事故时间",example="2018-11-16 08:00")
	private Date accident_time;
	
	@ApiModelProperty(value="受伤害经过简述（事故时间、地点、受伤原因、受伤过程、诊断结论）")
	private String accident_process;
	
	@ApiModelProperty(value="受伤害部位",example="手肘")
	private String injured_part;
	
	@ApiModelProperty(value="用人单位意见")
	private String institution_opinion;
	
	
	//认定结果
	@ApiModelProperty(value="受理机构ID")
	private Long admin_id;
	
	@ApiModelProperty(value="受理机关意见")
	private String admin_opinion;
	
	@ApiModelProperty(value="工伤认定决定书结果")
	private boolean workInjuryCertificate_result;
	
	@ApiModelProperty(value="伤残等级")
	private String disability_level;
}
