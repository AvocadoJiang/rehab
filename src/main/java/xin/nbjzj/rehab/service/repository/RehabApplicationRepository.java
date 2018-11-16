package xin.nbjzj.rehab.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xin.nbjzj.rehab.entity.RehabApplication;

public interface RehabApplicationRepository extends MongoRepository<RehabApplication,String> {

}
