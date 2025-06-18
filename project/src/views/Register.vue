<!-- src/components/Register.vue -->
<template>
    <div class="register">
      <h2>注册</h2>
      <form @submit.prevent="onSubmit">
        <div>
          <label>用户名：</label>
          <input v-model="username" required />
        </div>
        <div>
          <label>邮箱：</label>
          <input type="email" v-model="email" required />
        </div>
        <div>
          <label>密码：</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="submit">注册</button>
      </form>
      <p>{{ message }}</p>
      
    </div>
  </template>
  
  <script>
  import { register } from '@/api/authService';
  
  export default {
    name: 'Register',
    data() {
      return {
        username: '',
        email: '',
        password: '',
        message: '',
      };
    },
    methods: {
      async onSubmit() {
        try {
          const response = await register({
            username: this.username,
            email: this.email,
            password: this.password,
          });
          this.message = response.data;
          // 注册成功后的逻辑，例如跳转到登录页面
          // this.$router.push('/login');
        } catch (error) {
          if (error.response) {
            this.message = error.response.data;
          } else {
            this.message = '请求失败，请稍后再试。';
          }
        }
      },
    },
  };
  </script>
  
  <style scoped>
  /* 添加样式 */
  </style>
  