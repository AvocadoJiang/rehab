package xin.nbjzj.rehab.service.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import xin.nbjzj.rehab.entity.ClinicalInfo;

public interface ClinicalInfoReactive extends ReactiveMongoRepository<ClinicalInfo, String> {

}
