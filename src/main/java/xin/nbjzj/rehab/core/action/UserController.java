package xin.nbjzj.rehab.core.action;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.BlockBody;
import xin.nbjzj.rehab.blockchain.block.BlockHeader;
import xin.nbjzj.rehab.blockchain.block.Instruction;
import xin.nbjzj.rehab.blockchain.block.Operation;
import xin.nbjzj.rehab.blockchain.block.PairKey;
import xin.nbjzj.rehab.blockchain.block.db.DbStore;
import xin.nbjzj.rehab.blockchain.block.merkle.MerkleTree;
import xin.nbjzj.rehab.blockchain.common.CommonUtil;
import xin.nbjzj.rehab.blockchain.common.Sha256;
import xin.nbjzj.rehab.blockchain.common.TrustSDK;
import xin.nbjzj.rehab.blockchain.common.exception.TrustSDKException;
import xin.nbjzj.rehab.blockchain.manager.DbBlockManager;
import xin.nbjzj.rehab.core.entity.User;
import xin.nbjzj.rehab.core.entity.User.USER_IDENTITY;
import xin.nbjzj.rehab.core.entity.request.UserReq;
import xin.nbjzj.rehab.core.entity.response.UserResp;
import xin.nbjzj.rehab.core.service.BlockService;
import xin.nbjzj.rehab.core.service.UserRepository;
import xin.nbjzj.rehab.socket.body.RpcBlockBody;
import xin.nbjzj.rehab.socket.client.PacketSender;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.PacketBuilder;
import xin.nbjzj.rehab.socket.packet.PacketType;
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
	private UserRepository userRepository;
	private BlockService blockService;
	
	
	@Value("${userphoto.savepath}")
	private String savepath;
	
	@Value("${userphoto.urlprefix}")
	private String urlprefix;
	
	/**
	 * 构造函数 构造器注入
	 * @param academyRepository
	 */
	public UserController(UserRepository userRepository,BlockService blockService) {
		super();
		this.userRepository = userRepository;		
		this.blockService = blockService;		
	}
	
	
	@ApiOperation(value = "获取全部用户" ,  notes="获取全部用户,以数组形式一次性返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/all")
	public List<UserResp> getAll() {
		return userRepository.findAll()
				.stream()
				.map(entity->new UserResp(entity))
				.collect(Collectors.toList());
	}
	

	
	@ApiOperation(value = "根据身份获取相应用户" ,  notes="根据身份获取相应用户,以数组形式一次性返回数据")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "identity", value = "被操作的目标主键,直接放入地址中,替换{identity}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/identity/{identity}")
	public List<UserResp> getPatient(@PathVariable("identity")String identity){
		return userRepository.findByIdentity(identity.toUpperCase())
				.stream()
				.map(entity->new UserResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "获取全部用户" ,  notes="获取全部用户,以SSE形式多次返回数据")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping(value="/stream/all",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public List<UserResp> streamGetAll(){
		return userRepository.findAll()
				.stream()
				.map(entity->new UserResp(entity))
				.collect(Collectors.toList());
	}
	
	
	@ApiOperation(value = "新增用户" ,  notes="上传必要的用户信息来创建一个新的用户")
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PostMapping("/add")
	public UserResp add(@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @Valid @RequestBody UserReq userReq,HttpSession session) throws TrustSDKException {
		User user = new User(userReq);
		//用户自定义完整性进行校验
		UserCheck(user);
		PairKey key = TrustSDK.generatePairKey(true);
		user.setPublicKey(key.getPublicKey());
		user.setPrivateKey(key.getPrivateKey());
		
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());

		blockService.constructBlock(Operation.ADD, user, keys);
		return new UserResp(user);
	}
	
	@ApiOperation(value = "删除用户" ,  notes="根据用户的user_id来删除一个用户")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "user_id", value = "被操作的目标主键,直接放入地址中,替换{user_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@DeleteMapping("/{user_id}")
	public ResponseEntity<Void> delete(@PathVariable("user_id")String user_id,HttpSession session){
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		return userRepository.findById(user_id)
				.map(entity->{
					blockService.constructBlock(Operation.DELETE, entity, keys);
					return new ResponseEntity<Void>(HttpStatus.OK);})
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
		
	
	}
	
	@ApiOperation(value = "更新用户信息" ,  notes="通过user_id定位用户并更新其信息")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "user_id", value = "被操作的目标主键,直接放入地址中,替换{user_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@PutMapping("/{user_id}")
	public ResponseEntity<UserResp> update(@PathVariable("user_id")String user_id,
			@ApiParam(value="需要更新的课时信息,以json格式放入Request Body中",required=true) @RequestBody UserReq userReq,HttpSession session){
		PairKey keys = new PairKey(session.getAttribute("public_key").toString(),session.getAttribute("private_key").toString());
		User user = new User(userReq);
		return userRepository.findById(user_id)
				.map(entity->{
					//需要更新的信息（可被更新的信息）
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
					blockService.constructBlock(Operation.UPDATE, entity, keys);
					
					return entity;})
				.map(entity->new ResponseEntity<UserResp>(new UserResp(entity),HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@ApiOperation(value = "根据主键查找用户" ,  notes="根据用户user_id查找用户")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "path",name = "user_id", value = "被操作的目标主键,直接放入地址中,替换{user_id}", required = true) })
	@ApiResponses({@ApiResponse(code = 200, message = "操作成功",response = UserResp.class),
        @ApiResponse(code = 500, message = "服务器内部异常"),
        @ApiResponse(code = 400, message = "客户端请求的语法错误,服务器无法理解"),
        @ApiResponse(code = 405, message = "权限不足")})
	@GetMapping("/{user_id}")
	public  ResponseEntity<UserResp> findByID(@PathVariable("user_id")String user_id){
		return userRepository.findById(user_id)
				.map(entity->new ResponseEntity<UserResp>(new UserResp(entity),HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	private void UserCheck(@Valid User entity) {
		USER_IDENTITY identity = User.getUserTypeEnum(entity.getIdentity());
	}
	
}
