<template>
  <div class="body">
    <!-- 视频背景 -->
    <div class="video-bg">
      <video autoplay loop muted>
        <source src="../assets/img/7btrrd.mp4" type="video/mp4" />
      </video>
    </div>

    <!-- 主题切换 -->
    <div :class="['dark-light', isBlack ? 'black-text' : 'white-text']" @click="toggleTheme">
      <svg
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="3"
        fill="none"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" />
      </svg>
    </div>

    <!-- 主体 -->
    <div class="app">
      <!-- Header -->
      <div class="header">
        <div class="logo"></div>
        <div class="nav">
          <router-link class="menu-link notify is-active" to="/MainLayout">平台主页</router-link>
          <router-link class="menu-link" to="/graduate-data">毕业生数据</router-link>
          <router-link class="menu-link" to="/job-trend">就业趋势分析</router-link>
          <router-link class="menu-link" to="/feed-back">企业反馈</router-link>
        </div>
        <div class="search-box">
          <input type="text" placeholder="搜索毕业生数据" />
        </div>
        <div class="header-right">
          <div class="notification">
            <span class="notification-number">3</span>
            <svg
              viewBox="0 0 24 24"
              fill="currentColor"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="feather feather-bell"
            >
              <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 01-3.46 0" />
            </svg>
          </div>
          <svg viewBox="0 0 512 512" fill="currentColor">
            <path
              d="M448.773 235.551A135.893 135.893 0 00451 211c0-74.443-60.557-135-135-135-47.52 0-91.567 25.313-115.766 65.537-32.666-10.59-66.182-6.049-93.794 12.979-27.612 19.013-44.092 49.116-45.425 82.031C24.716 253.788 0 290.497 0 331c0 7.031 1.703 13.887 3.006 20.537l.015.015C12.719 400.492 56.034 436 106 436h300c57.891 0 106-47.109 106-105 0-40.942-25.053-77.798-63.227-95.449z"
            />
          </svg>
          <img
            class="profile-img"
            src="https://images.unsplash.com/photo-1600353068440-6361ef3a86e8?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80"
            alt=""
          />
        </div>
      </div>

      <!-- 容器 -->
      <el-container class="container">
        <!-- Header -->
        <el-header class="header">
          <el-row>
            <el-col :span="8"></el-col>
            <el-col :span="8"></el-col>
            <el-col :span="8">
              <h1>
                <i class="fa fa-chart-line"></i> 面向B端大学生就业数据分析可视化平台
              </h1>
            </el-col>
          </el-row>
        </el-header>

        <!-- 主体内容 -->
        <el-main class="main-content">
          <!-- 技术栈展示 -->
          <section class="section">
            <h2><i class="fa fa-code"></i> 技术栈展示</h2>
            <el-row :gutter="10" class="tech-tags">
              <el-col :span="24">
                <el-tag type="dark">Vue.js</el-tag>
                <el-tag type="dark">ECharts/Chart.js</el-tag>
                <el-tag type="dark">Element Plus</el-tag>
                <el-tag type="dark">C# ASP.NET Web API</el-tag>
                <el-tag type="dark">Python</el-tag>
                <el-tag type="dark">SQL Server</el-tag>
              </el-col>
            </el-row>
          </section>

          <!-- 新增：就业趋势预测 -->
          <section class="section">
            <h2><i class="fa fa-info-circle"></i> 就业趋势预测</h2>
            <el-row>
              <el-button type="primary" @click="showPredictionDialog">打开就业预测功能</el-button>
            </el-row>
          </section>
        </el-main>

        <!-- 对话框：就业趋势预测 -->
        <el-dialog v-model="predictionDialogVisible" title="就业趋势预测" width="60%">
          <div class="chat-box">
            <div
              v-for="(message, index) in predictionMessages"
              :key="index"
              :class="['message', message.type]"
            >
              {{ message.content }}
            </div>
          </div>
          <el-input
            v-model="userPredictionInput"
            placeholder="请输入行业或职业，点击预测"
          ></el-input>
          <template #footer>
            <el-button type="primary" @click="handlePrediction">预测</el-button>
            <el-button @click="predictionDialogVisible = false">关闭</el-button>
          </template>
        </el-dialog>
      </el-container>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "index",
  data() {
    return {
      isLightMode: false,
      isBlack: true,
      predictionDialogVisible: false, // 对话框控制
      predictionMessages: [], // 聊天内容
      userPredictionInput: "", // 用户输入
    };
  },
 methods: {
  // 切换主题
  toggleTheme() {
    this.isLightMode = !this.isLightMode;
    this.isBlack = !this.isBlack;
    document.body.classList.toggle("light-mode");
  },

  // 打开就业预测对话框
  showPredictionDialog() {
    this.predictionDialogVisible = true;
  },

  // 调用 GPT 接口进行预测
  async handlePrediction() {
    if (!this.userPredictionInput.trim()) {
      this.$message.warning("请输入行业或职业！");
      return;
    }

    // 添加用户输入到聊天记录
    this.predictionMessages.push({
      content: this.userPredictionInput,
      type: "user",
    });

    try {
      // 使用 axios 发起请求
      const response = await axios.post(
        "https://open.xiaojingai.com/v1/chat/completions", // GPT 接口地址
        {
          model: "gpt-4o-mini", // 替换为您提供的模型
          messages: [
            {
              role: "user",
              content: `请预测未来5年内 ${this.userPredictionInput} 的就业趋势。`, // 动态插入用户输入内容
            },
          ],
          temperature: 0.7,
          max_tokens: 1000,
        },
        {
          headers: {
            Authorization: "Bearer sk-hC38rrC7CSqeH5BJ6071726a1e2c4395A723E2B40741E0Ba", // 替换为提供的密钥
            "Content-Type": "application/json",
          },
        }
      );

      // 处理 GPT 返回的结果
      const reply = response.data.choices[0].message.content;
      this.predictionMessages.push({
        content: reply,
        type: "bot",
      });
    } catch (error) {
      console.error("预测失败：", error);
      this.predictionMessages.push({
        content: "预测失败，请稍后重试。",
        type: "bot",
      });
    } finally {
      this.userPredictionInput = ""; // 清空输入框
    }
  },

  // 模拟逐字输出
  simulateStreamOutput(reply) {
    let index = 0;
    const messages = [...this.predictionMessages, { content: "", type: "bot" }];

    // 模拟逐字输出效果
    const interval = setInterval(() => {
      if (index < reply.length) {
        messages[messages.length - 1].content += reply[index];
        index++;
        this.predictionMessages = messages; // 实时更新消息内容
      } else {
        clearInterval(interval); // 输出完成后清除定时器
      }
    }, 50); // 每隔 50 毫秒显示一个字符
  },
},
};
</script>
<style lang="less" scoped>
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@200;300;400;500;600;700&display=swap");

