package org.example.library.mapper;

import org.example.library.dto.request.UserRequest;
import org.example.library.dto.response.UserResponse;
import org.example.library.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        User u = new User();
        u.setName(request.name());
        u.setEmail(request.email());
        return u;
    }

    public void updateEntity(UserRequest request, User target) {
        target.setName(request.name());
        target.setEmail(request.email());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}