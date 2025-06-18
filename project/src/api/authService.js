// src/api/authService.js
import axiosInstance from './axiosInstance';


export const register = data => {
  return axiosInstance.post('/Account/Register', data);
};

export const login = data => {
  return axiosInstance.post('/Account/Login', data);
};

export const changePassword = data => {
  return axiosInstance.post('/Account/ChangePassword', data);
};

// 如果有重置密码功能
export const resetPassword = data => {
  return axiosInstance.post('/passwordreset', data);
};
