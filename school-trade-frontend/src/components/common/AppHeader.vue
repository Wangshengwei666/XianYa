<template>
    <div class="header">
        <div class="header-container">
            <div class="app-name">
                <router-link to="/">
                    <img src="../../assets/logo.png" alt="闲鸭Logo" class="app-logo">
                    <b class="app-title">校园二手交易平台</b>
                </router-link>
            </div>
            <div class="search-container">
                <el-input placeholder="发现好物..." v-model="searchValue" @keyup.enter.native="searchIdle">
                    <el-button slot="append" icon="el-icon-search" @click="searchIdle"></el-button>
                </el-input>
            </div>
            <el-button type="primary" icon="el-icon-plus"  @click="toRelease">物品发布</el-button>
            <el-button type="primary" icon="el-icon-chat-dot-round" @click="toMessage">我的消息</el-button>
            <router-link v-if="!isLogin" class="user-name-text" to="/login">登录</router-link>
            <el-dropdown trigger="click" v-else>
                <div style="cursor:pointer;display: flex;align-items: center;">
                    <div style="font-size: 16px;color: #409EFF;padding-right: 5px;">{{nicknameValue?nicknameValue:nickname}}</div>
                    <el-avatar :src="avatarValue?avatarValue:avatar"></el-avatar>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item><div @click="toMe">个人中心</div></el-dropdown-item>
                    <el-dropdown-item divided style="color: red;"><div @click="loginOut">退出登录</div></el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>
<script>

    export default {
        name: 'Header',
        props: ['searchInput','nicknameValue','avatarValue'],
        data() {
            return {
                searchValue: this.searchInput,
                nickname:'登录',
                avatar:'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
                isLogin:false
            };
        },
        created(){
            // console.log("header");
            if(! this.$globalData.userInfo.nickname){
                this.$api.getUserInfo().then(res=>{
                    console.log('Header getUserInfo:',res);
                    if(res.status_code===1){
                        this.nickname=res.data.nickname;
                        this.avatar=res.data.avatar;
                        res.data.signInTime=res.data.signInTime.substring(0,10);
                        this.$globalData.userInfo=res.data;
                        this.isLogin=true;
                    }
                })
            }else {
                this.nickname=this.$globalData.userInfo.nickname;
                this.avatar=this.$globalData.userInfo.avatar;
                this.isLogin=true;
            }
        },
        methods: {
            searchIdle() {
                if ('/search' !== this.$route.path) {
                    this.$router.push({path: '/search', query: {searchValue: this.searchValue}});
                } else {
                    this.$router.replace({path: '/search', query: {searchValue: this.searchValue}});
                    this.$router.go(0);
                }

            },
            toMe() {
                if ('/me' !== this.$route.path) {
                    this.$router.push({path: '/me'});
                }
            },
            toMessage(){
                if ('/message' !== this.$route.path) {
                    this.$router.push({path: '/message'});
                }
            },
            toRelease(){
                if ('/release' !== this.$route.path) {
                    this.$router.push({path: '/release'});
                }
            },
            loginOut(){
                this.$api.logout().then(res=>{
                    if(res.status_code===1){
                        this.$globalData.userInfo={};
                        console.log("login out");
                        if ('/index' === this.$route.path) {
                            this.$router.go(0);
                        }else {
                            this.$router.push({path: '/index'});
                        }
                    }else {
                        this.$message.error('网络或系统异常，退出登录失败！');
                    }
                });

            }
        }
    };
</script>
<style scoped>
/* --- 头部容器整体样式 --- */
    .header {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        height: 60px; /* 稍微增加高度 */
        background: #ffffff; /* 保持纯白背景 */
        display: flex;
        justify-content: center;
        border-bottom: 1px solid #f0f0f0; /* 边框线变细，更精致 */
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 添加一点阴影，增加立体感 */
        z-index: 1000;
    }

    /* --- 内容容器 --- */
    .header-container {
        width: 1000px;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    /* --- Logo和名称 --- */
    .app-name a {
        display: flex;         /* 1. 将<a>标签变成一个flex容器 */
        align-items: center;   /* 2. 让它所有的子元素（图片和文字）在垂直方向上居中对齐！*/
        text-decoration: none;
    }

    .app-logo { 
        width: 38px;  /* 稍微调整一下大小，让它更精致 */
        height: 38px;
        margin-right: 10px; /* 在Logo和文字之间增加一点间距 */
    }

    .app-title {
        color: #FFB800;
        font-size: 22px;
        font-weight: bold;
    }

    /* --- 搜索框 --- */
    .search-container {
        width: 350px;
    }

    /* --- 未登录时的链接 --- */
    .user-name-text {
        font-size: 15px;
        font-weight: 500;
        color: #606266;
        text-decoration: none;
        transition: color 0.2s;
    }
    .user-name-text:hover {
        color: #FFB800;
    }

    /* --- 已登录时的用户信息 --- */
    .el-dropdown {
        display: flex;
        align-items: center;
    }
    /* 你可以保留原来的样式，或者如果想让昵称和头像换个位置，可以参考下面的样式 */
    .el-dropdown .el-avatar {
        margin-left: 8px;
    }

    /* --- 下拉菜单中的“退出登录”文字 --- */
    .el-dropdown-menu__item .logout-text {
        color: #F56C6C; /* 保持红色警告 */
    }
</style>