package xin.nbjzj.rehab.action;



import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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


import xin.nbjzj.rehab.service.reactive.UserReactive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import xin.nbjzj.rehab.entity.request.UserReq;
import xin.nbjzj.rehab.entity.response.UserResp;
import xin.nbjzj.rehab.entity.User;
import xin.nbjzj.rehab.entity.User.USER_IDENTITY;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
	private UserReactive userReactive;
	
	
	@Value("${userphoto.savepath}")
	private String savepath;
	
	@Value("${userphoto.urlprefix}")
	private String urlprefix;
	
	/**
	 * 构造函数 构造器注入
	 * @param academyRepository
	 */
	public UserController(UserReactive userReactive) {
		super();
		this.userReactive = userReactive;		
	}
	
	
	@ApiOperation(value = "获取全部用户" ,  notes="获取全部用户,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public Flux<UserResp> getAll(){
		return userReactive.findAll().map(entity->new UserResp(entity));
	}
	
	
	@ApiOperation(value = "获取全部用户" ,  notes="获取全部用户,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<UserResp> streamGetAll(){
		return userReactive.findAll().map(entity->new UserResp(entity));
	}
	
	
	@ApiOperation(value = "新增用户" ,  notes="上传必要的用户信息来创建一个新的用户")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public Mono<UserResp> add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody UserReq userReq) {
		User user = new User(userReq);
		//用户自定义完整性进行校验
		UserCheck(user);
		return userReactive.save(user).map(entity->new UserResp(entity));
	}
	
	@ApiOperation(value = "删除用户" ,  notes="根据用户的user_id来删除一个用户")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "user_id", value = "被操作的目标主键,直接放入地址中,替换{user_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{user_id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("user_id")String user_id){
		return userReactive.findById(user_id)
				.flatMap(entity->userReactive.delete(entity)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "更新用户信息" ,  notes="通过user_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "user_id", value = "被操作的目标主键,直接放入地址中,替换{user_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{user_id}")
	public Mono<ResponseEntity<UserResp>> update(@PathVariable("user_id")String user_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody UserReq userReq){
		User user = new User(userReq);
		return userReactive.findById(user_id)
				.flatMap(entity->{
					if(StringUtils.isNotBlank(user.getPassword())) {
						entity.setPassword(user.getPassword());
					}
					if(StringUtils.isNotBlank(user.getUserName())) {
						entity.setUserName(user.getUserName());
					}
					if(StringUtils.isNotBlank(user.getAddress())) {
						entity.setAddress(user.getAddress());
					}
					if(StringUtils.isNotBlank(user.getGender())) {
						entity.setGender(user.getGender());
					}
					if(StringUtils.isNotBlank(user.getIdNumber())) {
						entity.setIdNumber(user.getIdNumber());
					}
					if(StringUtils.isNotBlank(user.getInstitution())) {
						entity.setInstitution(user.getInstitution());
					}
					
					
					
					UserCheck(entity);
					return userReactive.save(entity);
				})
				.map(entity->new ResponseEntity<UserResp>(new UserResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找用户" ,  notes="根据用户user_id查找用户")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "user_id", value = "被操作的目标主键,直接放入地址中,替换{user_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{user_id}")
	public  Mono<ResponseEntity<UserResp>> findByID(@PathVariable("user_id")String user_id){
		return userReactive.findById(user_id)
				.map(entity->new ResponseEntity<UserResp>(new UserResp(entity),HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void UserCheck(@Valid User entity) {
		USER_IDENTITY identity = User.getUserTypeEnum(entity.getIdentity());
		
	}
	
}
