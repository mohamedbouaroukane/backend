package com.dac.dac.mapper;


import com.dac.dac.constants.UserRole;
import com.dac.dac.constants.UserRoleValue;
import com.dac.dac.entity.Client;
import com.dac.dac.entity.Courier;
import com.dac.dac.entity.User;
import com.dac.dac.payload.request.RegisterRequestDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(source="userRole",target = "userRole",qualifiedByName = "userrole")
    public User mapToUser(RegisterRequestDto registerRequestDto);
    @Named("userrole")
    default public UserRole mapToUserRole(String userRole) {
        if (userRole.equals(UserRoleValue.CLIENT)) {
            return UserRole.CLIENT;
        }else if(userRole.equals(UserRoleValue.COURIER)){
            return UserRole.COURIER;
        }else if(UserRole.ADMIN.equals(UserRoleValue.ADMIN) ){
            return UserRole.ADMIN;
        }
        throw new IllegalArgumentException("Invalid userRole: " + userRole);
    }

    @Mapping(source="userRole",target = "userRole",qualifiedByName = "userrole")
    public Client mapToClient(RegisterRequestDto registerRequestDto);

    @Mapping(source="userRole",target = "userRole",qualifiedByName = "userrole")
    public Courier mapToCourier(RegisterRequestDto registerRequestDto);


}
