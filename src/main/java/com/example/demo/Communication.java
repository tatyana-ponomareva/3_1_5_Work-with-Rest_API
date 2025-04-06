package com.example.demo;

import com.example.demo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class Communication {
    private RestTemplate restTemplate;
    private final String URL = "http://94.198.50.185:7081/api/users";
    private String sessionCookie;

    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> showAllUsers() {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {});

        HttpHeaders headers = responseEntity.getHeaders();
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID=")) {
                    // Получаем только значение cookie до первого ";"
                    sessionCookie = cookie.split(";")[0];
                    break;
                }
            }
        }

        List<User> allUsers = responseEntity.getBody();
        return allUsers;
    }
    // 2. Создание пользователя (POST)
    public String saveUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Добавляем сохранённую session cookie
        headers.set("Cookie", sessionCookie);
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                URL, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }

    // 3. Обновление пользователя (PUT)
    public String updateUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", sessionCookie);
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                URL, HttpMethod.PUT, requestEntity, String.class);
        return response.getBody();
    }

    // 4. Удаление пользователя (DELETE)
    public String deleteUser(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", sessionCookie);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                URL + "/" + id, HttpMethod.DELETE, requestEntity, String.class);
        return response.getBody();
    }
}
