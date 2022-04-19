package com.example.managementApi.WorkingDay;

import com.example.managementApi.User.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    @JsonBackReference
    private int id;

    @Column(name = "full_date")
    private LocalDate fullDate;

    @Column(name = "n_hour")
    private int numberHours;

    @Column(name = "is_worked")
    private boolean isWorked;

    @Column(name = "is_over_timed")
    private boolean isOverTimed;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column
    private String unWorkedHoursReason;

    @Column
    private int unWorkedNHours;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;


    public Day(LocalDate fullDate, int numberHours, boolean isWorked, boolean isOverTimed, boolean isApproved,int unWorkedNHours,String unWorkedHoursReason, User user) {
        this.fullDate = fullDate;
        this.numberHours = numberHours;
        this.isWorked = isWorked;
        this.isOverTimed = isOverTimed;
        this.isApproved = isApproved;
        this.unWorkedNHours = unWorkedNHours;
        this.unWorkedHoursReason  = unWorkedHoursReason;
        this.user = user;
    }
}
