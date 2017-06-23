
package track.msgtest.messenger.store;

import track.msgtest.messenger.User;

public interface UserStore {
    /**
     * Добавить пользователя в хранилище
     * Вернуть его же
     */
    User addUser(String name, String pass);

    /**
     * Обновить информацию о пользователе
     */
    User updateUser(User user);

    /**
     *
     * Получить пользователя по логину/паролю
     * return null if user not found
     */
    User getUser(String name, String pass);

    /**
     *
     * Получить пользователя по id, например запрос информации/профиля
     * return null if user not found
     */
    User getUserById(Long id);
}