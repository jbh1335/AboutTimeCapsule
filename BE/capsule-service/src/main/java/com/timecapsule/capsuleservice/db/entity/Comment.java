package com.timecapsule.capsuleservice.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id")
    private Memory memory;
    private String content;
    private boolean isDeleted;

    @Builder
    public Comment(Member member, Memory memory, String content, boolean isDeleted) {
        this.member = member;
        this.memory = memory;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public static Comment of(Comment comment, boolean isDeleted) {
        comment.setDeleted(isDeleted);
        return comment;
    }
}
