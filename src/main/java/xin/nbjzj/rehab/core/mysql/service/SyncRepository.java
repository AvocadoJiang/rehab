package xin.nbjzj.rehab.core.mysql.service;

import org.springframework.data.jpa.repository.JpaRepository;

import xin.nbjzj.rehab.core.mysql.entity.Sync;

public interface SyncRepository extends JpaRepository<Sync, Long> {
	Sync findTopByOrderBySyncIDDesc();
}
