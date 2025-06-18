<template>
  <div class="body">
    <div class="video-bg">
      <video width="320" height="240" autoplay loop muted>
        <source src="../assets/img/7btrrd.mp4" type="video/mp4" />
      </video>
    </div>
    <div
      :class="isBlack ? 'black-text' : 'white-text'"
      class="dark-light"
      @click="toggleTheme"
    >
      <svg
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="3"
        fill="red"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" />
      </svg>
    </div>
    <div class="app">
      <!-- 绑定 v-model 到 authMode -->
      <input
        type="radio"
        name="optionScreen"
        id="SignIn"
        value="SignIn"
        v-model="authMode"
        hidden
        checked
      />
      <input
        type="radio"
        name="optionScreen"
        id="SignUp"
        value="SignUp"
        v-model="authMode"
        hidden
      />

      <section>
        <!-- 登录表单 -->
        <form
          @submit.prevent="handleSubmit"
          id="SignInFormData"
          v-if="!isRegister"
        >
          <div class="title" :style="{ color: textColor }">
            <h1>登录</h1>
          </div>

          <input
            v-model="loginForm.username"
            type="text"
            id="username"
            placeholder="用户名"
            style="font-weight: bold; color: black;"
            required
          />
          <input
            v-model="loginForm.password"
            type="password"
            id="password"
            placeholder="密码"
            style="font-weight: bold; color: black;"
            required
          />
          <button type="submit" title="Sign In" :style="{ color: textColor }">
            Let me in
          </button>
          <small :style="{ color: textColor }">
            Forgot Your Password?
            <router-link class="reset-link" to="/change">重置</router-link>
          </small>
          <small :style="{ color: textColor }">
            Don't have an account?
            <!-- 使用 label 触发 radio 按钮的切换 -->
            <label for="SignUp">注册</label>
          </small>
          <p v-if="authMessage" :style="{ color: authMessageColor }">
            {{ authMessage }}
          </p>
          <!-- 显示消息 -->
        </form>

        <!-- 注册表单 -->
        <form @submit.prevent="handleRegister" id="SignUpFormData" v-else>
          <div class="title">
            <h1 :style="{ color: textColor }">注册</h1>
          </div>
          <input
            v-model="registerForm.username"
            type="text"
            id="username"
            placeholder="用户名"
            style="font-weight: bold; color: black;"
            required
          />
          <input
            v-model="registerForm.email"
            type="email"
            id="email"
            placeholder="E-mail"
            style="font-weight: bold; color: black;"
            required
          />
          <input
            v-model="registerForm.password"
            type="password"
            id="password"
            placeholder="新密码"
            style="font-weight: bold; color: black;"
            required
          />
          <button type="submit" title="Sign Up">注册</button>
          <small :style="{ color: textColor }">
            返回 <label for="SignIn">登录</label>
          </small>
          <p v-if="authMessage" :style="{ color: authMessageColor }">
            {{ authMessage }}
          </p>
          <!-- 显示消息 -->
        </form>
      </section>

      <!-- 成功登录的模态框 -->
      <div v-if="showModal" class="modal-overlay">
        <div class="modal">
          <h2>欢迎登陆，管理员！</h2>
          <button @click="closeModal">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios"; // 导入 axios

