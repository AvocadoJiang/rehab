package xin.nbjzj.rehab.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import xin.nbjzj.rehab.entity.request.RehabApplicationReq;

/**
 * 工伤康复申请实体类
 * @author Jason Chiang
 *
 */
@Document(collection="rehabilitationApplication")
@Data
public class RehabApplication {
	@Id
	private String rehabilitationApplicationID;
	
	@DBRef
	/** 工伤认定决定书 **/
	private WorkInjuryCertificate workInjuryCertificate;
	
	private String workInjuryCertificateID;
	
	/** 申请康复治疗理由 **/
	private String rehabilitationReason;
	
	//申请结果
	@DBRef
	/** 主治医生 **/
	private User doctor;
	private String doctorID;
	
	/** 主治医生意见 **/
	private String doctorOpinion;
	
	/** 工伤康复申请结果 **/
	private boolean rehabilitationApplicationResult;

	public RehabApplication() {
		super();
	}
	
	public RehabApplication(RehabApplicationReq req) {
		super();
		this.workInjuryCertificateID = req.getWorkInjuryCertificate_id();
		this.rehabilitationReason = req.getRehabilitation_reason();
		this.doctorID = req.getDoctor_id();
		this.doctorOpinion = req.getDoctor_opinion();
		this.rehabilitationApplicationResult = req.isRehabilitationApplication_result();
		
	}
	
	
	
	
	
}
