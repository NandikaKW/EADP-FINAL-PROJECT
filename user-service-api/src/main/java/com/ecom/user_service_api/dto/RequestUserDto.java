package com.ecom.user_service_api.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestUserDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
