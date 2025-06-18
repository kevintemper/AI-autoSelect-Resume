<template>
  <div class="body">
    <div class="video-bg">
      <video width="320" height="240" autoplay loop muted>
        <source src="../assets/img/7btrrd.mp4" type="video/mp4">
      </video>
    </div>
    <div :class="isBlack ? 'black-text' : 'white-text'" class="dark-light" @click="toggleTheme">
      <svg viewBox="0 0 24 24" stroke="currentColor" stroke-width="3" fill="red" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" />
      </svg>
    </div>
    <div class="app">
      <input type="radio" name="optionScreen" id="SignIn" hidden checked>
      <input type="radio" name="optionScreen" id="SignUp" hidden>

      <section>
        <!-- 修改密码表单 -->
        <form @submit.prevent="handleChangePassword" id="SignInFormData">
          <div class="title">
            <h1 style="color: var(--text-color); /* 使用文本颜色变量 */">修改密码</h1>
          </div>

          <input type="password" v-model="currentPassword" placeholder="当前密码" style="font-weight: bold;
  color: black;">
          <input type="password" v-model="newPassword" placeholder="新密码" style="font-weight: bold;
  color: black;">
          <input type="password" v-model="confirmNewPassword" placeholder="确认新密码" style="font-weight: bold;
  color: black;">
          
          <button type="submit" title="修改密码" style="color: var(--text-color); /* 使用文本颜色变量 */">修改密码</button>

          <small style="color: var(--text-color); /* 使用文本颜色变量 */">
            忘记密码? <label for="SignUp" @click="toggleAuthMode">找回</label>
          </small>
          
          <!-- 显示修改密码的反馈消息 -->
          <p v-if="changeMessage" :class="changeMessageType === 'success' ? 'success-message' : 'error-message'">
            {{ changeMessage }}
          </p>
        </form>

        <!-- 找回密码表单 -->
        <form @submit.prevent="handleRecoverPassword" id="SignUpFormData">
          <div class="title">
            <h1 style="color: var(--text-color); /* 使用文本颜色变量 */">找回密码</h1>
          </div>
          <input type="text" v-model="nickname" placeholder="用户名" required style="font-weight: bold;
  color: black;">
          <input type="email" v-model="resetEmail" placeholder="E-mail" required style="font-weight: bold;
  color: black;">
          
          <button type="submit" title="找回密码">找回密码</button>
          <small>返回 <router-link class="back-link" to="/login">登录</router-link></small>
          
          <!-- 显示找回密码的反馈消息 -->
          <p v-if="resetMessage" :class="resetMessageType === 'success' ? 'success-message' : 'error-message'">
            {{ resetMessage }}
          </p>
        </form>
      </section>
    </div>
  </div>
</template>

