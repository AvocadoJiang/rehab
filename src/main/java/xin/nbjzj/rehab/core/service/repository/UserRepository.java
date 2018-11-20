package xin.nbjzj.rehab.core.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xin.nbjzj.rehab.core.entity.User;

public interface UserRepository extends MongoRepository<User,String> {
	User findByPhone(String phone);
}
