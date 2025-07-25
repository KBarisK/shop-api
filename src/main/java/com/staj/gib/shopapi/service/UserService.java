package com.staj.gib.shopapi.service;

import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.entity.dto.CreateUserDto;
import com.staj.gib.shopapi.entity.dto.RequestUserDto;
import com.staj.gib.shopapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.staj.gib.shopapi.enums.UserType;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<RequestUserDto>getUser(UUID userID){
        return userRepository.findById(userID)
                .map(user -> new RequestUserDto(
                        user.getId(),
                        user.getCreatedAt(),
                        user.getUpdatedAt(),
                        user.getVersion(),
                        user.getUsername(),
                        user.getUserType()
                ));
    }

    public Optional<RequestUserDto> saveUser(CreateUserDto requestUserDto){
        User user = new User();
        user.setUsername(requestUserDto.getUsername());
        user.setPassword(requestUserDto.getPassword()); //NEEDS HASHING TODO
        user.setUserType(UserType.CUSTOMER);
        User savedUser = this.userRepository.save(user);
        return Optional.of(new RequestUserDto(
                savedUser.getId(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt(),
                savedUser.getVersion(),
                savedUser.getUsername(),
                savedUser.getUserType()
        ));
    }
}
