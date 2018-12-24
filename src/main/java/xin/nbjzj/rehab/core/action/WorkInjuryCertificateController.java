package xin.nbjzj.rehab.core.action;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xin.nbjzj.rehab.ApplicationContextProvider;
import xin.nbjzj.rehab.blockchain.block.Operation;
import xin.nbjzj.rehab.blockchain.block.PairKey;
import xin.nbjzj.rehab.core.advice.exceptions.CheckException;
import xin.nbjzj.rehab.core.entity.ClinicalInfo;
import xin.nbjzj.rehab.core.entity.User;
import xin.nbjzj.rehab.core.entity.WorkInjuryCertificate;
import xin.nbjzj.rehab.core.entity.request.WorkInjuryCertificateReq;
import xin.nbjzj.rehab.core.entity.response.WorkInjuryCertificateResp;
import xin.nbjzj.rehab.core.globle.Constants;
import xin.nbjzj.rehab.core.service.BlockService;
import xin.nbjzj.rehab.core.service.ClinicalInfoRepository;
import xin.nbjzj.rehab.core.service.UserRepository;
import xin.nbjzj.rehab.core.service.WorkInjuryCertificateRepository;
import xin.nbjzj.rehab.socket.distruptor.base.MessageProducer;

@Api(tags = "工伤认定相关接口")
@RestController
@RequestMapping("/work")
public class WorkInjuryCertificateController {
	private WorkInjuryCertificateRepository workInjuryCertificateRepository;
	private ClinicalInfoRepository clinicalInfoRepository;
	private UserRepository userRepository;
	private BlockService blockService;
	
	public WorkInjuryCertificateController(WorkInjuryCertificateRepository workInjuryCertificateRepository,ClinicalInfoRepository clinicalInfoRepository,UserRepository userRepository,BlockService blockService) {
		super();
		this.workInjuryCertificateRepository = workInjuryCertificateRepository;
		this.clinicalInfoRepository = clinicalInfoRepository;
		this.userRepository = userRepository;
		this.blockService = blockService;
	}
	
	@ApiOperation(value = "获取全部工伤认定" ,  notes="获取全部工伤认定,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public List<WorkInjuryCertificateResp> getAll(){
		return workInjuryCertificateRepository.findAll()
				.stream()
				.map(entity->new WorkInjuryCertificateResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "获取全部工伤认定" ,  notes="获取全部工伤认定,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public List<WorkInjuryCertificateResp> streamGetAll(){
		return workInjuryCertificateRepository.findAll()
				.stream()
				.map(entity->new WorkInjuryCertificateResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "新增工伤认定" ,  notes="上传必要的工伤认定信息来创建一个新的工伤认定")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public WorkInjuryCertificateResp add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody WorkInjuryCertificateReq workInjuryCertificateReq,HttpSession session) {
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		WorkInjuryCertificate workInjuryCertificate = new WorkInjuryCertificate(workInjuryCertificateReq);
		//工伤认定自定义完整性进行校验
		WorkInjuryCertificateCheck(workInjuryCertificate);
		blockService.constructBlock(Operation.ADD, workInjuryCertificate, keys);
		return new WorkInjuryCertificateResp(workInjuryCertificate);
	}
	
	@ApiOperation(value = "删除工伤认定" ,  notes="根据工伤认定的WorkInjuryCertificate_id来删除一个工伤认定")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "workInjuryCertificate_id", value = "被操作的目标主键,直接放入地址中,替换{WorkInjuryCertificate_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{workInjuryCertificate_id}")
	public ResponseEntity<Void> delete(@PathVariable("workInjuryCertificate_id")Long workInjuryCertificate_id,HttpSession session){
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		return workInjuryCertificateRepository.findById(workInjuryCertificate_id)
				.map(entity->{
					blockService.constructBlock(Operation.DELETE, entity, keys);
					return new ResponseEntity<Void>(HttpStatus.OK);})
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新工伤认定信息" ,  notes="通过WorkInjuryCertificate_id定位工伤认定并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "workInjuryCertificate_id", value = "被操作的目标主键,直接放入地址中,替换{WorkInjuryCertificate_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{workInjuryCertificate_id}")
	public ResponseEntity<WorkInjuryCertificateResp> update(@PathVariable("workInjuryCertificate_id")Long workInjuryCertificate_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody WorkInjuryCertificateReq workInjuryCertificateReq,HttpSession session){
		WorkInjuryCertificate workInjuryCertificate = new WorkInjuryCertificate(workInjuryCertificateReq);
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		return workInjuryCertificateRepository.findById(workInjuryCertificate_id)
				.map(entity->{
					if(StringUtils.isNotBlank(workInjuryCertificate.getAccidentPlace())) {
						entity.setAccidentPlace(workInjuryCertificate.getAccidentPlace());
					}
					
					if(workInjuryCertificate.getAdminID()!=null) {
						entity.setAdminID(workInjuryCertificate.getAdminID());
					}
					if(StringUtils.isNotBlank(workInjuryCertificate.getAccidentProcess())) {
						entity.setAccidentProcess(workInjuryCertificate.getAccidentProcess());
					}
					if(StringUtils.isNotBlank(workInjuryCertificate.getAdminOpinion())) {
						entity.setAdminOpinion(workInjuryCertificate.getAdminOpinion());
					}
					if(StringUtils.isNotBlank(workInjuryCertificate.getDisabilityLevel())) {
						entity.setDisabilityLevel(workInjuryCertificate.getDisabilityLevel());
					}
					if(StringUtils.isNotBlank(workInjuryCertificate.getInjuredPart())) {
						entity.setInjuredPart(workInjuryCertificate.getInjuredPart());
					}
					if(StringUtils.isNotBlank(workInjuryCertificate.getInstitutionOpinion())) {
						entity.setInstitutionOpinion(workInjuryCertificate.getInstitutionOpinion());
					}
					
					entity.setWorkInjuryCertificateResult(workInjuryCertificate.isWorkInjuryCertificateResult());
					
					
					
					WorkInjuryCertificateCheck(entity);
					blockService.constructBlock(Operation.UPDATE, entity, keys);
					return entity;
				})
				.map(entity->new ResponseEntity<WorkInjuryCertificateResp>(new WorkInjuryCertificateResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找工伤认定" ,  notes="根据工伤认定WorkInjuryCertificate_id查找工伤认定")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "workInjuryCertificate_id", value = "被操作的目标主键,直接放入地址中,替换{WorkInjuryCertificate_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{workInjuryCertificate_id}")
	public  ResponseEntity<WorkInjuryCertificateResp> findByID(@PathVariable("workInjuryCertificate_id")Long WorkInjuryCertificate_id){
		return workInjuryCertificateRepository.findById(WorkInjuryCertificate_id)
				.map(entity->new ResponseEntity<WorkInjuryCertificateResp>(new WorkInjuryCertificateResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void WorkInjuryCertificateCheck(@Valid WorkInjuryCertificate entity) {
		//adminID
		if(entity.getAdminID()!=null) {
			if(userRepository.existsById(entity.getAdminID())) {
				entity.setAdmin(userRepository.findById(entity.getAdminID()).get());
			}
		}
		if(entity.getAdmin()==null) {
			throw new CheckException("admin_id",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		
		//clinicalInfoID
		if(entity.getClinicalInfoID()!=null) {
			if(userRepository.existsById(entity.getClinicalInfoID())) {
				entity.setClinicalInfo(clinicalInfoRepository.findById(entity.getClinicalInfoID()).get());
			}
		}
		if(entity.getClinicalInfo()==null) {
			throw new CheckException("clinicalInfo_id",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
	}
	
}
