package com.pms.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.entity.Physician;

import jakarta.transaction.Transactional;

public interface PhysicianRepository extends JpaRepository<Physician, String> {
	
	@Query(value="SELECT * FROM physician_availability pv WHERE availability=1",  nativeQuery = true)
	public List<Physician> findByValue(boolean availability);
	
	
	public boolean existsById(String physicianId);
	public void deleteById(String physicianId);
	

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "UPDATE physician_availability SET availability =:availability, start_date=:startDate, end_date=:endDate WHERE physician_id =:physicianId", nativeQuery = true)
	public int updateByPhysicianId(@Param("physicianId") String physicianId, @Param("availability") boolean availability, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


	public Physician getByPhysicianId(String physicianId);

}
