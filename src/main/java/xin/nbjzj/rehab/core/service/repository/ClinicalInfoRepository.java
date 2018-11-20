package xin.nbjzj.rehab.core.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xin.nbjzj.rehab.core.entity.ClinicalInfo;

public interface ClinicalInfoRepository extends MongoRepository<ClinicalInfo,String> {

}
