package xin.nbjzj.rehab.core.service;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xin.nbjzj.rehab.core.entity.User;

public interface UserRepository extends JpaRepository<User,String> {
	User findByPhone(String phone);
	List<User> findByIdentity(String identity);
}
