package com.qozz.worldwidehotelsystem.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int floor;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Schedule> schedules;
}
