package com.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Test", schema = "RecycleIT")
public class Test {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
}
