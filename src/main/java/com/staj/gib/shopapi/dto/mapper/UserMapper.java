package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.dto.request.CreateUserDto;
import com.staj.gib.shopapi.dto.request.UpdateUserDto;
import com.staj.gib.shopapi.dto.response.ResponseUserDto;
import com.staj.gib.shopapi.dto.response.UserResponse;
import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.security.UserSecurityDetails;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper {
    ResponseUserDto userToResponseUserDto(User user);


    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getPassword()))")
    User createUserDtoToUser(CreateUserDto dto, @Context PasswordEncoder passwordEncoder);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getPassword()))")
    User updateUserDtoToUser(UpdateUserDto dto,@MappingTarget User user, @Context PasswordEncoder passwordEncoder);

    UserSecurityDetails userToUserSecurityDetails(User user);

    @Mapping(target = "user", source = "user")
    UserResponse userToUserResponse(User user,String token);
}
