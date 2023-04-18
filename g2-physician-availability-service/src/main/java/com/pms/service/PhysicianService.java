package com.pms.service;

import java.time.LocalDate;
import java.util.List;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.pms.entity.Physician;
import com.pms.exception.PhysicianServiceException;

public interface PhysicianService {
	
	public List<Physician> showAllAvailability()throws PhysicianServiceException;
	public List<Physician> showAvailability(boolean availability)throws PhysicianServiceException;
	public boolean deletePhysician(String physicianId)throws PhysicianServiceException;
	public int updatePhysicianInfo(Physician physician, String physicianId)throws PhysicianServiceException;
	public List<Physician> findAllPhysicianOnDate(LocalDate date);
	public void addPhysicianInfo() throws UnirestException;

}
