package xin.nbjzj.rehab.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import xin.nbjzj.rehab.entity.RehabApplication;

public interface RehabApplicationReactive extends ReactiveMongoRepository<RehabApplication,String> {

}