* {
    font-family: "Gotham Rounded Book", "sans-serif";
    padding: 0;
    margin: 0;
    color: black;
    color: black;
}

/* 颜色变量 */
:root {
  
  --black-color: #000;
  --white-color: #fff;
  --theme-bg-color: rgba(16, 18, 27, 0.4);
  --theme-color: #f9fafb;
  --inactive-color: rgba(113, 119, 144, 0.78);
  --border-color: rgba(0, 0, 0, 0.25);
  --hover-menu-bg: rgba(12, 15, 25, 0.3);
  --content-bg: rgba(146, 151, 179, 0.13);
  --button-inactive: rgba(249, 250, 251, 0.55);
  --dropdown-bg: #21242d;
  --dropdown-hover: rgb(42, 46, 60);
  --popup-bg: rgb(22, 25, 37);
  --search-bg: #14162b;
  --overlay-bg: rgba(36, 39, 59, 0.3);
  --scrollbar-bg: rgba(1, 2, 3, 0.4);
  --body-font: "Poppins", sans-serif;
}

body {
  font-family: var(--body-font);
  background-image: url('https://wallpapershome.com/images/wallpapers/macos-big-sur-1280x720-dark-wwdc-2020-22655.jpg');
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 2em;
  width: 100%;
  height: 100vh;
  position: relative;
  color: var(--black-color);

  .black-text {
    color: var(--black-color) !important;
  }

  .white-text {
    color: var(--white-color) !important;
  }

  &.light-mode {
    --theme-bg-color: rgba(255, 255, 255, 0.31);
    --theme-color: #3c3a3a;
    --inactive-color: #333333;
    --button-inactive: #3c3a3a;
    --search-bg: rgba(255, 255, 255, 0.31);
    --dropdown-bg: #080707;
    --overlay-bg: rgba(255, 255, 255, 0.3);
    --dropdown-hover: #ececec;
    --border-color: rgba(0, 0, 0, 0.35);
    --popup-bg: #0c0b0b;
    --hover-menu-bg: rgba(255, 255, 255, 0.35);
    --scrollbar-bg: rgba(255, 253, 253, 0.57);
    --content-title-color: var(--theme-color);
  }

  .video-bg {
    position: fixed;
    right: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: -1;

    video {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  img {
    max-width: 100%;
  }

  .dark-light {
    position: fixed;
    bottom: 50px;
    right: 30px;
    background-color: var(--dropdown-bg);
    box-shadow: -1px 3px 8px -1px rgba(0, 0, 0, 0.2);
    padding: 12px;
    border-radius: 50%;
    z-index: 3;
    cursor: pointer;
    transition: background-color 0.3s;

    svg {
      width: 24px;
      fill: #ffce45;
      stroke: #ffce45;
      transition: fill 0.5s, stroke 0.5s;
    }

    &.light-mode svg {
      fill: transparent;
      stroke: #080707;
    }
  }

  .app {
    background-color: var(--theme-bg-color);
    width: 100%;
    max-width: 1200px;
    height: auto;
    margin: 0 auto;
    padding: 20px;
    border-radius: 14px;
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
    box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    font-size: 16px;
    font-weight: 500;
  }

  .header {
    display: flex;
    align-items: center;
    height: 80px;
    width: 100%;
    border-bottom: 1px solid var(--border-color);
    padding: 0 30px;
    background-color: transparent;

    h1 {
      font-size:2.5rem;
      color: var(--theme-color);
      display: flex;
      align-items: center;

      i {
        margin-right: 10px;
      }
    }
  }

  .main-content {
    padding: 20px 30px;
    flex-grow: 1;
  }

  .section {
    margin-bottom: 40px;

    h2 {
      font-size: 1.25rem;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      color: var(--theme-color);

      i {
        margin-right: 10px;
      }
    }

    h3 {
      font-size: 1.1rem;
      margin-bottom: 15px;
      display: flex;
      align-items: center;
      color: var(--theme-color);

      i {
        margin-right: 8px;
      }
    }

    p {
      line-height: 1.6;
      color: var(--inactive-color);
    }

    .tech-tags {
      margin-top: 10px;
      display: flex;
      flex-wrap: wrap;

      .el-tag {
        margin: 5px 5px 0 0;
      }
    }

    .el-list {
      list-style: none;
      padding: 0;

      .el-list-item {
        padding: 8px 0;
        border-bottom: 1px solid var(--border-color);
        color: var(--theme-color);
        font-size: 1rem;

        &:last-child {
          border-bottom: none;
        }
      }
    }
  }

  .footer {
    text-align: center;
    padding: 20px 0;
    border-top: 1px solid var(--border-color);
    color: var(--inactive-color);
    font-size: 0.9rem;
  }

  /* Light Mode Overlay */
  &.light-mode:before,
  &.light-mode .video-bg:before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100vh;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.72) 0%, rgba(255, 255, 255, 0.45) 100%);
    backdrop-filter: saturate(3);
    z-index: -1;
  }

  /* Responsive Adjustments */
  @media screen and (max-width: 768px) {
    .app {
      padding: 15px;
    }

    .header {
      padding: 0 20px;

      h1 {
        font-size: 1.2rem;
      }
    }

    .section {
      margin-bottom: 30px;

      h2 {
        font-size: 1.1rem;
      }

      h3 {
        font-size: 1rem;
      }

      .tech-tags {
        .el-tag {
          margin: 4px 4px 0 0;
          font-size: 0.9rem;
        }
      }

      .el-list-item {
        font-size: 0.95rem;
      }
    }

    .dark-light {
      bottom: 30px;
      right: 20px;
      padding: 10px;

      svg {
        width: 20px;
      }
    }
  }

  @media screen and (max-width: 480px) {
    .app {
      padding: 10px;
    }

    .header {
      padding: 0 15px;

      h1 {
        font-size: 1rem;
      }
    }

    .section {
      margin-bottom: 20px;

      h2 {
        font-size: 1rem;
      }

      h3 {
        font-size: 0.95rem;
      }

      .tech-tags {
        .el-tag {
          margin: 3px 3px 0 0;
          font-size: 0.8rem;
        }
      }

      .el-list-item {
        font-size: 0.9rem;
      }
    }

    .dark-light {
      bottom: 20px;
      right: 15px;
      padding: 8px;

      svg {
        width: 18px;
      }
    }
  }
}

