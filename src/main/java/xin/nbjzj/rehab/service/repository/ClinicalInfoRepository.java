package xin.nbjzj.rehab.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xin.nbjzj.rehab.entity.ClinicalInfo;

public interface ClinicalInfoRepository extends MongoRepository<ClinicalInfo,String> {

}
