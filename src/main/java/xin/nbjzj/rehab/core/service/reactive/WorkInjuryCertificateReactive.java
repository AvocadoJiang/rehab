package xin.nbjzj.rehab.core.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import xin.nbjzj.rehab.core.entity.WorkInjuryCertificate;

public interface WorkInjuryCertificateReactive extends ReactiveMongoRepository<WorkInjuryCertificate,String> {

}
