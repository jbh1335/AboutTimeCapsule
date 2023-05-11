package com.timecapsule.capsuleservice.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String content;
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
    private int memoryId;

    @Builder
    public Alarm(Member member, String content, CategoryType categoryType, int memoryId) {
        this.member = member;
        this.content = content;
        this.categoryType = categoryType;
        this.memoryId = memoryId;
    }
}
