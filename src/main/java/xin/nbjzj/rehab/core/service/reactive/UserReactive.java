package xin.nbjzj.rehab.core.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import xin.nbjzj.rehab.core.entity.User;

@Repository
public interface UserReactive extends ReactiveMongoRepository<User,String> {
	Flux<User> findByIdentity(String identity);
}
