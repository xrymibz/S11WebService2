package com.s11web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xietian on 2017/1/3.
 */

@Entity
@Table(name = "tmp_user")
public class Test_user {
    @Id
    @Column(name = "id")
    private int id;
    public String name;
    public int  age;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
