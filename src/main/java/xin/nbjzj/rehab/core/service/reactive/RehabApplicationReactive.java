package xin.nbjzj.rehab.core.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import xin.nbjzj.rehab.core.entity.RehabApplication;

public interface RehabApplicationReactive extends ReactiveMongoRepository<RehabApplication,String> {

}
