package com.pms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entity.Physician;
import com.pms.exception.PhysicianServiceException;
import com.pms.service.PhysicianService;



@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins="*")
public class PhysicianController {

	@Autowired
	private PhysicianService physicianService;

	@GetMapping("/physician-availability")
	public ResponseEntity<List<Physician>> showAvailability(
			@RequestParam(required = false, value = "availability") boolean availability)
			throws PhysicianServiceException {
		if (Objects.nonNull(availability) && availability) {

			/// patient
			List<Physician> list = physicianService.showAvailability(availability);

			if (list == null) {
				throw new PhysicianServiceException("Available physician don't exist");
			}
			return ResponseEntity.ok().body(list);
		} else {
			/// admin
			List<Physician> list = physicianService.showAllAvailability();
			if (list.size() <= 0) {
				throw new PhysicianServiceException("Physicians information don't exist");
			}
			return ResponseEntity.ok().body(list);
		}

	}
	/*
	 * @PostMapping("/physician-availability") public ResponseEntity<Physician>
	 * addPhysician(@RequestBody Physician physician) { Physician phy = null;
	 * 
	 * phy = this.physicianService.addPhysician(physician);
	 * System.out.println(physician); return
	 * ResponseEntity.status(HttpStatus.CREATED).body(phy);
	 * 
	 * }
	 */

	@DeleteMapping("/physician-availability/{physicianId}")
	public ResponseEntity<Void> deletePhysician(@PathVariable("physicianId") String physicianId) throws PhysicianServiceException {
		boolean bool = false;

		bool = this.physicianService.deletePhysician(physicianId);
		if (bool == true) {
			System.out.println("Deleted succesfully");
			return ResponseEntity.status(HttpStatusCode.valueOf(202)).build();
		} else {
			throw new PhysicianServiceException("List is empty");
		}

	}

	@PutMapping("/physician-availability/{physicianId}")
	public ResponseEntity<Physician> UpdatePhysicianInfo(@RequestBody Physician physician,
			@PathVariable("physicianId") String physicianId) throws PhysicianServiceException {
		int result = 0;

		result = this.physicianService.updatePhysicianInfo(physician, physicianId);

		if (result != 0) {
			System.out.println("Update successfully");
			return ResponseEntity.accepted().body(physician);
		} else {
			throw new PhysicianServiceException("Physician Id not valid  "+physicianId);
		}

	}
	
	@GetMapping("/physician-availability/{date}")
	public ResponseEntity<List<Physician>> getByDate(@PathVariable String date){
		LocalDate localDate = LocalDate.parse(date);
		System.out.println("Before service");
		List<Physician> list= physicianService.findAllPhysicianOnDate(localDate);
		return new ResponseEntity<>(list,HttpStatus.OK);
		
	}

}
