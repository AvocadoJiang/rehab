package xin.nbjzj.rehab.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import xin.nbjzj.rehab.entity.WorkInjuryCertificate;

public interface WorkInjuryCertificateReactive extends ReactiveMongoRepository<WorkInjuryCertificate,String> {

}
