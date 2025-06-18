<!-- src/components/ChangePassword.vue -->
<template>
    <div class="change-password">
      <h2>修改密码</h2>
      <form @submit.prevent="onSubmit">
        <div>
          <label>用户名：</label>
          <input v-model="username" required />
        </div>
        <div>
          <label>旧密码：</label>
          <input type="password" v-model="oldPassword" required />
        </div>
        <div>
          <label>新密码：</label>
          <input type="password" v-model="newPassword" required />
        </div>
        <button type="submit">修改密码</button>
      </form>
      <p>{{ message }}</p>
    </div>
  </template>
  
  <script>
  import { changePassword } from '@/api/authService';
  
  export default {
    name: 'ChangePassword',
    data() {
      return {
        username: '',
        oldPassword: '',
        newPassword: '',
        message: '',
      };
    },
    methods: {
      async onSubmit() {
        try {
          const response = await changePassword({
            username: this.username,
            oldPassword: this.oldPassword,
            newPassword: this.newPassword,
          });
          this.message = response.data;
          // 修改密码成功后的逻辑
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
  