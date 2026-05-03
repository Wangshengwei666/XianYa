<template>
    <div>
        <app-head :searchInput="searchValue"></app-head>
        <app-body>
            <div style="min-height: 85vh;">
                <div style="margin: 0 20px;padding-top: 20px;">
                    <div style="text-align: center;color: #555555;padding: 20px;" v-if="idleList.length===0">暂无匹配的二手物品</div>
                        <el-row :gutter="30">
                            <el-col :span="6" v-for="(idle,index) in idleList" :key="idle.id">
                                <div class="idle-card" @click="toDetails(idle)">
                                    
                                    <!-- 卡片顶部的图片 -->
                                    <el-image
                                            class="card-img"
                                            :src="idle.imgUrl"
                                            fit="cover">
                                        <div slot="error" class="image-slot">
                                            <i class="el-icon-picture-outline"></i>
                                        </div>
                                    </el-image>
                            
                                    <!-- 卡片下部的内容区域 -->
                                    <div class="card-content">
                                        
                                        <!-- 上半部分：标题 -->
                                        <div>
                                            <div class="idle-title" :title="idle.idleName">
                                                {{idle.idleName}}
                                            </div>
                                        </div>
                            
                                        <!-- 下半部分：价格和发布者 -->
                                        <div class="card-footer">
                                            <div class="user-info">
                                                <el-image
                                                        class="user-avatar"
                                                        :src="idle.user.avatar"
                                                        fit="cover">
                                                    <div slot="error" class="image-slot">
                                                        <i class="el-icon-user-solid"></i>
                                                    </div>
                                                </el-image>
                                                <div class="user-nickname">{{idle.user.nickname}}</div>
                                            </div>
                                            <div class="idle-price">￥{{idle.idlePrice}}</div>
                                        </div>
                            
                                    </div>
                            
                                </div>
                            </el-col>
                        </el-row>
                </div>
                <div class="fenye">
                    <el-pagination
                            background
                            @current-change="handleCurrentChange"
                            :current-page.sync="currentPage"
                            :page-size="8"
                            layout="prev, pager, next, jumper"
                            :total="totalItem">
                    </el-pagination>
                </div>
            </div>
            <app-foot></app-foot>
        </app-body>
    </div>
</template>

<script>
    import AppHead from '../common/AppHeader.vue';
    import AppBody from '../common/AppPageBody.vue'
    import AppFoot from '../common/AppFoot.vue'

    export default {
        name: "search",
        components: {
            AppHead,
            AppBody,
            AppFoot
        },
        data() {
            return {
                idleList: [],
                currentPage: 1,
                searchValue: '',
                totalItem:1
            };
        },
        created() {
            this.findIdleItem(1, this.$route.query.searchValue);
            this.searchValue = this.$route.query.searchValue;
        },
        watch: {
            $route(to, from) {
                this.searchValue = to.query.searchValue;
                this.findIdleItem(to.query.page, to.query.searchValue);
            }
        },
        methods: {
            findIdleItem(page, findValue) {
                this.$api.findIdleItem({
                    page: page,
                    nums: 8,
                    findValue: findValue
                }).then(res => {
                    console.log(res);
                    let list = res.data.list;
                    for (let i = 0; i < list.length; i++) {
                        list[i].timeStr = list[i].releaseTime.substring(0, 10) + " " + list[i].releaseTime.substring(11, 19);
                        let pictureList = JSON.parse(list[i].pictureList);
                        list[i].imgUrl = pictureList.length > 0 ? pictureList[0] : '';
                    }
                    this.idleList = list;
                    this.totalItem=res.data.count;
                }).catch(e => {
                    console.log(e)
                })
            },
            handleClick(tab, event) {
                console.log(tab, event);
                console.log(this.labelName)
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.$router.replace({query: {page: val, searchValue: this.searchValue}});
            },
            toDetails(idle) {
                this.$router.push({path: '/details', query: {id: idle.id}});
            }
        }
    }
</script>

<style scoped>
    /* --- 商品卡片整体样式 --- */
    .idle-card {
        height: 320px;
        border-radius: 12px;
        overflow: hidden;
        background-color: #fff;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease-in-out;
        cursor: pointer;
        margin-bottom: 30px;
        display: flex;
        flex-direction: column;
    }

    /* --- 鼠标悬浮效果 --- */
    .idle-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
    }

    /* 卡片图片区域 */
    .card-img {
        width: 100%;
        height: 180px;
    }

    /* --- 卡片文字内容容器 --- */
    .card-content {
        padding: 15px;
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    /* 商品标题 */
    .idle-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        margin-bottom: 8px;
    }

    /* 商品价格 */
    .idle-price {
        font-size: 18px;
        font-weight: bold;
        color: #FF5722;
    }

    /* --- 底部用户信息和价格的容器 --- */
    .card-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-top: 1px solid #f0f0f0;
        padding-top: 10px;
        margin-top: 10px;
    }

    /* 用户信息 */
    .user-info {
        display: flex;
        align-items: center;
    }

    .user-avatar {
        width: 28px !important;
        height: 28px !important;
        border-radius: 50%;
    }

    .user-nickname {
        color: #606266;
        font-size: 13px;
        margin-left: 8px;
    }

    /* --- 分页器样式 --- */
    .fenye {
        display: flex;
        justify-content: center;
        padding: 40px 0;
    }

</style>