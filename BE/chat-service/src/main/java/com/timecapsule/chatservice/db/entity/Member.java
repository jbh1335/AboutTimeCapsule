package com.timecapsule.chatservice.db.entity;

import com.timecapsule.chatservice.dto.ProviderType;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
public class Member {
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    private String email;
    private String profileImageUrl;
    private String createdDate;
}
