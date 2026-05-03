import request from '../utils/request';

// 获取后端的baseURL
const getBaseUrl = () => {
    // request.defaults.baseURL 是在 utils/request.js 中配置的后端地址
    // 如果没有配置，就使用本地开发地址
    return request.defaults.baseURL || 'http://localhost:8080';
};

const api = {
    // 将获取baseURL的方法也导出，方便其他组件使用
    getBaseUrl,

    // 【新增】调用后端AI接口生成描述
    generateDescription(data) {
        return request({
            url: '/ai/generate-description', //【核心】在URL前面加上 /api
            method: 'post',
            data: data
        });
    },

    // --- 用户 & 认证 API ---
    userLogin(query) {
        return request({
            url: '/user/login',
            method: 'get',
            params: query
        });
    },
    logout(query) {
        return request({
            url: '/user/logout',
            method: 'get',
            params: query
        });
    },
    signIn(data) {
        return request({
            url: '/user/sign-in',
            method: 'post',
            data: data
        });
    },
    getUserInfo(query) {
        return request({
            url: '/user/info',
            method: 'get',
            params: query
        });
    },
    updateUserPublicInfo(data) {
        return request({
            url: '/user/info',
            method: 'post',
            data: data
        });
    },
    updatePassword(query) {
        return request({
            url: '/user/password',
            method: 'get',
            params: query
        });
    },

    // --- 地址 API ---
    addAddress(data) {
        return request({
            url: '/address/add',
            method: 'post',
            data: data
        });
    },
    getAddress(query) {
        return request({
            url: '/address/info',
            method: 'get',
            params: query
        });
    },
    updateAddress(data) {
        return request({
            url: '/address/update',
            method: 'post',
            data: data
        });
    },
    deleteAddress(data) {
        return request({
            url: '/address/delete',
            method: 'post',
            data: data
        });
    },

    // --- 闲置物品 API ---
    addIdleItem(data) {
        return request({
            url: '/idle/add',
            method: 'post',
            data: data
        });
    },
    getIdleItem(query) {
        return request({
            url: '/idle/info',
            method: 'get',
            params: query
        });
    },
    getAllIdleItem(query) {
        return request({
            url: '/idle/all',
            method: 'get',
            params: query
        });
    },
    findIdleItem(query) {
        return request({
            url: '/idle/find',
            method: 'get',
            params: query
        });
    },
    findIdleItemByLabel(query) {
        return request({
            url: '/idle/label',
            method: 'get',
            params: query
        });
    },
    updateIdleItem(data) {
        return request({
            url: '/idle/update',
            method: 'post',
            data: data
        });
    },

    // --- 订单 API ---
    addOrder(data) {
        return request({
            url: '/order/add',
            method: 'post',
            data: data
        });
    },
    getOrder(query) {
        return request({
            url: '/order/info',
            method: 'get',
            params: query
        });
    },
    updateOrder(data) {
        return request({
            url: '/order/update',
            method: 'post',
            data: data
        });
    },
    getMyOrder(query) {
        return request({
            url: '/order/my',
            method: 'get',
            params: query
        });
    },
    getMySoldIdle(query) {
        return request({
            url: '/order/my-sold',
            method: 'get',
            params: query
        });
    },
    addOrderAddress(data) {
        return request({
            url: '/order-address/add',
            method: 'post',
            data: data
        });
    },
    updateOrderAddress(data) {
        return request({
            url: '/order-address/update',
            method: 'post',
            data: data
        });
    },
    getOrderAddress(query) {
        return request({
            url: '/order-address/info',
            method: 'get',
            params: query
        });
    },
    addFavorite(data) {
        return request({
            url: '/favorite/add',
            method: 'post',
            data: data
        });
    },
    getMyFavorite(query) {
        return request({
            url: '/favorite/my',
            method: 'get',
            params: query
        });
    },
    deleteFavorite(query) {
        return request({
            url: '/favorite/delete',
            method: 'get',
            params: query
        });
    },
    checkFavorite(query) {
        return request({
            url: '/favorite/check',
            method: 'get',
            params: query
        });
    },
    sendMessage(data) {
        return request({
            url: '/message/send',
            method: 'post',
            data: data
        });
    },
    getMessage(query) {
        return request({
            url: '/message/info',
            method: 'get',
            params: query
        });
    },
    getAllIdleMessage(query) {
        return request({
            url: '/message/idle',
            method: 'get',
            params: query
        });
    },
    getAllMyMessage(query) {
        return request({
            url: '/message/my',
            method: 'get',
            params: query
        });
    },
    deleteMessage(query) {
        return request({
            url: '/message/delete',
            method: 'get',
            params: query
        });
    },
    getGoods(query) {
        return request({
            url: '/admin/idleList',
            method: 'get',
            params: query
        });
    },
    updateGoods(query) {
        return request({
            url: '/admin/updateIdleStatus',
            method: 'get',
            params: query
        });
    },

    getOrderList(query) {
        return request({
            url: '/admin/orderList',
            method: 'get',
            params: query
        });
    },
    deleteOrder(query) {
        return request({
            url: '/admin/deleteOrder',
            method: 'get',
            params: query
        });
    },
    getUserData(query) {
        return request({
            url: '/admin/userList',
            method: 'get',
            params: query
        });
    },
    getUserManage(query) {
        return request({
            url: '/admin/list',
            method: 'get',
            params: query
        });
    },
    updateUserStatus(query){
        return request({
            url: '/admin/updateUserStatus',
            method: 'get',
            params: query
        });
    },
    regAdministrator(data){
        return request({
            url: '/admin/add',
            method: 'post',
            data: data
        });
    },
    adminLogin(query) {
        return request({
            url: '/admin/login',
            method: 'get',
            params: query
        });
    },
    loginOut(query) {
        return request({
            url: '/admin/loginOut',
            method: 'get',
            params: query
        });
    }
};


export default api;