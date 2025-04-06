package com.example.demo;

import com.example.demo.config.MyConfig;
import com.example.demo.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.annotation.Annotation;
import java.util.List;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        Communication comm = context.getBean("communication", Communication.class);
        List<User> allUsers = comm.showAllUsers();
        System.out.println(allUsers);

        // 2. Создаем пользователя с id=3, name=James, lastName=Brown, age=30
        User newUser = new User(3L, (byte)30, "Brown", "James");
        String saveResponse = comm.saveUser(newUser);
        System.out.println("Ответ при сохранении пользователя: " + saveResponse);

        // 3. Обновляем пользователя: меняем name на Thomas, lastName на Shelby (age оставляем 30)
        User updatedUser = new User(3L, (byte)30, "Shelby", "Thomas");
        String updateResponse = comm.updateUser(updatedUser);
        System.out.println("Ответ при обновлении пользователя: " + updateResponse);

        // 4. Удаляем пользователя с id=3
        String deleteResponse = comm.deleteUser(3);
        System.out.println("Ответ при удалении пользователя: " + deleteResponse);

        context.close();
    }
}