export default {
  name: "AuthForm",
  data() {
    return {
      isLightMode: false,
      isBlack: true,
      showResetPassword: false, // 控制重置密码模态框显示
      showChangePassword: false, // 控制修改密码模态框显示
      resetEmail: "",
      currentPassword: "",
      newPassword: "",
      confirmNewPassword: "",
      resetMessage: "",
      changeMessage: "",
      // 用于表单数据的绑定
      loginForm: {
        username: "",
        password: "",
      },
      registerForm: {
        username: "",
        email: "",
        password: "",
      },
      // 用于显示错误或成功消息
      authMessage: "",
      // 用于绑定 radio 按钮
      authMode: "SignIn", // 默认显示登录
      // 控制模态框显示
      showModal: false,
    };
  },

  computed: {
    isRegister() {
      return this.authMode === "SignUp";
    },
    textColor() {
      return this.isLightMode
        ? "var(--light-text-color)"
        : "var(--dark-text-color)";
    },
    authMessageColor() {
      // 根据 authMessage 的内容决定颜色
      if (this.authMessage.includes("成功")) {
        return "green";
      } else if (this.authMessage.includes("失败")) {
        return "red";
      }
      return "black";
    },
  },

  methods: {
    toggleColor() {
      this.isBlack = !this.isBlack;
    },
    toggleResetPassword() {
      this.showResetPassword = !this.showResetPassword; // 切换重置密码模态框显示
      this.showChangePassword = false;
    },
    toggleChangePassword() {
      this.showChangePassword = !this.showChangePassword; // 切换修改密码模态框显示
      this.showResetPassword = false;
    },
    toggleTheme() {
      this.isLightMode = !this.isLightMode;
      document.body.classList.toggle("light-mode");

      // 使用 CSS 变量动态更改文本颜色
      const textColor = this.isLightMode
        ? "var(--light-text-color)"
        : "var(--dark-text-color)";
      document.documentElement.style.setProperty(
        "--login-text-color",
        textColor
      );
      document.documentElement.style.setProperty("--text-color", textColor);
    },
    async handleSubmit() {
  this.authMessage = ""; // 清空之前的消息

  if (!this.isRegister) {
    // 登录逻辑
    const { username, password } = this.loginForm;
    console.log("Attempting to log in with:", username, password);

    try {
      const response = await axios.post(
        "http://localhost:5185/api/Account/Login",
        {
          username: username,
          password: password,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log("Response status:", response.status);
      console.log("Response data:", response.data);

      // 假设成功响应状态码为 200
      if (response.status === 200) {
        this.authMessage = "成功登陆，欢迎管理员！";

        // 显示模态框
        this.showModal = true;
        console.log("Modal should now be visible:", this.showModal);
        // 重置表单
        this.loginForm.username = "";
        this.loginForm.password = "";
        setTimeout(() => {
          this.$router.push({ name: 'index' })
            .then(() => {
              console.log("跳转到 /index 成功");
            })
            .catch(err => {
              console.error("跳转到 /index 失败:", err);
            });
        }, 2000);

      } else {
        this.authMessage = "登录失败，请检查用户名和密码。";
      }
    } catch (error) {
      console.error("Login error:", error);
      // 处理错误响应
      if (error.response && error.response.status === 401) {
        this.authMessage = "登录失败：无效的用户名或密码。";
      } else {
        this.authMessage = "登录失败：服务器错误，请稍后再试。";
      }
    }
  } else {
    // 注册逻辑
    try {


      // 这里可以添加注册逻辑，例如调用 API
      // 由于没有后端支持，这里仅模拟成功注册
      this.authMessage = "注册成功！请登录。";
      // 切换回登录模式
      this.authMode = "SignIn";
      
      const response = await axios.post(
        "http://localhost:5185/api/Account/Register",
        {
          username: this.registerForm.username,
          email:this.registerForm.email,
          password: this.registerForm.password,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      // 清空注册表单
      this.registerForm.username = "";
      this.registerForm.email = "";
      this.registerForm.password = "";


    } catch (error) {
      this.authMessage = "注册失败，请稍后再试。";
    }
  }
},

    handleRegister() {
      // 如果有单独的注册逻辑，可以在这里实现
      // 目前 handleSubmit 已经处理了注册逻辑
      this.handleSubmit();
    },

    closeModal() {
      console.log("Closing modal and redirecting...");
  this.showModal = false;
  // 跳转到 index 页面
  this.$router.push("/index");
    },

    resetPassword() {
      if (this.resetEmail) {
        this.resetMessage = `Reset password link sent to: ${this.resetEmail}`;
        this.resetEmail = ""; // 清空输入框
        this.showResetPassword = false; // 关闭模态框
      } else {
        this.resetMessage = "Please enter a valid email.";
      }
    },

    changePassword() {
      if (this.newPassword === this.confirmNewPassword) {
        console.log(
          `Changing password from: ${this.currentPassword} to: ${this.newPassword}`
        );
        this.currentPassword = "";
        this.newPassword = "";
        this.confirmNewPassword = "";
        this.changeMessage = "Password changed successfully.";
        this.showChangePassword = false; // 关闭模态框
      } else {
        this.changeMessage = "New passwords do not match.";
      }
    },
  },
};
</script>


<style lang="less">
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@200;300;400;500;600;700&display=swap");

* {
  outline: none;
  box-sizing: border-box;
}

:root {
 // --text-color: var(--dark-text-color); /* 默认文本颜色为暗模式 */
 

  --light-text-color: black; /* 光模式文本颜色 */
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
  //color: #eee;
  color: var(--text-color); /* 使用文本颜色变量 */
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
.reset-link {
  color: #1ed760;
  text-decoration: none;
  &:hover {
    color: #1ed760;
    text-decoration: none;
  }
}
.logo {
  justify-content: center;
  display: flex;
  align-items: center;
  margin: 30px;
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

.title {
  margin: 0 auto;
}

input,
button {
//  color: #111;
color: var(--text-color); /* 使用文本颜色变量 */
  padding: 12px 15px;
  border: none;
  outline: none;
  font-size: 1rem;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
}

button {
  // background: #1ed760;
  text-transform: uppercase;
  font-weight: bold;
  //color: #fff;
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
