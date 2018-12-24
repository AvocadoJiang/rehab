package xin.nbjzj.rehab.core.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import xin.nbjzj.rehab.core.entity.request.RehabApplicationReq;

/**
 * 工伤康复申请实体类
 * @author Jason Chiang
 *
 */
@Entity
@Table(name="rehabapplication")
@Data
public class RehabApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rehabilitationApplicationID;
	
	
	/** 工伤认定决定书 **/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="workInjuryCertificateID")
	private WorkInjuryCertificate workInjuryCertificate;
	@Transient
	private Long workInjuryCertificateID;
	
	/** 申请康复治疗理由 **/
	private String rehabilitationReason;
	
	//申请结果
	/** 主治医生 **/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="doctorID")
	private User doctor;
	@Transient
	private Long doctorID;
	
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
