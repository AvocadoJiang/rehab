package xin.nbjzj.rehab.action;

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
import xin.nbjzj.rehab.entity.request.WorkInjuryCertificateReq;
import xin.nbjzj.rehab.entity.response.WorkInjuryCertificateResp;
import xin.nbjzj.rehab.entity.WorkInjuryCertificate;
import xin.nbjzj.rehab.service.reactive.WorkInjuryCertificateReactive;

@Api(tags = "工伤认定相关接口")
@RestController
@RequestMapping("/work")
public class WorkInjuryCertificateController {
	private WorkInjuryCertificateReactive workInjuryCertificateReactive;

	public WorkInjuryCertificateController(WorkInjuryCertificateReactive workInjuryCertificateReactive) {
		super();
		this.workInjuryCertificateReactive = workInjuryCertificateReactive;
	}
	
	@ApiOperation(value = "获取全部用户" ,  notes="获取全部用户,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<WorkInjuryCertificateResp> getAll(){
		return workInjuryCertificateReactive.findAll().map(entity->new WorkInjuryCertificateResp(entity));
	}
	
	
	@ApiOperation(value = "获取全部用户" ,  notes="获取全部用户,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<WorkInjuryCertificateResp> streamGetAll(){
		return workInjuryCertificateReactive.findAll().map(entity->new WorkInjuryCertificateResp(entity));
	}
	
	
	@ApiOperation(value = "新增用户" ,  notes="上传必要的用户信息来创建一个新的用户")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<WorkInjuryCertificateResp> add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody WorkInjuryCertificateReq workInjuryCertificateReq) {
		WorkInjuryCertificate workInjuryCertificate = new WorkInjuryCertificate(workInjuryCertificateReq);
		//用户自定义完整性进行校验
		WorkInjuryCertificateCheck(workInjuryCertificate);
		return workInjuryCertificateReactive.save(workInjuryCertificate).map(entity->new WorkInjuryCertificateResp(entity));
	}
	
	@ApiOperation(value = "删除用户" ,  notes="根据用户的WorkInjuryCertificate_id来删除一个用户")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "WorkInjuryCertificate_id", value = "被操作的目标主键,直接放入地址中,替换{WorkInjuryCertificate_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{workInjuryCertificate_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("workInjuryCertificate_id")String workInjuryCertificate_id){
		return workInjuryCertificateReactive.findById(workInjuryCertificate_id)
				.flatMap(entity->workInjuryCertificateReactive.delete(entity)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新用户信息" ,  notes="通过WorkInjuryCertificate_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "workInjuryCertificate_id", value = "被操作的目标主键,直接放入地址中,替换{WorkInjuryCertificate_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{workInjuryCertificate_id}")
	public Mono<ResponseEntity<WorkInjuryCertificateResp>> update(@PathVariable("workInjuryCertificate_id")String workInjuryCertificate_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody WorkInjuryCertificateReq workInjuryCertificateReq){
		WorkInjuryCertificate workInjuryCertificate = new WorkInjuryCertificate(workInjuryCertificateReq);
		return workInjuryCertificateReactive.findById(workInjuryCertificate_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(workInjuryCertificate.getAccidentPlace())) {
						entity.setAccidentPlace(workInjuryCertificate.getAccidentPlace());
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
					
					
					
					WorkInjuryCertificateCheck(entity);
					return workInjuryCertificateReactive.save(entity);
				})
				.map(entity->new ResponseEntity<WorkInjuryCertificateResp>(new WorkInjuryCertificateResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找用户" ,  notes="根据用户WorkInjuryCertificate_id查找用户")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "workInjuryCertificate_id", value = "被操作的目标主键,直接放入地址中,替换{WorkInjuryCertificate_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = WorkInjuryCertificateResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{workInjuryCertificate_id}")
	public  Mono<ResponseEntity<WorkInjuryCertificateResp>> findByID(@PathVariable("workInjuryCertificate_id")String WorkInjuryCertificate_id){
		return workInjuryCertificateReactive.findById(WorkInjuryCertificate_id)
				.map(entity->new ResponseEntity<WorkInjuryCertificateResp>(new WorkInjuryCertificateResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void WorkInjuryCertificateCheck(@Valid WorkInjuryCertificate entity) {
		
	}
	
}
