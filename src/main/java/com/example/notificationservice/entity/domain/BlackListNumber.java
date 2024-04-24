package com.example.notificationservice.entity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// It will store in mysql database in blacklist_number table
@Entity
@Table(name="blacklist_number")
@AllArgsConstructor
@NoArgsConstructor

@Data
public class BlackListNumber {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="number")
    private String number;
    @Column(name="status")
    private int status;

}
