package com.pms.entity;


import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "physician_availability")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Physician {

	@Id
	@GeneratedValue(generator = CustomIdGenerator.GENERATOR_NAME)
	@GenericGenerator(name = CustomIdGenerator.GENERATOR_NAME, strategy = "com.pms.entity.CustomIdGenerator", parameters = {
	@Parameter(name = CustomIdGenerator.PREFIX_PARAM, value = "PH00") })
	@Column(name = "physician_id")
	private String physicianId;
	@NotNull
	private String physicianName;
	@NotNull
	@Column(name = "physician_email", unique = true)
	private String physicianEmail;

	private String speciality;
	
	@Column(name="start_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	
	@Column( name="end_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private boolean availability;
	
	@Column(name = "contact_number")
	private String contactNumber;
	


}
