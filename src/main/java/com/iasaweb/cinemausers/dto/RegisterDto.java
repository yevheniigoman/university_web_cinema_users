package com.iasaweb.cinemausers.dto;

import com.iasaweb.cinemausers.entity.Role;

public record RegisterDto(
    String username,
    String password,
    Role role
) {}
