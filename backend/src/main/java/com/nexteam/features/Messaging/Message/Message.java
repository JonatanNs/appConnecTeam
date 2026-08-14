package com.nexteam.features.Messaging.Message;

import com.nexteam.common.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
public class Message extends AuditableEntity {
    @Lob
    @Column(nullable = false)
    @NotBlank
    private List<String> content;
}
