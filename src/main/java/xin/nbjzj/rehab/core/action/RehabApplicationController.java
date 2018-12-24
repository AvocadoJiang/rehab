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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xin.nbjzj.rehab.blockchain.block.Operation;
import xin.nbjzj.rehab.blockchain.block.PairKey;
import xin.nbjzj.rehab.core.advice.exceptions.CheckException;
import xin.nbjzj.rehab.core.entity.RehabApplication;
import xin.nbjzj.rehab.core.entity.request.RehabApplicationReq;
import xin.nbjzj.rehab.core.entity.response.RehabApplicationResp;
import xin.nbjzj.rehab.core.entity.response.UserResp;
import xin.nbjzj.rehab.core.globle.Constants;
import xin.nbjzj.rehab.core.service.UserRepository;
import xin.nbjzj.rehab.core.service.WorkInjuryCertificateRepository;
import xin.nbjzj.rehab.core.service.BlockService;
import xin.nbjzj.rehab.core.service.RehabApplicationRepository;

@Api(tags = "工伤康复申请相关接口")
@RestController
@RequestMapping("/rehab")
public class RehabApplicationController {
	RehabApplicationRepository rehabApplicationRepository;
	private WorkInjuryCertificateRepository workInjuryCertificateRepository;
	private UserRepository userRepository;
	private BlockService blockService;
	
	public RehabApplicationController(RehabApplicationRepository rehabApplicationRepository,UserRepository userRepository,WorkInjuryCertificateRepository workInjuryCertificateRepository,BlockService blockService) {
		super();
		this.rehabApplicationRepository = rehabApplicationRepository;
		this.userRepository = userRepository;
		this.workInjuryCertificateRepository = workInjuryCertificateRepository;
		this.blockService = blockService;
	}
	@ApiOperation(value = "获取全部工伤康复申请" ,  notes="获取全部工伤康复申请,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = RehabApplicationResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public List<RehabApplicationResp> getAll(){
		return rehabApplicationRepository.findAll()
				.stream()
				.map(entity->new RehabApplicationResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "获取全部工伤康复申请" ,  notes="获取全部工伤康复申请,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = RehabApplicationResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public List<RehabApplicationResp> streamGetAll(){
		return rehabApplicationRepository.findAll()
				.stream()
				.map(entity->new RehabApplicationResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "新增工伤康复申请" ,  notes="上传必要的工伤康复申请信息来创建一个新的工伤康复申请")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = RehabApplicationResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public RehabApplicationResp add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody RehabApplicationReq rehabApplicationReq,HttpSession session) {
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		RehabApplication rehabApplication = new RehabApplication(rehabApplicationReq);
		//工伤康复申请自定义完整性进行校验
		RehabApplicationCheck(rehabApplication);
		blockService.constructBlock(Operation.ADD, rehabApplication, keys);
		return new RehabApplicationResp(rehabApplication);
	}
	
	@ApiOperation(value = "删除工伤康复申请" ,  notes="根据工伤康复申请的rehabApplication_id来删除一个工伤康复申请")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "rehabApplication_id", value = "被操作的目标主键,直接放入地址中,替换{rehabApplication_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{rehabApplication_id}")
	public ResponseEntity<Void> delete(@PathVariable("rehabApplication_id")String rehabApplication_id,HttpSession session){
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		return rehabApplicationRepository.findById(rehabApplication_id)
				.map(entity->{
					blockService.constructBlock(Operation.DELETE, entity, keys);
					return new ResponseEntity<Void>(HttpStatus.OK);})
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新工伤康复申请信息" ,  notes="通过rehabApplication_id定位工伤康复申请并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "rehabApplication_id", value = "被操作的目标主键,直接放入地址中,替换{rehabApplication_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = RehabApplicationResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{rehabApplication_id}")
	public ResponseEntity<RehabApplicationResp> update(@PathVariable("rehabApplication_id")String rehabApplication_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody RehabApplicationReq rehabApplicationReq,HttpSession session){
		RehabApplication rehabApplication = new RehabApplication(rehabApplicationReq);
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		return rehabApplicationRepository.findById(rehabApplication_id)
				.map(entity->{
					if(StringUtils.isNotBlank(rehabApplication.getDoctorOpinion())) {
						entity.setDoctorOpinion(rehabApplication.getDoctorOpinion());
					}
					
					if(StringUtils.isNotBlank(rehabApplication.getDoctorID())) {
						entity.setDoctorID(rehabApplication.getDoctorID());
					}
					
					if(StringUtils.isNotBlank(rehabApplication.getRehabilitationReason())) {
						entity.setRehabilitationReason(rehabApplication.getRehabilitationReason());
					}
					
					entity.setRehabilitationApplicationResult(rehabApplication.isRehabilitationApplicationResult());
					
					RehabApplicationCheck(entity);
					blockService.constructBlock(Operation.UPDATE, entity, keys);
					return entity;
				})
				.map(entity->new ResponseEntity<RehabApplicationResp>(new RehabApplicationResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找工伤康复申请" ,  notes="根据工伤康复申请rehabApplication_id查找工伤康复申请")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "rehabApplication_id", value = "被操作的目标主键,直接放入地址中,替换{rehabApplication_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = RehabApplicationResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{rehabApplication_id}")
	public  ResponseEntity<RehabApplicationResp> findByID(@PathVariable("rehabApplication_id")String rehabApplication_id){
		return rehabApplicationRepository.findById(rehabApplication_id)
				.map(entity->new ResponseEntity<RehabApplicationResp>(new RehabApplicationResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void RehabApplicationCheck(@Valid RehabApplication entity) {
		//doctorID
		if(StringUtils.isNotBlank(entity.getDoctorID())) {
			if(!userRepository.existsById(entity.getDoctorID())) {
				throw new CheckException("doctor_id",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
			}
			entity.setDoctor(userRepository.findById(entity.getDoctorID()).get());
		}
		
		
		//workInjuryCertificateID
		if(!workInjuryCertificateRepository.existsById(entity.getWorkInjuryCertificateID())) {
			throw new CheckException("workInjuryCertificate_id",Constants.REFERENTIAL_INTEGRITY_CHECK_FAILED);
		}
		
		entity.setWorkInjuryCertificate(workInjuryCertificateRepository.findById(entity.getWorkInjuryCertificateID()).get());
		
		
	}
}
