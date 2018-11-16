package xin.nbjzj.rehab.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xin.nbjzj.rehab.entity.User;

public interface UserRepository extends MongoRepository<User,String> {
	User findByPhone(String phone);
}
