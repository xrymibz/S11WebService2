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
@Table(name="S11_task_item")
public class S11TaskItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	@Getter @Setter
	private int id;

	@Getter @Setter
	private String taskId;

	@Getter @Setter
	private String scanId;

	@Getter @Setter
	private Date scanDatetime;

	@Getter @Setter
	private String box;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getScanId() {
		return scanId;
	}

	public void setScanId(String scanId) {
		this.scanId = scanId;
	}

	public Date getScanDatetime() {
		return scanDatetime;
	}

	public void setScanDatetime(Date scanDatetime) {
		this.scanDatetime = scanDatetime;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}
}
