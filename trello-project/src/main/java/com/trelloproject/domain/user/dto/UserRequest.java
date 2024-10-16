package com.trelloproject.domain.user.dto;

import com.trelloproject.domain.user.dto.UserRequest.Delete;

public sealed  interface UserRequest permits Delete {
    record Delete(String password) implements UserRequest {}

}
