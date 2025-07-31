package com.staj.gib.shopapi.dto.mapper;

import com.staj.gib.shopapi.entity.User;
import com.staj.gib.shopapi.dto.request.CreateUserDto;
import com.staj.gib.shopapi.dto.response.ResponseUserDto;
import com.staj.gib.shopapi.dto.request.UpdateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    ResponseUserDto userToResponseUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "userType", ignore = true)
    User createUserDtoToUser(CreateUserDto user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "userType", ignore = true)
    User updateUserDtoToUser(UpdateUserDto updateUserDto,@MappingTarget User user);
}
