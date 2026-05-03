import Vue from 'vue';
import Router from 'vue-router';

// --- 解决Vue-Router重复导航报错的问题 ---
// 防止用户快速连续点击同一个链接时，控制台出现报错。
const originalReplace = Router.prototype.replace;
Router.prototype.replace = function replace(location) {
    return originalReplace.call(this, location).catch(err => err);
};
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err);
};

Vue.use(Router);

// 定义我们网站的基础标题
const baseTitle = '闲鸭 - SYSU 校园二手平台';

export default new Router({
    routes: [
        // 默认根路径重定向到首页
        {
            path: '/',
            redirect: '/index'
        },
        // --- 主要页面路由 ---
        {
            path: '/index',
            component: () => import('../components/page/index.vue'),
            // 首页直接使用基础标题
            meta: { title: baseTitle }
        },
        {
            path: '/search',
            component: () => import('../components/page/search.vue'),
            meta: { title: `搜索结果 - ${baseTitle}` }
        },
        {
            path: '/me',
            component: () => import('../components/page/me.vue'),
            meta: { title: `个人中心 - ${baseTitle}` }
        },
        {
            path: '/message',
            component: () => import('../components/page/message.vue'),
            meta: { title: `我的消息 - ${baseTitle}` }
        },
        {
            path: '/release',
            component: () => import('../components/page/release.vue'),
            meta: { title: `发布物品 - ${baseTitle}` }
        },
        {
            path: '/details',
            component: () => import('../components/page/idle-details.vue'),
            meta: { title: `物品详情 - ${baseTitle}` }
        },
        {
            path: '/order',
            component: () => import('../components/page/order.vue'),
            meta: { title: `确认订单 - ${baseTitle}` }
        },
        // --- 登录注册路由 ---
        {
            path: '/login',
            component: () => import('../components/page/login.vue'),
            meta: { title: `登录 - ${baseTitle}` }
        },
        {
            path: '/sign-in',
            component: () => import('../components/page/sign-in.vue'),
            meta: { title: `注册 - ${baseTitle}` }
        },
        // --- 管理员后台路由 ---
        {
            path: '/login-admin',
            component: () => import('../components/page/login-admin.vue'),
            meta: { title: `管理员登录 - ${baseTitle}` }
        },
        {
            path: '/platform-admin',
            component: () => import('../components/page/platform-admin.vue'),
            meta: { title: `后台管理 - ${baseTitle}` }
        },
        // --- 404页面处理 ---
        // 匹配所有未定义的路径，并重定向到首页
        {
            path: '*',
            redirect: '/'
        }
    ]
});