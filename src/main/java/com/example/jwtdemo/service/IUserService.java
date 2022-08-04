package com.example.jwtdemo.service;

import com.example.jwtdemo.dto.request.RegistryRequest;
import com.example.jwtdemo.dto.response.RegistryResponse;
import com.example.jwtdemo.dto.response.ResponseObject;

public interface IUserService {
    ResponseObject<RegistryResponse> register(RegistryRequest req);
}
