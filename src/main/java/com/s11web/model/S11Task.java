package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="S11_task")
public class S11Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	@Getter @Setter
	private int id;

	@Getter @Setter
	private int userId;

	@Getter @Setter
	private String cargoType;

	@Getter @Setter
	private String source;

	@Getter @Setter
	private String destination;

	@Getter @Setter
	private String sortCode;

	@Getter @Setter
	private String taskId;

	@Getter @Setter
	private Date creDate;

	@Getter @Setter
	private String laneE;

	@Getter @Setter
	private String laneName;

	@Getter @Setter
	private String carrierAbbr;

	@Getter @Setter
	private String carType;

	@Getter @Setter
	private String carNumber;

	@Getter @Setter
	private double waterVol;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public String getLaneE() {
		return laneE;
	}

	public void setLaneE(String laneE) {
		this.laneE = laneE;
	}

	public String getLaneName() {
		return laneName;
	}

	public void setLaneName(String laneName) {
		this.laneName = laneName;
	}

	public String getCarrierAbbr() {
		return carrierAbbr;
	}

	public void setCarrierAbbr(String carrierAbbr) {
		this.carrierAbbr = carrierAbbr;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public double getWaterVol() {
		return waterVol;
	}

	public void setWaterVol(double waterVol) {
		this.waterVol = waterVol;
	}
}
