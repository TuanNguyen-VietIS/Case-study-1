package com.tun.casestudy1.service;

public interface AuthService {
    String authenticate(String username, String password);

    String getRoleByUsername(String username);
}
