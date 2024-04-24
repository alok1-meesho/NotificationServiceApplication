package com.example.notificationservice.entity.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Data
@Table(name="sms_requests")
public class SmsRequests {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="phone_number")
    private String number;

    @Column(name="message")
    private String message;

    @Column(name="status")
    private String status;

    @Column(name="failure_code")
    private int  failureCode;

    @Column(name="failure_comment")
    private String failureComment;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "SmsRequests{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", failureCode=" + failureCode +
                ", failureComment='" + failureComment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
