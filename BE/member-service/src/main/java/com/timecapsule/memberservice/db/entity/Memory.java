package com.timecapsule.memberservice.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Memory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id")
    private Capsule capsule;
    @Column(length = 128)
    private String title;
    private String content;
    private String image;
    private boolean isDeleted;
    private boolean isLocked;
    @OneToMany(mappedBy = "memory")
    private List<Comment> commentList = new ArrayList<>();
}
