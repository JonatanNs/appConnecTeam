package com.nexteam.features.Messaging.Message.enums;

/**
 * Class 'MessageType' en charge de
 * CHAT → (l'historique de conversation).
 * SYSTEM ("untel a rejoint/quitté la conversation")
 *
 * @author JonatanNs
 * @version 1.0
 * @since 16/08/2026 16:09
 */
public enum MessageType {
    CHAT,   // message normal, persisté, déclenche notif
    SYSTEM
}

