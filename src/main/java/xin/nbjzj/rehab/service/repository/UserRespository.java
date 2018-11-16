package xin.nbjzj.rehab.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xin.nbjzj.rehab.entity.User;

public interface UserRespository extends MongoRepository<User,String> {

}
