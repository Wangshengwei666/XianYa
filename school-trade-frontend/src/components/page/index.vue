<template>
    <div>
        <app-head></app-head>
        <app-body>
            <div style="min-height: 85vh;">
            <el-tabs v-model="labelName" type="card" @tab-click="handleClick">
                <el-tab-pane label="全部" name="0"></el-tab-pane>
                <el-tab-pane label="数码" name="1"></el-tab-pane>
                <el-tab-pane label="家电" name="2"></el-tab-pane>
                <el-tab-pane label="户外" name="3"></el-tab-pane>
                <el-tab-pane label="图书" name="4"></el-tab-pane>
                <el-tab-pane label="其他" name="5"></el-tab-pane>
            </el-tabs>

             <!--<el-menu  mode="horizontal" @select="handleSelect">
                    <el-menu-item >全部</el-menu-item>
                    <el-submenu ><template slot="title">数码</template></el-submenu>
                    <el-submenu><template slot="title">家电</template></el-submenu>
                    <el-submenu><template slot="title">户外</template></el-submenu>
                    <el-submenu><template slot="title">图书</template></el-submenu>
                    <el-submenu><template slot="title">其他</template></el-submenu>

              </el-menu>-->
            <div style="margin: 0 20px;">
                <el-row :gutter="30">
                    <el-col :span="6" v-for="(idle,index) in idleList" :key="idle.id">
                        <div class="idle-card" @click="toDetails(idle)">
                            <el-image
                                    class="card-img"
                                    :src="idle.imgUrl"
                                    fit="cover">
                                <div slot="error" class="image-slot">
                                    <i class="el-icon-picture-outline"></i>
                                </div>
                            </el-image>
                            <div class="card-content">
                                <div>
                                    <div class="idle-title" :title="idle.idleName">
                                        {{idle.idleName}}
                                    </div>
                                </div>
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
        name: "index",
        components: {
            AppHead,
            AppBody,
            AppFoot
        },
        data() {
            return {
                labelName: '0',
                idleList: [],
                currentPage: 1,
                totalItem:1
            };
        },
        created() {
            this.findIdleItem(1)
        },
        watch:{
            $route(to,from){
                this.labelName=to.query.labelName;
                let val=parseInt(to.query.page)?parseInt(to.query.page):1;
                // let totalPage=parseInt(this.totalItem/8)+1;
                // val=parseInt(val%totalPage);
                // val=val===0?totalPage:val;
                this.currentPage=parseInt(to.query.page)?parseInt(to.query.page):1;
                this.findIdleItem(val);
            }
        },
        methods: {
            findIdleItem(page){
                const loading = this.$loading({
                    lock: true,
                    text: '加载数据中',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)'
                });
                if(this.labelName>0){
                    this.$api.findIdleItemByLabel({
                        idleLabel:this.labelName,
                        page: page,
                        nums: 8
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
                        console.log(this.totalItem);
                    }).catch(e => {
                        console.log(e)
                    }).finally(()=>{
                        loading.close();
                    })
                }else{
                    this.$api.findIdleItem({
                        page: page,
                        nums: 8
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
                        console.log(this.totalItem);
                    }).catch(e => {
                        console.log(e)
                    }).finally(()=>{
                        loading.close();
                    })
                }
            },
            handleClick(tab, event) {
                // console.log(tab,event);
                console.log(this.labelName);
                this.$router.replace({query: {page: 1,labelName:this.labelName}});
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.$router.replace({query: {page: val,labelName:this.labelName}});
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
        box-shadow: 0 4px_12px rgba(0, 0, 0, 0.08);
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
    .idle-card .el-image {
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

    .user-info .el-image {
        width: 28px !important;
        height: 28px !important;
        border-radius: 50%;
    }

    .user-nickname {
        color: #606266;
        font-size: 13px;
        margin-left: 8px;
    }

    .fenye {
        display: flex;
        justify-content: center;
        height: 60px;
        align-items: center;
    }

</style>