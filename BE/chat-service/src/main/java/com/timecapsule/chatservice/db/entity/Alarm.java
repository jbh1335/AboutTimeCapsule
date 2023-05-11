package com.timecapsule.chatservice.db.entity;


import com.timecapsule.chatservice.dto.CategoryType;
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
