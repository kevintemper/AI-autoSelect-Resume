package com.example.zwtcampuscareerview.services.interfaces;

public interface AccountService {

    void register(String username, String email, String password);
    boolean login(String username, String password);
    void changePassword(String username, String oldPassword, String newPassword);
}
