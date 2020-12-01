package com.sunset.spring.jpa.entity;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "id")
    private Team team;
}
