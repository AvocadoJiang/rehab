package xin.nbjzj.rehab.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import xin.nbjzj.rehab.entity.User;

@Repository
public interface UserReactive extends ReactiveMongoRepository<User,String> {

}
