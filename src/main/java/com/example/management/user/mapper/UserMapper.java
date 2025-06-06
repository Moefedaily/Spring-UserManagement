package com.example.management.user.mapper;

import com.example.management.user.dto.UserDto;
import com.example.management.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getDateNaissance()
        );
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getDateNaissance()
        );
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<User> toEntityList(List<UserDto> dtos) {
        return dtos.stream()
                .map(UserMapper::toEntity)
                .collect(Collectors.toList());
    }
}