/* 通用元素样式 */
* {
  box-sizing: border-box;
}

a {
  color: inherit;
  text-decoration: none;
}

h1, h2, h3, p {
  margin: 0;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-thumb {
  background: var(--scrollbar-bg);
  border-radius: 10px;
}

/* 新增就业预测功能的样式 */

/* 聊天框容器 */
.chat-box {
  height: 300px;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 10px;
  margin-bottom: 20px;
  background: #f9f9f9;
  border-radius: 4px;
  font-size: 14px;
  line-height: 1.5;
}

/* 聊天消息的通用样式 */
.message {
  margin-bottom: 10px;
  padding: 8px 12px;
  border-radius: 12px;
  display: inline-block;
  max-width: 80%;
  word-wrap: break-word;
}

/* 用户消息样式（右对齐） */
.message.user {
  text-align: right;
  background-color: #e6f7ff;
  color: #409eff;
  float: right;
}

/* GPT 消息样式（左对齐） */
.message.bot {
  text-align: left;
  background-color: #f2f2f2;
  color: #606266;
  float: left;
}

/* 清除浮动 */
.chat-box::after {
  content: "";
  display: block;
  clear: both;
}

/* 输入框样式（继承 el-input） */
.el-input {
  margin-top: 10px;
}

/* 按钮样式 */
.el-button {
  margin-top: 10px;
}

</style>
