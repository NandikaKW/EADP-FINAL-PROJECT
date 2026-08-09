package com.ecom.user_service_api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestUserLoginDto {
    private String email;
    private String password;
}
