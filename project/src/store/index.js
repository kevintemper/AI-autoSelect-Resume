// store/index.js

import { createStore } from 'vuex';
import apiClient from '../axios'; // 根据实际路径调整

export default createStore({
  state: {
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user')) || {},
  },
  mutations: {
    setAuth(state, payload) {
      state.token = payload.token;
      state.user = payload.user;
      localStorage.setItem('token', payload.token);
      localStorage.setItem('user', JSON.stringify(payload.user));
    },
    clearAuth(state) {
      state.token = '';
      state.user = {};
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },
  },
  actions: {
    async login({ commit }, credentials) {
      console.log('Vuex login action called with:', credentials);
      try {
        const response = await apiClient.post('/api/login', credentials);
        commit('setAuth', { token: response.data.token, user: response.data.user });
        return response.data;
      } catch (error) {
        console.error('Login error:', error);
        throw error.response.data;
      }
    },
    async register({ commit }, userInfo) {
      console.log('Vuex register action called with:', userInfo);
      try {
        const response = await apiClient.post('/api/register', userInfo);
        // 注册成功后可选择自动登录或提示用户去登录
        return response.data;
      } catch (error) {
        console.error('Register error:', error);
        throw error.response.data;
      }
    },
    async changePassword({ state }, payload) {
      console.log('Vuex changePassword action called with:', payload);
      try {
        const response = await apiClient.post('/api/change-password', payload, {
          headers: {
            Authorization: `Bearer ${state.token}`,
          },
        });
        console.log('Change password response:', response);
        return response.data;
      } catch (error) {
        console.error('Change password error:', error);
        throw error.response.data;
      }
    },
    async recoverPassword({ commit }, payload) {
      console.log('Vuex recoverPassword action called with:', payload);
      try {
        const response = await apiClient.post('/api/reset-password', payload);
        console.log('Recover password response:', response);
        return response.data;
      } catch (error) {
        console.error('Recover password error:', error);
        throw error.response.data;
      }
    },
    logout({ commit }) {
      commit('clearAuth');
    },
  },
  getters: {
    isAuthenticated: (state) => !!state.token,
    getUser: (state) => state.user,
  },
});
