package com.ecom.user_service_api.service;

import com.ecom.user_service_api.dto.RequestUserDto;
import com.ecom.user_service_api.dto.RequestUserLoginDto;

public interface SystemUserService {
    public void signup(RequestUserDto dto);
    public Object login(RequestUserLoginDto dto);
}
