package xin.nbjzj.rehab.core.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import xin.nbjzj.rehab.core.entity.ClinicalInfo;

public interface ClinicalInfoReactive extends ReactiveMongoRepository<ClinicalInfo, String> {

}
