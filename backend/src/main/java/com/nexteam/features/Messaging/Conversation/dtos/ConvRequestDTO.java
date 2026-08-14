package com.nexteam.features.Messaging.Conversation.dtos;

import com.nexteam.features.Messaging.Message.Message;
import com.nexteam.features.Users.User.User;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvRequestDTO {
    private Set<User> user;
    private String name;
    private Instant createdAt;
    private List<Message> messages;
}
