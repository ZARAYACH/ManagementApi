package com.example.managementApi.WorkingDay;

import com.example.managementApi.User.User;
import com.example.managementApi.WeekTimeSheet.WeekTimeSheet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnore
    @OneToOne(mappedBy = "beginningOfWeek")
    private WeekTimeSheet beginningOfWeek;
    @JsonIgnore
    @OneToOne(mappedBy = "endOfWeek")
    private WeekTimeSheet endOfWeek;

}
