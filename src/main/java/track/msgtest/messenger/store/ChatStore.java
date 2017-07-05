package track.msgtest.messenger.store;

import track.msgtest.messenger.messages.ChatCreateMessage;

import java.util.ArrayList;

/**
 * Created by ivan on 23.06.17.
 */
public interface ChatStore {
    /**
     * Создать чат
     * Вернуть True или False
     */
    long createChat(ChatCreateMessage chatCreateMessage);

    /**
     * Присоединять к чату с id = chatId пользователя с id = userId
     */
    long joinChat(long userId, String chatName);

    /**
     * Получить chatId по названю чата
     */
    long getChatId(String name);

    /**
     * Получить список чатов, в которых состоит пользователь с userId
     */
    String getChats(long userId);

    /**
     * Получить имя чата по chatId
     */
    String getChatNameById(long chatId);

}
