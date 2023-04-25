package com.example.oauthservice.db.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member memberId;
    @Column(length = 255)
    private String content;
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
    private int capsuleId;
}
