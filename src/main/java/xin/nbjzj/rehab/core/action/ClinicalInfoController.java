package xin.nbjzj.rehab.core.action;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tio.utils.json.Json;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import xin.nbjzj.rehab.blockchain.block.Operation;
import xin.nbjzj.rehab.blockchain.block.PairKey;
import xin.nbjzj.rehab.core.advice.exceptions.CheckException;
import xin.nbjzj.rehab.core.entity.ClinicalInfo;
import xin.nbjzj.rehab.core.entity.request.ClinicalInfoReq;
import xin.nbjzj.rehab.core.entity.response.ClinicalInfoResp;
import xin.nbjzj.rehab.core.globle.Constants;
import xin.nbjzj.rehab.core.service.BlockService;
import xin.nbjzj.rehab.core.service.ClinicalInfoRepository;
import xin.nbjzj.rehab.core.service.UserRepository;

@Api(tags = "临床医疗信息相关接口")
@RestController
@RequestMapping("/clinic")
public class ClinicalInfoController {
	private ClinicalInfoRepository clinicalInfoRepository;
	private UserRepository userRepository;
	private BlockService blockService;
	
	public ClinicalInfoController(ClinicalInfoRepository clinicalInfoRepository,UserRepository userRepository,BlockService blockService) {
		super();
		this.clinicalInfoRepository = clinicalInfoRepository;
		this.userRepository = userRepository;
		this.blockService = blockService;
	}
	@ApiOperation(value = "获取全部临床医疗信息" ,  notes="获取全部临床医疗信息,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public List<ClinicalInfoResp> getAll(){
		return clinicalInfoRepository.findAll()
				.stream()
				.map(entity->new ClinicalInfoResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "获取全部临床医疗信息" ,  notes="获取全部临床医疗信息,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public List<ClinicalInfoResp> streamGetAll(){
		return clinicalInfoRepository.findAll()
				.stream()
				.map(entity->new ClinicalInfoResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "新增临床医疗信息" ,  notes="上传必要的临床医疗信息信息来创建一个新的临床医疗信息")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public ClinicalInfoResp add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody ClinicalInfoReq clinicalInfoReq,HttpSession session) {
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		ClinicalInfo clinicalInfo = new ClinicalInfo(clinicalInfoReq);
		//临床医疗信息自定义完整性进行校验
		ClinicalInfoCheck(clinicalInfo);
		
		blockService.constructBlock(Operation.ADD, clinicalInfo, keys);
		
		return new ClinicalInfoResp(clinicalInfo);
		
	}
	
	@ApiOperation(value = "删除临床医疗信息" ,  notes="根据临床医疗信息的clinicalInfo_id来删除一个临床医疗信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "clinicalInfo_id", value = "被操作的目标主键,直接放入地址中,替换{clinicalInfo_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{clinicalInfo_id}")
	public ResponseEntity<Void> delete(@PathVariable("clinicalInfo_id")Long clinicalInfo_id,HttpSession session){
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		return clinicalInfoRepository.findById(clinicalInfo_id)
				.map(entity->{
					blockService.constructBlock(Operation.DELETE, entity, keys);
					return new ResponseEntity<Void>(HttpStatus.OK);})
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新临床医疗信息信息" ,  notes="通过clinicalInfo_id定位临床医疗信息并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "clinicalInfo_id", value = "被操作的目标主键,直接放入地址中,替换{clinicalInfo_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{clinicalInfo_id}")
	public ResponseEntity<ClinicalInfoResp> update(@PathVariable("clinicalInfo_id")Long clinicalInfo_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody ClinicalInfoReq clinicalInfoReq,HttpSession session){
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		
		ClinicalInfo clinicalInfo = new ClinicalInfo(clinicalInfoReq);
		
		return clinicalInfoRepository.findById(clinicalInfo_id)
				.map(entity->{
					if(StringUtils.isNotBlank(clinicalInfo.getDiagnosis())) {
						entity.setDiagnosis(clinicalInfo.getDiagnosis());
					}
					if(StringUtils.isNotBlank(clinicalInfo.getMedicalPlan())) {
						entity.setMedicalPlan(clinicalInfo.getMedicalPlan());
					}
					if(StringUtils.isNotBlank(clinicalInfo.getSummary())) {
						entity.setSummary(clinicalInfo.getSummary());
					}
					if(clinicalInfo.getDoctorID()!=null) {
						entity.setDoctorID(clinicalInfo.getDoctorID());
					}
					if(clinicalInfo.getPatientID()!=null) {
						entity.setPatientID(clinicalInfo.getPatientID());
					}
					ClinicalInfoCheck(entity);
					blockService.constructBlock(Operation.UPDATE, entity, keys);
					return entity;})
				.map(entity->new ResponseEntity<ClinicalInfoResp>(new ClinicalInfoResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找临床医疗信息" ,  notes="根据临床医疗信息clinicalInfo_id查找临床医疗信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "clinicalInfo_id", value = "被操作的目标主键,直接放入地址中,替换{clinicalInfo_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{clinicalInfo_id}")
	public  ResponseEntity<ClinicalInfoResp> findByID(@PathVariable("clinicalInfo_id")Long clinicalInfo_id){
		return clinicalInfoRepository.findById(clinicalInfo_id)
				.map(entity->new ResponseEntity<ClinicalInfoResp>(new ClinicalInfoResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void ClinicalInfoCheck(@Valid ClinicalInfo entity) {
		//patientID
		if(entity.getPatientID()!=null) {
			if(userRepository.existsById(entity.getPatientID())) {
				entity.setPatient(userRepository.findById(entity.getPatientID()).get());
			}
		}
		
		if(entity.getPatient()==null) {
			throw new CheckException("patient_id",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		//doctorID
		if(entity.getDoctorID()!=null) {
			if(userRepository.existsById(entity.getDoctorID())) {
				entity.setDoctor(userRepository.findById(entity.getDoctorID()).get());
			}
		}
		if(entity.getDoctor()==null) {
			throw new CheckException("doctor_id",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		
	}
	
}
