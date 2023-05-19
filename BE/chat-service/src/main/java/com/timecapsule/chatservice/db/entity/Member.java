package com.timecapsule.chatservice.db.entity;

import com.timecapsule.chatservice.dto.ProviderType;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
public class Member implements Serializable {
    @Id
    private Integer id;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    private String email;
    private String profileImageUrl;
    private String createdDate;
}
