package com.pms.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pms.entity.Physician;
import com.pms.exception.PhysicianServiceException;
import com.pms.repository.PhysicianRepository;
import com.pms.service.PhysicianService;

@Service
public class PhysicianServiceImpl implements PhysicianService {

	@Autowired
	private PhysicianRepository physicianRepository;

	@Override
	public List<Physician> showAllAvailability() throws PhysicianServiceException {
		List<Physician> list = this.physicianRepository.findAll();
		if(list == null) {
			throw new PhysicianServiceException("Physican information don't exist");
		}
		return list;
	}

	@Override
	public List<Physician> showAvailability(boolean availability) throws PhysicianServiceException {
		List<Physician> list = null;
		list = (List<Physician>) this.physicianRepository.findByValue(availability);
		if(list == null) {
			throw new PhysicianServiceException("Physician's are not available");
		}
		return list;

	}

	@Override
	public boolean deletePhysician(String physicianId) throws PhysicianServiceException {
		boolean bool = physicianRepository.existsById(physicianId);
		physicianRepository.deleteById(physicianId);
		if (bool) {
			System.out.println("item deleted");
			return true;
		} else {
			throw new PhysicianServiceException("Physician Id is not valid  "+physicianId);

		}

	}

	@Override
	public int updatePhysicianInfo(Physician physician, String physicianId) throws PhysicianServiceException {
		
		boolean bool = physicianRepository.existsById(physicianId);
		if(!bool) {
			throw new PhysicianServiceException("Physician Id is not valid  "+physicianId);
		}

		int result = physicianRepository.updateByPhysicianId(physicianId, physician.isAvailability(), physician.getStartDate(), physician.getEndDate());
		return result;

	}
	
	@Override
	public List<Physician> findAllPhysicianOnDate(LocalDate date) {
		List<Physician> allPhy=physicianRepository.findAll();
		List<Physician> physicianOnThatDate=new ArrayList<>();
		for(Physician physician:allPhy) {
			
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			LocalDate currentDate=date; 
			LocalDate startdate = physician.getStartDate();  
			LocalDate enddate = physician.getEndDate(); 
			int startDate=currentDate.compareTo(startdate);
			int endDate=currentDate.compareTo(enddate);
		
			if(startDate>=0 && endDate<=0) {
				System.out.println("Inside service");
				physicianOnThatDate.add(physician);
			}
			
		}
		
		return physicianOnThatDate;
	}
	
	
	
	// Get Physician details from Auth0
		@Override
		public void addPhysicianInfo() throws UnirestException {
			
			HttpResponse<String> response = Unirest.post("https://dev-7vcuci4tdeykfzss.us.auth0.com/oauth/token")
					.header("content-type", "application/json")
					.body("{\"client_id\":\"D4SKE5uz2tw92xACR5ZrKA2DUQpeNRA8\",\"client_secret\":\"TxUXhIK-f4EDrAzO1lLgOsSCt0866twNXSkZ4MsqjRhL2I6jNipg2AtnefWF9dhS\",\"audience\":\"https://dev-7vcuci4tdeykfzss.us.auth0.com/api/v2/\",\"grant_type\":\"client_credentials\"}")
					.asString();

			System.out.println(response);
			JsonNode jsonNode = null;
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				jsonNode = objectMapper.readTree(response.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			String accessToken = jsonNode.get("access_token").textValue();
			String tokenType = jsonNode.get("token_type").textValue();
			String token = tokenType + " " + accessToken;
			System.out.println(token);
			
			
			List<Physician> physicianList = new ArrayList<>();

			HttpResponse<String> res = Unirest.get("https://dev-7vcuci4tdeykfzss.us.auth0.com/api/v2/users")
					.header("authorization", token).asString();		

			try {
				jsonNode = objectMapper.readTree(res.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			
			
			for (int i = 0; i < jsonNode.size(); i++) {

				Physician physician = new Physician();
				
				if (jsonNode.get(i).get("user_metadata") != null) {

					
					/*
					 * System.out.print(jsonNode.get(i).get("name")); System.out.print("\t");
					 * System.out.print(jsonNode.get(i).get("email")); System.out.print("\t");
					 * System.out.print(jsonNode.get(i).get("user_metadata").get("role"));
					 * System.out.print("\t");
					 * 
					 * System.out.print(jsonNode.get(i).get("user_metadata").get("contact")+"\t");
					 * System.out.print(jsonNode.get(i).get("user_metadata").get("speciality"));
					 * 
					 * System.out.println(i);
					 */
					 
					String role = jsonNode.get(i).get("user_metadata").get("role").textValue();		
					
					if (role.equalsIgnoreCase("doctor")) {
						physician.setPhysicianName(jsonNode.get(i).get("name").textValue());
						physician.setPhysicianEmail(jsonNode.get(i).get("email").textValue());
						physician.setContactNumber(jsonNode.get(i).get("user_metadata").get("contact").textValue());
						physician.setSpeciality(jsonNode.get(i).get("user_metadata").get("speciality").textValue());
						physicianList.add(physician);
					}
				}
			}	
			//store all physician information in database
			physicianRepository.saveAll(physicianList);	
			
		}	

	
	

}
