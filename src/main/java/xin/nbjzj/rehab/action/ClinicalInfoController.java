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
import xin.nbjzj.rehab.entity.request.ClinicalInfoReq;
import xin.nbjzj.rehab.entity.response.ClinicalInfoResp;
import xin.nbjzj.rehab.entity.ClinicalInfo;
import xin.nbjzj.rehab.service.reactive.ClinicalInfoReactive;

@Api(tags = "临床医疗信息相关接口")
@RestController
@RequestMapping("/clinic")
public class ClinicalInfoController {
	private ClinicalInfoReactive clinicalInfoReactive;

	public ClinicalInfoController(ClinicalInfoReactive clinicalInfoReactive) {
		super();
		this.clinicalInfoReactive = clinicalInfoReactive;
	}
	@ApiOperation(value = "获取全部临床医疗信息" ,  notes="获取全部临床医疗信息,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<ClinicalInfoResp> getAll(){
		return clinicalInfoReactive.findAll().map(entity->new ClinicalInfoResp(entity));
	}
	
	
	@ApiOperation(value = "获取全部临床医疗信息" ,  notes="获取全部临床医疗信息,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ClinicalInfoResp> streamGetAll(){
		return clinicalInfoReactive.findAll().map(entity->new ClinicalInfoResp(entity));
	}
	
	
	@ApiOperation(value = "新增临床医疗信息" ,  notes="上传必要的临床医疗信息信息来创建一个新的临床医疗信息")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<ClinicalInfoResp> add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody ClinicalInfoReq clinicalInfoReq) {
		ClinicalInfo clinicalInfo = new ClinicalInfo(clinicalInfoReq);
		//临床医疗信息自定义完整性进行校验
		ClinicalInfoCheck(clinicalInfo);
		return clinicalInfoReactive.save(clinicalInfo).map(entity->new ClinicalInfoResp(entity));
	}
	
	@ApiOperation(value = "删除临床医疗信息" ,  notes="根据临床医疗信息的clinicalInfo_id来删除一个临床医疗信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "clinicalInfo_id", value = "被操作的目标主键,直接放入地址中,替换{clinicalInfo_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{clinicalInfo_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("clinicalInfo_id")String clinicalInfo_id){
		return clinicalInfoReactive.findById(clinicalInfo_id)
				.flatMap(clinicalInfo->clinicalInfoReactive.delete(clinicalInfo)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新临床医疗信息信息" ,  notes="通过clinicalInfo_id定位临床医疗信息并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "clinicalInfo_id", value = "被操作的目标主键,直接放入地址中,替换{clinicalInfo_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{clinicalInfo_id}")
	public Mono<ResponseEntity<ClinicalInfoResp>> update(@PathVariable("clinicalInfo_id")String clinicalInfo_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody ClinicalInfoReq clinicalInfoReq){
		ClinicalInfo clinicalInfo = new ClinicalInfo(clinicalInfoReq);
		return clinicalInfoReactive.findById(clinicalInfo_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(clinicalInfo.getDiagnosis())) {
						entity.setDiagnosis(clinicalInfo.getDiagnosis());
					}
					if(StringUtils.isNotBlank(clinicalInfo.getMedicalPlan())) {
						entity.setMedicalPlan(clinicalInfo.getMedicalPlan());
					}
					if(StringUtils.isNotBlank(clinicalInfo.getSummary())) {
						entity.setSummary(clinicalInfo.getSummary());
					}
					
					
					
					ClinicalInfoCheck(entity);
					return clinicalInfoReactive.save(entity);
				})
				.map(entity->new ResponseEntity<ClinicalInfoResp>(new ClinicalInfoResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找临床医疗信息" ,  notes="根据临床医疗信息clinicalInfo_id查找临床医疗信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "clinicalInfo_id", value = "被操作的目标主键,直接放入地址中,替换{clinicalInfo_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = ClinicalInfoResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{clinicalInfo_id}")
	public  Mono<ResponseEntity<ClinicalInfoResp>> findByID(@PathVariable("clinicalInfo_id")String clinicalInfo_id){
		return clinicalInfoReactive.findById(clinicalInfo_id)
				.map(entity->new ResponseEntity<ClinicalInfoResp>(new ClinicalInfoResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void ClinicalInfoCheck(@Valid ClinicalInfo entity) {
		
	}
	
}
