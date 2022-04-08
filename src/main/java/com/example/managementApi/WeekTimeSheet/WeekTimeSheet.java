package com.example.managementApi.WeekTimeSheet;

import com.example.managementApi.User.User;
import com.example.managementApi.WorkingDay.Day;
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
    private Day beginningOfWeek;

    @ManyToOne
    @JoinColumn(name = "end_of_week",nullable = false)
    private Day endOfWeek;

    private int totalNhours;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

}
