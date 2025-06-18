import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import 'element-plus/dist/index.css'; // 引入 Element Plus 样式
import 'bootstrap/dist/css/bootstrap.min.css'; // 如果确实需要 Bootstrap
import 'font-awesome/css/font-awesome.min.css'; // 如果确实需要 Font Awesome
import { createI18n } from 'vue-i18n';
import ElementPlus from 'element-plus';

const messages = {
  en: {
    message: {
      hello: 'Hello World'
    }
  },
  zh: {
    message: {
      hello: '你好，世界'
    }
  }
};

const i18n = createI18n({
  locale: 'zh', // 默认语言设为中文
  fallbackLocale: 'en',
  messages,
});

const app = createApp(App);
app.use(ElementPlus)
.use(store)
   .use(router)
   .use(i18n)
   .mount('#app');
