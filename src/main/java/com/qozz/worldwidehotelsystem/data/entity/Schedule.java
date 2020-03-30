package com.qozz.worldwidehotelsystem.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "usr_id")
    private User user;

    @Column(nullable = false)
    private LocalDate registerStart;

    @Column(nullable = false)
    private LocalDate registerEnd;
}
