package com.example.managementApi.WorkingDay;

import com.example.managementApi.User.User;
import com.example.managementApi.WeekTimeSheet.WeekTimeSheet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table( name = "day")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_date")
    private LocalDate fullDate;

    @Column(name = "begin_time")
    private LocalTime beginTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "n_hour")
    private int numberHours;

    @Column(name = "is_worked")
    private boolean isWorked;

    @Column(name = "is_over_timed")
    private boolean isOverTimed;

    @Column(name = "is_approved")
    private boolean isApproved;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;


    public Day(LocalDate fullDate, LocalTime beginTime, LocalTime endTime, int numberHours, boolean isWorked, boolean isOverTimed, boolean isApproved, User user) {
        this.fullDate = fullDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.numberHours = numberHours;
        this.isWorked = isWorked;
        this.isOverTimed = isOverTimed;
        this.isApproved = isApproved;
        this.user = user;
    }
}
