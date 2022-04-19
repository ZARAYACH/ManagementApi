package com.example.managementApi.WeekTimeSheet;

import com.example.managementApi.User.User;
import com.example.managementApi.WorkingDay.Day;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "week_time_sheet")
public class WeekTimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int weekId;

    @ManyToOne
    @JoinColumn(name = "beginning_of_week",nullable = false)
    @JsonManagedReference
    private Day beginningOfWeek;

    @ManyToOne
    @JoinColumn(name = "end_of_week",nullable = false)
    @JsonManagedReference
    private Day endOfWeek;

    private int totalNhours;
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

}
