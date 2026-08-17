package com.nexteam.websocket.messaging.conversation;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Class 'ConversationPresenceService' en charge de
 *
 * @author JonatanNs
 * @version 1.0
 * @since 17/08/2026 22:34
 */
@Service
public class ConversationPresenceService {

    // email utilisateur -> ensemble des conversations (publicId en String) actuellement ouvertes
    private final Map<String, Set<String>> presence = new ConcurrentHashMap<>();

    public void markOpen(String userEmail, String conversationPublicId) {
        presence.computeIfAbsent(userEmail, k -> new CopyOnWriteArraySet<>()).add(conversationPublicId);
    }

    public void markClosed(String userEmail, String conversationPublicId) {
        Set<String> convs = presence.get(userEmail);
        if (convs != null) {
            convs.remove(conversationPublicId);
            if (convs.isEmpty()) {
                presence.remove(userEmail);
            }
        }
    }

    public void clearAllForUser(String userEmail) {
        presence.remove(userEmail);
    }

    public boolean isOpen(String userEmail, String conversationPublicId) {
        Set<String> convs = presence.get(userEmail);
        return convs != null && convs.contains(conversationPublicId);
    }
}
