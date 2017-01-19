package com.s11web.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by xietian on 2017/1/9.
 */
@Entity
@Table(name = "S11_task_ArcAverageScanTime")
public class ArcAverageScanTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private Date date;

    private String type;

    private String Taskid;

    private int scanNumber;

    private double avgScanTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskid() {
        return Taskid;
    }

    public void setTaskid(String taskid) {
        Taskid = taskid;
    }

    public int getScanNumber() {
        return scanNumber;
    }

    public void setScanNumber(int scanNumber) {
        this.scanNumber = scanNumber;
    }

    public double getAvgScanTime() {
        return avgScanTime;
    }

    public void setAvgScanTime(double avgScanTime) {
        this.avgScanTime = avgScanTime;
    }
}
