<!-- src/components/Login.vue -->
<template>
    <div class="login">
      <h2>登录</h2>
      <form @submit.prevent="onSubmit">
        <div>
          <label>用户名：</label>
          <input v-model="username" required />
        </div>
        <div>
          <label>密码：</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="submit">登录</button>
      </form>
      <p>{{ message }}</p>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Login',
    data() {
      return {
        username: '',
        password: '',
        message: '',
      };
    },
    methods: {
      async onSubmit() {
        try {
          // 调用登录 API
        } catch (error) {
          this.message = '登录失败';
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .login {
    max-width: 400px;
    margin: 0 auto;
  }
  .login h2 {
    text-align: center;
  }
  .login form {
    display: flex;
    flex-direction: column;
  }
  .login form div {
    margin-bottom: 15px;
  }
  .login form label {
    margin-bottom: 5px;
  }
  .login form input {
    padding: 8px;
    font-size: 16px;
  }
  .login form button {
    padding: 10px;
    font-size: 16px;
  }
  </style>
  