package com.qozz.worldwidehotelsystem.data.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
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
    private LocalDate rentStart;

    @Column(nullable = false)
    private LocalDate rentEnd;
}
