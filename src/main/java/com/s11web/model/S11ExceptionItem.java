package com.s11web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="S11_exception_item")
public class S11ExceptionItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String scanId;

    @Getter @Setter
    private Date creDate;

    @Getter @Setter
    private String exceptionType;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String taskId;

    @Getter @Setter
    private String pictureUrl;

}
