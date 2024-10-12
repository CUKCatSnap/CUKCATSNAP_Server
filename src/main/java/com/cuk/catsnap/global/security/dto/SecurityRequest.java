package com.cuk.catsnap.global.security.dto;

import lombok.Getter;

public class SecurityRequest {

    @Getter
    public static class MemberSingInRequest {
        private String identifier;
        private String password;
    }
}