<script>
import axios from "axios"; // 导入 axios
export default {
  data() {
    return {
      isLightMode: false,
      isBlack: true,
      isRegister: false,
      showResetPassword: false,
      showChangePassword: false,
      nickname: '',
      resetEmail: '',
      currentPassword: '',
      newPassword: '',
      confirmNewPassword: '',
      resetMessage: '',
      resetMessageType: '',
      changeMessage: '',
      changeMessageType: '',
      smtpHost: 'smtp.163.com',
      smtpPort: 465,
      smtpUsername: '19120338015@163.com',
      smtpPassword: 'CFsjn3S2ULcb7VjD', // 建议使用授权码
      fromEmail: '18622014903@163.com',
      fromName: 'Your Name',
    };
  },
  
  mounted() {
    this.loadSMTPJS();
  },
  
  methods: {
    loadSMTPJS() {
      return new Promise((resolve, reject) => {
        if (typeof Email !== 'undefined') {
          resolve();
          return;
        }
        const script = document.createElement('script');
        script.src = 'https://smtpjs.com/v3/smtp.js';
        script.onload = () => {
          resolve();
        };
        script.onerror = () => {
          reject(new Error('SMTP.js 加载失败'));
        };
        document.head.appendChild(script);
      });
    },
    
    toggleColor() {
      this.isBlack = !this.isBlack;
    },
    
    toggleResetPassword() { 
      this.showResetPassword = !this.showResetPassword;
      this.showChangePassword = false;
    },
    
    toggleChangePassword() { 
      this.showChangePassword = !this.showChangePassword;
      this.showResetPassword = false;
    },
    
    toggleTheme() {
      this.isLightMode = !this.isLightMode;
      document.body.classList.toggle('light-mode');

      const textColor = this.isLightMode ? 'var(--light-text-color)' : 'var(--dark-text-color)';
      document.documentElement.style.setProperty('--login-text-color', textColor);
      document.documentElement.style.setProperty('--text-color', textColor); 
    },
    
    toggleAuthMode() {
      this.isRegister = !this.isRegister;
      this.showResetPassword = false;
      this.showChangePassword = false;
    },
    
    async handleChangePassword() {
      if (this.newPassword !== this.confirmNewPassword) {
        this.changeMessage = '新密码与确认密码不匹配。';
        this.changeMessageType = 'error';
        return;
      }
      
      try {
        const payload = {
          currentPassword: this.currentPassword,
          newPassword: this.newPassword,
        };
        const response = await this.$store.dispatch('changePassword', payload);
        this.changeMessage = response.message || '密码修改成功。';
        this.changeMessageType = 'success';
        this.currentPassword = '';
        this.newPassword = '';
        this.confirmNewPassword = '';
      } catch (error) {
        this.changeMessage = error.message || '密码修改失败。';
        this.changeMessageType = 'error';
      }
    },
    
    async handleRecoverPassword() {
      if (!this.nickname || !this.resetEmail) {
        this.resetMessage = '请输入有效的用户名和邮箱。';
        this.resetMessageType = 'error';
        return;
      }
      
      try {
        await this.loadSMTPJS(); // 确保 SMTP.js 已加载

        const initialPassword = 'zwt_5201314';
        
        const response = await axios.post(

        "http://localhost:5185/api/passwordreset",
        {
          username: this.nickname,
          email:this.resetEmail,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );


        // 使用SMTP.js发送邮件
        await Email.send({
          Host: this.smtpHost,
          Username: this.smtpUsername,
          Password: this.smtpPassword,
          To: this.resetEmail,
          From: this.fromEmail,
          Subject: '密码重置',
          Body: `
            亲爱的 ${this.nickname},<br><br>
            您的初始密码是：<strong>${initialPassword}</strong><br><br>
            请登录并及时修改密码以确保账户安全。<br><br>
            谢谢！
          `,
          Secure: this.smtpPort === 465, // 对于端口465使用SSL
        });
        
        this.resetMessage = '邮件发送成功，请检查您的邮箱。';
        this.resetMessageType = 'success';
        this.nickname = '';
        this.resetEmail = '';
      } catch (error) {
        console.error('发送邮件错误:', error);
        if (error && error.Message) {
          this.resetMessage = `找回密码失败：${error.Message}`;
        } else {
          this.resetMessage = '找回密码失败。';
        }
        this.resetMessageType = 'error';
      }
    }
  },
  
  computed: {
    colorModeClass() {
      return this.isLightMode ? 'light-mode-text' : 'dark-mode-text';
    },
    isAuthenticated() {
      return this.$store.getters.isAuthenticated;
    },
  },
};
</script>
<style lang="less">
//@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@200;300;400;500;600;700&display=swap");
/* Black 900 */
@font-face {
  font-family: 'Poppins';
  font-style: normal;
  font-weight: 900;
  src: url('../assets/fonts/Poppins-Black.woff2') format('woff2'),
       url('../assets/fonts/Poppins-Black.woff') format('woff');
}

@font-face {
  font-family: 'Poppins';
  font-style: italic;
  font-weight: 900;
  src: url('../assets/fonts/Poppins-BlackItalic.woff2') format('woff2'),
       url('../assets/fonts/Poppins-BlackItalic.woff') format('woff');
}

/* Bold 700 */
@font-face {
  font-family: 'Poppins';
  font-style: normal;
  font-weight: 700;
  src: url('../assets/fonts/Poppins-Bold.woff2') format('woff2'),
       url('../assets/fonts/Poppins-Bold.woff') format('woff');
}

@font-face {
  font-family: 'Poppins';
  font-style: italic;
  font-weight: 700;
  src: url('../assets/fonts/Poppins-BoldItalic.woff2') format('woff2'),
       url('../assets/fonts/Poppins-BoldItalic.woff') format('woff');
}

/* Extra Bold 800 */
@font-face {
  font-family: 'Poppins';
  font-style: normal;
  font-weight: 800;
  src: url('../assets/fonts/Poppins-ExtraBold.woff2') format('woff2'),
       url('../assets/fonts/Poppins-ExtraBold.woff') format('woff');
}

@font-face {
  font-family: 'Poppins';
  font-style: italic;
  font-weight: 800;
  src: url('../assets/fonts/Poppins-ExtraBoldItalic.woff2') format('woff2'),
       url('../assets/fonts/Poppins-ExtraBoldItalic.woff') format('woff');
}

/* Light 300 */
@font-face {
  font-family: 'Poppins';
  font-style: normal;
  font-weight: 300;
  src: url('../assets/fonts/Poppins-Light.woff2') format('woff2'),
       url('../assets/fonts/Poppins-Light.woff') format('woff');
}

@font-face {
  font-family: 'Poppins';
  font-style: italic;
  font-weight: 300;
  src: url('../assets/fonts/Poppins-LightItalic.woff2') format('woff2'),
       url('../assets/fonts/Poppins-LightItalic.woff') format('woff');
}

/* Regular 400 */
@font-face {
  font-family: 'Poppins';
  font-style: normal;
  font-weight: 400;
  src: url('../assets/fonts/Poppins-Regular.woff2') format('woff2'),
       url('../assets/fonts/Poppins-Regular.woff') format('woff');
}

@font-face {
  font-family: 'Poppins';
  font-style: italic;
  font-weight: 400;
  src: url('../assets/fonts/Poppins-Italic.woff2') format('woff2'),
       url('../assets/fonts/Poppins-Italic.woff') format('woff');
}

/* SemiBold 600 */
@font-face {
  font-family: 'Poppins';
  font-style: normal;
  font-weight: 600;
  src: url('../assets/fonts/Poppins-SemiBold.woff2') format('woff2'),
       url('../assets/fonts/Poppins-SemiBold.woff') format('woff');
}

@font-face {
  font-family: 'Poppins';
  font-style: italic;
  font-weight: 600;
  src: url('../assets/fonts/Poppins-SemiBoldItalic.woff2') format('woff2'),
       url('../assets/fonts/Poppins-SemiBoldItalic.woff') format('woff');
}
* {
  outline: none;
  box-sizing: border-box;
}

:root { 
  --text-color:  white;/* 默认文本颜色为暗模式 */
  --light-text-color:black; /* 光模式文本颜色 */
  --dark-text-color: white; /* 暗模式文本颜色 */
  --login-text-color: plum; /* 默认颜色 */
  --dropdown-bg: #21242d;
  --theme-bg-color: rgba(16, 18, 27, 0.4);
  --border-color: rgba(0, 0, 0, 0.25);
  --theme-color: #f9fafb;
  --inactive-color: rgb(113, 119, 144, 0.78);
  --body-font: "Poppins", sans-serif;
  --hover-menu-bg: rgba(12, 15, 25, 0.3);
  --content-title-color: #999ba5;
  --content-bg: rgb(146, 151, 179, 0.13);
  --button-inactive: rgb(249, 250, 251, 0.55);
  --search-bg: #14162b;
  --overlay-bg: rgba(36, 39, 59, 0.3);
  --scrollbar-bg: rgb(1, 2, 3, 0.4);
}

body {
  
  color: var(--text-color);
  font-family: var(--body-font);
  background-image: url(https://wallpapershome.com/images/wallpapers/macos-big-sur-1280x720-dark-wwdc-2020-22655.jpg);
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 2em;
  width: 100%;
  height: 100vh;
}

.app {
  height: 630px;
  width: 950px;
  margin: 0 auto;
  max-width: 950px;
  max-height: 630px;
}

* {
    font-family: "Gotham Rounded Book", "sans-serif";
    padding: 0;
    margin: 0;
    box-sizing: border-box;
    
}

::selection {
    background: 0;
}

body {
    width: 100vw;
    min-height: 560px;
    height: 100vh;
    display: grid;
    place-content: center;
    // background: #1ed760;
    overflow: hidden;
}

// section {
// 	min-width: 300px;
// 	max-width: 450px;
// 	min-height: 520px;
// 	width: 100vw;
// 	padding: 15px;
// 	box-shadow: rgb(38, 57, 77) 0px 20px 30px -10px;
// 	background: #2c2d31;
// 	overflow: hidden;
// 	position: relative;
// }

.logo {
    justify-content: center;
    display: flex;
    align-items: center;
    margin: 30px;
}
.back-link{
  color:#1ed760;
  text-decoration: none;
  &:hover{
text-decoration: none;
color:#1ed760;
  }
}

img {
    margin-right: 5px;
}

form {
    position: absolute;
    left: 0;
    top: 140px;
    width: 78%;
    transform: translatex(140%);
    display: flex;
    flex-direction: column;
    transition: 0.3s;
    gap: 10px;
}

#SignIn:checked ~ section #SignInFormData,
#SignUp:checked ~ section #SignUpFormData {
    transform: translatex(15%);
}


.title{
  margin:0 auto;
}




input,
button {
  color: var(--text-color); /* 使用文本颜色变量 */
    padding: 12px 15px;
    border: none;
    outline: none;
    font-size: 1rem;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
}

button {
    background: #1ed760;
    text-transform: uppercase;
    font-weight: bold;
   // color: #fff;
   color: var(--text-color); /* 使用文本颜色变量 */
    cursor: pointer;
    margin-bottom: 10px;
}

form span {
    margin-left: 20px;
    display: flex;
    gap: 5px;
}

form span label {
    cursor: pointer;
    font-size: 14px;
    text-transform: lowercase;
}

input[type="checkbox"] {
    cursor: pointer;
    accent-color: #1ed760; /* #3498db; */
}

a {
    font-weight: bold;
    text-decoration: none;
    margin-top: 20px;
    cursor: pointer;
    text-align: center;
}

small {
    text-align: center;
}

small label {
    color: #1ed760;
}

@media (max-width: 450px) {
    body {
        background: #2c2d31;
    }
    section {
        box-shadow: none;
    }
}

</style>
