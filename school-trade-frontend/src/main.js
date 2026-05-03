import Vue from 'vue';
import App from './App.vue';
import router from './router'; // 导入我们配置好的路由实例
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'babel-polyfill'; // 用于兼容旧版浏览器的polyfill

// 导入封装好的API请求模块
import api from './api/index.js';
// 将api挂载到Vue的原型上，这样在所有组件中都可以通过 this.$api 来调用后端接口
Vue.prototype.$api = api;

// --- 全局状态管理 ---
// 这是一个简单的、非Vuex的状态管理方案，用于在组件间共享数据。

// 用于存储全局用户数据
let globalData = {
    userInfo: {
        nickname: ''
    }
};
Vue.prototype.$globalData = globalData;

// 用于存储全局状态，如登录状态等
let sta = {
    isLogin: false,
    adminName: ''
};
Vue.prototype.$sta = sta;


// --- Vue 全局配置 ---
Vue.config.productionTip = false; // 关闭生产模式下的提示

// 全局注册Element-UI组件库
Vue.use(ElementUI, {
    size: 'medium' // 设置Element-UI组件的默认尺寸
});


// --- 全局路由守卫 (Navigation Guard) ---
// router.beforeEach 是一个钩子函数，它在每次路由跳转之前被触发。
// to:   即将进入的目标路由对象
// from: 当前正要离开的路由对象
// next: 一个必须被调用的函数，用于解析这个钩子。next() 表示放行。
router.beforeEach((to, from, next) => {
    
    // 1. 更新页面标题
    // 从目标路由的meta字段中获取我们预设的标题
    if (to.meta.title) {
        document.title = to.meta.title;
    } else {
        // 如果某个路由没有设置标题，就使用一个默认的
        document.title = '闲鸭 - SYSU 校园二手平台';
    }

    // 2. 登录权限校验
    // 判断全局状态中是否已有用户信息
    const hasUserInfo = Vue.prototype.$globalData.userInfo && Vue.prototype.$globalData.userInfo.id;
    
    // 定义需要登录才能访问的页面路径
    const protectedPaths = ['/me', '/message', '/release', '/order'];

    // 如果用户未登录，但想访问受保护的页面
    if (!hasUserInfo && protectedPaths.includes(to.path)) {
        // 尝试通过调用后端接口获取用户信息
        api.getUserInfo().then(res => {
           // 如果后端返回状态码不为1，说明cookie无效或已过期
           if (res.status_code !== 1) {
               // 跳转到登录页
               next('/login');
           } else {
               // 获取用户信息成功，将其存入全局状态
               res.data.signInTime = res.data.signInTime.substring(0, 10);
               Vue.prototype.$globalData.userInfo = res.data;
               Vue.prototype.$sta.isLogin = true;
               // 放行，让用户继续访问他想去的页面
               next();
           }
        }).catch(e => {
            // 如果API请求直接失败 (比如网络错误)，也跳转到登录页
            next('/login');
        });
    } else {
        // 如果用户已登录，或访问的是公共页面，则直接放行
        next();
    }
});


// --- 创建并挂载Vue根实例 ---
new Vue({
    router, // 将路由实例注入到Vue根实例中
    render: h => h(App) // 渲染根组件App.vue
}).$mount('#app'); // 将Vue实例挂载到index.html中id为'app'的元素上