package com.qozz.worldwidehotelsystem.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Accessors(chain = true)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(nullable = false, name = "usr_id")
    private User user;

    @Column(nullable = false)
    private LocalDate registerStart;

    @Column(nullable = false)
    private LocalDate registerEnd;
}
