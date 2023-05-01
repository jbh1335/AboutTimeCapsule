package com.timecapsule.capsuleservice.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Memory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id")
    private Capsule capsule;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(length = 128)
    private String title;
    private String content;
    @Lob
    private String image;
    private boolean isDeleted;
    private boolean isLocked;
    private LocalDate openDate;
    @OneToMany(mappedBy = "memory")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "memory")
    private List<MemoryOpenMember> memoryOpenMemberList = new ArrayList<>();

    @Builder
    public Memory(Capsule capsule, Member member, String title, String content, String image, boolean isDeleted, boolean isLocked, LocalDate openDate) {
        this.capsule = capsule;
        this.member = member;
        this.title = title;
        this.content = content;
        this.image = image;
        this.isDeleted = isDeleted;
        this.isLocked = isLocked;
        this.openDate = openDate;
    }

    public static Memory of(Memory memory, boolean isDeleted) {
        memory.setDeleted(isDeleted);
        return memory;
    }
}
