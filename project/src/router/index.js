import { createRouter, createWebHashHistory } from 'vue-router'
import MainLayout from '../views/MainLayout.vue' // 确保 MainLayout 正确导入
import index from '../views/index.vue'
import text from '../views/text.vue'
import LoginPage from '../views/LoginPage.vue'
import ChangePage from '../views/ChangePage.vue'
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import ChangePassword from '../views/ChangePassword.vue';
import JobTrend from '../views/JobTrend.vue';
import GraduateData from '../views/GraduateData.vue';
import Feedback from '../views/Feedback.vue';


const routes = [
    {
        path: '/MainLayout',
        name: 'MainLayout',
        component: MainLayout
    },
    {
        path: '/index',
        name: 'index',
        component: index
    },
    {
        path: '/text',
        name: 'text',
        component: text
    },
    {
        path: '/login', // 保持小写路径
        name: 'LoginPage',
        component: LoginPage
    },
    {
        path: '/change', // 保持小写路径
        name: 'ChangePage',
        component: ChangePage
    },
    { path: '/login2',name: 'login2', component: Login },
    { path: '/register',name: 'register', component: Register },
    { path: '/change-password',name: 'change-password', component: ChangePassword },
    ,
    {
        path: '/job-trend', // 保持小写路径
        name: 'JobTrend',
        component: JobTrend
    },
    {
        path: '/graduate-data', // 保持小写路径
        name: 'GraduateData',
        component: GraduateData
    },
    {
        path: '/feed-back', // 保持小写路径
        name: 'Feedback',
        component: Feedback
    }

]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
