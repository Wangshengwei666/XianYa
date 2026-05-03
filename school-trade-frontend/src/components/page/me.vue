<template>
    <div>
        <app-head :nickname-value="userInfo.nickname"
                  :avatarValue="userInfo.avatar"></app-head>
        <app-body>
            <div v-show="!eidtAddress">
                <div class="user-info-container">
                    <div class="user-info-details">

                        <el-upload
                            class="avatar-uploader"
                            :action="uploadUrl"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :on-error="handleAvatarError"
                            :before-upload="beforeAvatarUpload"
                            accept="image/jpeg,image/png"
                        >
                            <el-image v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar"></el-image>
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            <div class="avatar-uploader-mask">
                                <i class="el-icon-edit"></i>
                            </div>
                        </el-upload>
                        <div class="user-info-details-text">
                            <div class="user-info-details-text-nickname">{{userInfo.nickname}}</div>
                            <div class="user-info-details-text-time">{{userInfo.signInTime}} 加入平台</div>
                            <div class="user-info-details-text-edit">
                                <el-button type="primary" plain @click="userInfoDialogVisible = true">编辑个人信息</el-button>
                            </div>
                            <el-dialog
                                    @close="finishEdit"
                                    title="编辑个人信息"
                                    :visible.sync="userInfoDialogVisible"
                                    width="400px">
                                <div class="edit-tip">昵称</div>
                                <el-input
                                        v-model="userInfo.nickname"
                                        :disabled="notUserNicknameEdit"
                                        @change="saveUserNickname">
                                    <el-button slot="append" type="warning" icon="el-icon-edit"
                                               @click="notUserNicknameEdit = false">编辑
                                    </el-button>
                                </el-input>

                                <div v-if="userPasswordEdit">
                                    <div class="edit-tip">原密码</div>
                                    <el-input v-model="userPassword1" show-password></el-input>
                                    <div class="edit-tip">新密码</div>
                                    <el-input v-model="userPassword2" show-password></el-input>
                                    <div class="edit-tip">确认新密码</div>
                                    <el-input v-model="userPassword3" show-password></el-input>
                                    <div class="edit-tip"></div>
                                    <el-button @click="savePassword" plain>确认修改</el-button>
                                </div>
                                <div v-else>
                                    <div class="edit-tip">密码</div>
                                    <el-input
                                            value="123456"
                                            :disabled="true"
                                            show-password>
                                        <el-button slot="append" type="warning" icon="el-icon-edit"
                                                   @click="userPasswordEdit = true">编辑
                                        </el-button>
                                    </el-input>
                                </div>
                                <span slot="footer" class="dialog-footer">
                                <el-button @click="userInfoDialogVisible=false">完成</el-button>
                            </span>
                            </el-dialog>
                        </div>
                    </div>
                    <div class="user-info-splace">
                        <el-button type="primary" plain @click="eidtAddress=true">编辑收货地址</el-button>
                    </div>
                </div>
                <div class="idle-container">
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <el-tab-pane label="我发布的" name="1"></el-tab-pane>
                        <el-tab-pane label="我下架的" name="2"></el-tab-pane>
                        <el-tab-pane label="我收藏的" name="3"></el-tab-pane>
                        <el-tab-pane label="我卖出的" name="4"></el-tab-pane>
                        <el-tab-pane label="我买到的" name="5"></el-tab-pane>
                    </el-tabs>
                    <div class="idle-container-list">
                        <div v-for="(item,index) in dataList[activeName-1]" class="idle-container-list-item">
                            <div class="idle-container-list-item-detile" @click="toDetails(activeName,item)">
                                <el-image
                                        style="width: 100px; height: 100px;"
                                        :src="item.imgUrl"
                                        fit="cover">
                                    <div slot="error" class="image-slot">
                                        <i class="el-icon-picture-outline">无图</i>
                                    </div>
                                </el-image>
                                <div class="idle-container-list-item-text">
                                    <div class="idle-container-list-title">
                                        {{item.idleName}}
                                    </div>
                                    <div class="idle-container-list-idle-details" v-html="item.idleDetails">
                                        {{item.idleDetails}}
                                    </div>
                                    <div class="idle-container-list-idle-time">{{item.timeStr}}</div>

                                    <div class="idle-item-foot">
                                        <div class="idle-prive">￥{{item.idlePrice}}
                                            {{(activeName==='4'||activeName==='5')?orderStatus[item.orderStatus]:''}}
                                        </div>
                                        <el-button v-if="activeName!=='4'&&activeName!=='5'" type="danger" size="mini" slot="reference"
                                                   plain @click.stop="handle(activeName,item,index)">{{handleName[activeName-1]}}
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-show="eidtAddress" class="address-container">
                <el-page-header class="address-container-back" @back="eidtAddress=false"
                                content="收货地址"></el-page-header>
                <div class="address-container-add">
                    <div class="address-container-add-title">新增收货地址</div>
                    <div class="address-container-add-item">
                        <el-input placeholder="请输入收货人姓名" v-model="addressInfo.consigneeName" maxlength="10"
                                  show-word-limit>
                            <div slot="prepend">收货人姓名</div>
                        </el-input>
                    </div>
                    <div class="address-container-add-item">
                        <el-input placeholder="请输入收货人手机号" v-model="addressInfo.consigneePhone"
                                  onkeyup="this.value=this.value.replace(/[^\d.]/g,'');" maxlength="11" show-word-limit>
                            <div slot="prepend">手机号</div>
                        </el-input>
                    </div>

                    <div class="address-container-add-item">
                        <span class="demonstration">学校/校区/楼栋</span>
                        <el-cascader
                                :options="options"
                                v-model="selectedOptions"
                                @change="handleAddressChange"
                                :separator="' '"
                        >
                        </el-cascader>
                    </div>
                    <div class="address-container-add-item">
                        <el-input placeholder="请输入详细地址（如道路、门牌号、小区、楼栋号等信息）" v-model="addressInfo.detailAddress"
                                  maxlength="50" show-word-limit>
                            <div slot="prepend">详细地址</div>
                        </el-input>
                    </div>
                    <el-checkbox v-model="addressInfo.defaultFlag">设置为默认地址</el-checkbox>
                    <el-button style="margin-left: 20px;" @click="saveAddress">保存</el-button>
                </div>
                <div class="address-container-list">
                    <div style="color: #FFB800;font-size: 15px;padding-left: 10px;">已有收货地址</div>
                    <el-table
                            stripe
                            :data="addressData"
                            style="width: 100%">
                        <el-table-column
                                prop="consigneeName"
                                label="收货人姓名"
                                width="100">
                        </el-table-column>
                        <el-table-column
                                prop="consigneePhone"
                                label="手机号"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="detailAddressText"
                                label="地址"
                                width="270">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                        size="mini"
                                        @click="handleEdit(scope.$index, scope.row)">编辑
                                </el-button>
                                <el-button
                                        size="mini"
                                        type="danger"
                                        @click="handleDelete(scope.$index, scope.row)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="是否默认地址" width="110">
                            <template slot-scope="scope">
                                <el-button v-if="!scope.row.defaultFlag"
                                           size="mini"
                                           @click="handleSetDefault(scope.$index, scope.row)">设为默认
                                </el-button>
                                <div v-else style="padding-left: 10px;color: #FFB800;">{{scope.row.defaultAddress}}
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
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
    import options from '../common/country-data.js'

    export default {
        name: "me",
        components: {
            AppHead,
            AppBody,
            AppFoot
        },
        data() {
            return {
                uploadUrl: this.$api.getBaseUrl() + '/file',
                addressInfo: {
                    consigneeName: '',
                    consigneePhone: '',
                    provinceName: '',
                    cityName: '',
                    regionName: '',
                    detailAddress: '',
                    defaultFlag: false
                },
                activeName: '1',
                handleName: ['下架', '删除', '取消收藏', '', ''],
                dataList: [
                    [],
                    [],
                    [],
                    [],
                    [],
                    [],
                    [],
                    [],
                ],
                orderStatus: ['待付款', '待发货', '待收货', '已完成', '已取消'],
                userInfoDialogVisible: false,
                notUserNicknameEdit: true,
                userPasswordEdit: false,
                userPassword1: '',
                userPassword2: '',
                userPassword3: '',
                eidtAddress: false,
                selectedOptions: [],//存放默认值
                options: options,   //存放城市数据,
                userInfo: {
                    accountNumber: "",
                    avatar: "",
                    nickname: "",
                    signInTime: "",
                },
                addressData: []
            };
        },
        created() {
            if (!this.$globalData.userInfo.nickname) {
                this.$api.getUserInfo().then(res => {
                    if (res.status_code === 1) {
                        res.data.signInTime = res.data.signInTime.substring(0, 10);
                        console.log(res.data);
                        this.$globalData.userInfo = res.data;
                        this.userInfo = this.$globalData.userInfo;
                    }
                })
            } else {
                this.userInfo = this.$globalData.userInfo;
                console.log(this.userInfo);
            }
            this.getAddressData();
            this.getIdleItemData();
            this.getMyOrder();
            this.getMySoldIdle();
            this.getMyFavorite();
        },
        methods: {
            handleAvatarSuccess(res, file) {
                // 上传成功后的回调
                if (res.status_code === 1) {
                    this.$message.success('头像更换成功!');
                    const newAvatarUrl = res.data;
                    this.userInfo.avatar = newAvatarUrl; // 更新当前页面的头像
                    this.$globalData.userInfo.avatar = newAvatarUrl; // 更新全局头像
                } else {
                    this.$message.error(res.msg || '头像上传失败，请重试');
                }
            },
            handleAvatarError(err, file, fileList) {
                // 上传失败后的回调
                this.$message.error('网络错误，头像上传失败');
            },
            beforeAvatarUpload(file) {
                // 上传前的校验
                const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPGorPNG) {
                    this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isJPGorPNG && isLt2M;
            },
            getMyFavorite(){
                this.$api.getMyFavorite().then(res=>{
                    console.log('getMyFavorite',res);
                    if (res.status_code === 1){
                        for (let i = 0; i < res.data.length; i++) {
                            let pictureList = JSON.parse(res.data[i].idleItem.pictureList);
                            this.dataList[2].push({
                                favoriteId:res.data[i].id,
                                id:res.data[i].idleItem.id,
                                imgUrl:pictureList.length > 0 ? pictureList[0] : '',
                                idleName:res.data[i].idleItem.idleName,
                                idleDetails:res.data[i].idleItem.idleDetails,
                                timeStr:res.data[i].createTime.substring(0, 10) + " " + res.data[i].createTime.substring(11, 19),
                                idlePrice:res.data[i].idleItem.idlePrice
                            });
                        }
                    }
                })
            },
            getMySoldIdle(){
                this.$api.getMySoldIdle().then(res=>{
                    if (res.status_code === 1){
                        console.log('getMySoldIdle',res.data);
                        for (let i = 0; i < res.data.length; i++) {
                            let pictureList = JSON.parse(res.data[i].idleItem.pictureList);
                            this.dataList[3].push({
                                id:res.data[i].id,
                                imgUrl:pictureList.length > 0 ? pictureList[0] : '',
                                idleName:res.data[i].idleItem.idleName,
                                idleDetails:res.data[i].idleItem.idleDetails,
                                timeStr:res.data[i].createTime.substring(0, 10) + " " + res.data[i].createTime.substring(11, 19),
                                idlePrice:res.data[i].orderPrice,
                                orderStatus:res.data[i].orderStatus
                            });
                        }
                    }
                })
            },
            getMyOrder(){
                this.$api.getMyOrder().then(res=>{
                    if (res.status_code === 1){
                        console.log('getMyOrder',res.data);
                        for (let i = 0; i < res.data.length; i++) {
                            let pictureList = JSON.parse(res.data[i].idleItem.pictureList);
                            this.dataList[4].push({
                                id:res.data[i].id,
                                imgUrl:pictureList.length > 0 ? pictureList[0] : '',
                                idleName:res.data[i].idleItem.idleName,
                                idleDetails:res.data[i].idleItem.idleDetails,
                                timeStr:res.data[i].createTime.substring(0, 10) + " " + res.data[i].createTime.substring(11, 19),
                                idlePrice:res.data[i].orderPrice,
                                orderStatus:res.data[i].orderStatus
                            });
                        }
                    }
                })
            },
            getIdleItemData() {
                this.$api.getAllIdleItem().then(res => {
                    console.log(res);
                    if (res.status_code === 1) {
                        for (let i = 0; i < res.data.length; i++) {
                            res.data[i].timeStr = res.data[i].releaseTime.substring(0, 10) + " " + res.data[i].releaseTime.substring(11, 19);
                            let pictureList = JSON.parse(res.data[i].pictureList);
                            res.data[i].imgUrl = pictureList.length > 0 ? pictureList[0] : '';
                            if (res.data[i].idleStatus === 1) {
                                this.dataList[0].push(res.data[i]);
                            } else if (res.data[i].idleStatus === 2) {
                                this.dataList[1].push(res.data[i]);
                            }
                        }
                    }
                })
            },
            getAddressData() {
                this.$api.getAddress().then(res => {
                    if (res.status_code === 1) {
                        let data = res.data;
                        for (let i = 0; i < data.length; i++) {
                            data[i].detailAddressText = data[i].provinceName + data[i].cityName + data[i].regionName + data[i].detailAddress;
                            data[i].defaultAddress = data[i].defaultFlag ? '默认地址' : '设为默认';
                        }
                        console.log(data);
                        this.addressData = data;
                    }
                })
            },
            handleClick(tab, event) {
                // console.log(tab, event);
                console.log(this.activeName);
            },
            saveUserNickname() {
                this.notUserNicknameEdit = true;
                this.$api.updateUserPublicInfo({
                    nickname: this.userInfo.nickname
                }).then(res => {
                    console.log(res);
                    this.$globalData.userInfo.nickname = this.userInfo.nickname;
                })

            },
            savePassword() {
                if (!this.userPassword1 || !this.userPassword2) {
                    this.$message.error('密码为空！');
                } else if (this.userPassword2 === this.userPassword3) {
                    this.$api.updatePassword({
                        oldPassword: this.userPassword1,
                        newPassword: this.userPassword2
                    }).then(res => {
                        if (res.status_code === 1) {
                            this.userPasswordEdit = false;
                            this.$message({
                                message: '修改成功！',
                                type: 'success'
                            });
                            this.userPassword1 = '';
                            this.userPassword2 = '';
                            this.userPassword3 = '';
                        } else {
                            this.$message.error('旧密码错误，修改失败！');
                        }
                    })
                } else {
                    this.$message.error('两次输入的密码不一致！');
                }

            },
            finishEdit() {
                this.notUserNicknameEdit = true;
                this.userInfoDialogVisible = false;
                this.userPasswordEdit = false;
            },
            handleAddressChange(value) {
                console.log(value);
                this.addressInfo.provinceName = value[0];
                this.addressInfo.cityName = value[1];
                this.addressInfo.regionName = value[2];
            },
            handleEdit(index, row) {
                console.log(index, row);
                this.addressInfo = JSON.parse(JSON.stringify(row));
                this.selectedOptions = ['', '', ''];
                this.selectedOptions[0] = row.provinceName;
                this.selectedOptions[1] = row.cityName;
                this.selectedOptions[2] = row.regionName;
            },
            handleDelete(index, row) {
                console.log(index, row);
                this.$confirm('是否确定删除该地址?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$api.deleteAddress(row).then(res => {
                        if (res.status_code === 1) {
                            this.$message({
                                message: '删除成功！',
                                type: 'success'
                            });
                            this.addressData.splice(index, 1);
                            if (row.defaultFlag && this.addressData.length > 0) {
                                this.addressData[0].defaultFlag = true;
                                this.addressData[0].defaultAddress = '默认地址';
                                this.update({
                                    id: this.addressData[0].id,
                                    defaultFlag: true
                                });
                            }
                        } else {
                            this.$message.error('系统异常，删除失败！')
                        }
                    }).catch(() => {
                        this.$message.error('网络异常！')
                    });
                }).catch(() => {
                });

            },
            handleSetDefault(index, row) {
                console.log(index, row);
                row.defaultFlag = true;
                this.update(row);
            },
            toDetails(activeName, item) {
                if (activeName === '4'||activeName === '5') {
                    this.$router.push({path: '/order', query: {id: item.id}});
                } else {
                    this.$router.push({path: '/details', query: {id: item.id}});
                }
            },
            handle(activeName,item,index) {
                console.log(activeName,item,index);
                this.$confirm('是否确认？', '提示', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if(activeName==='1'){
                        this.$api.updateIdleItem({
                            id:item.id,
                            idleStatus:2
                        }).then(res=>{
                            console.log(res);
                            if(res.status_code===1){
                                this.dataList[0].splice(index,1);
                                item.idleStatus=2;
                                this.dataList[1].unshift(item);
                            }else {
                                this.$message.error(res.msg)
                            }
                        });
                    }else if(activeName==='2'){
                        this.$api.updateIdleItem({
                            id:item.id,
                            idleStatus:0
                        }).then(res=>{
                            console.log(res);
                            if(res.status_code===1){
                                this.dataList[1].splice(index,1);
                            }else {
                                this.$message.error(res.msg)
                            }
                        });
                    }else if(activeName==='3'){
                        this.$api.deleteFavorite({
                            id: item.favoriteId
                        }).then(res=>{
                            console.log(res);
                            if(res.status_code===1){
                                this.$message({
                                    message: '已取消收藏！',
                                    type: 'success'
                                });
                                this.dataList[2].splice(index,1);
                            }else {
                                this.$message.error(res.msg)
                            }
                        }).catch(e=>{
                        })
                    }
                }).catch(() => {
                });

            },
            fileHandleSuccess(response, file, fileList) {
                // 调用新的、更健壮的成功处理函数
                this.handleAvatarSuccess(response, file);
                // 清空el-upload的内部文件列表，防止多次上传UI问题
                if(fileList.length > 1){
                fileList.splice(0, 1);
                }
            },
            update(data) {
                this.$api.updateAddress(data).then(res => {
                    if (res.status_code === 1) {
                        this.getAddressData();
                        this.$message({
                            message: '修改成功！',
                            type: 'success'
                        });
                    } else {
                        this.$message.error('系统异常，修改失败！')
                    }
                }).catch(() => {
                    this.$message.error('网络异常！')
                })
            },
            saveAddress() {
                if (this.addressInfo.id) {
                    console.log('update:', this.addressInfo);
                    this.update(this.addressInfo);
                    this.addressInfo = {
                        consigneeName: '',
                        consigneePhone: '',
                        provinceName: '',
                        cityName: '',
                        regionName: '',
                        detailAddress: '',
                        defaultFlag: false
                    };
                    this.selectedOptions = [];
                } else {
                    if (this.addressData.length >= 5) {
                        this.$message.error('已达到最大地址数量！')
                    } else {
                        console.log(this.addressInfo);
                        this.$api.addAddress(this.addressInfo).then(res => {
                            if (res.status_code === 1) {
                                this.getAddressData();
                                this.$message({
                                    message: '新增成功！',
                                    type: 'success'
                                });
                                this.selectedOptions = [];
                                this.addressInfo = {
                                    consigneeName: '',
                                    consigneePhone: '',
                                    provinceName: '',
                                    cityName: '',
                                    regionName: '',
                                    detailAddress: '',
                                    defaultFlag: false
                                };
                            } else {
                                this.$message.error('系统异常，新增失败！')
                            }
                        }).catch(e => {
                            this.$message.error('网络异常！')
                        })
                    }
                }
            }
        }
    }
</script>

<style scoped>

    .user-info-details {
        display: flex;
        height: 140px;
        align-items: center;
        margin: 20px 40px;
    }

    .user-info-details-text {
        margin-left: 20px;
    }

    .user-info-details-text-nickname {
        font-size: 26px;
        font-weight: 600;
        margin: 10px 0;
    }

    .user-info-details-text-time {
        font-size: 14px;
        margin-bottom: 10px;
    }

    .user-info-splace {
        margin-right: 90px;
    }

    .idle-container {
        padding: 0 20px;
    }

    .idle-container-list {
        min-height: 55vh;
    }

    .idle-container-list-item {
        border-bottom: 1px solid #eeeeee;
        cursor: pointer;
    }

    .idle-container-list-item:last-child {
        border-bottom: none;
    }

    .idle-container-list-item-detile {
        height: 120px;
        display: flex;
        align-items: center;
    }

    .idle-container-list-item-text {
        margin-left: 10px;
        height: 100px;
        max-width: 800px;
    }

    .idle-container-list-title {
        font-weight: 600;
        font-size: 18px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }

    .idle-container-list-idle-details {
        font-size: 14px;
        color: #555555;
        padding-top: 5px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }

    .idle-container-list-idle-time {
        font-size: 13px;
        padding-top: 5px;
    }

    .idle-prive {
        font-size: 15px;
        padding-top: 5px;
        color: #FF5722;
    }

    .edit-tip {
        font-size: 14px;
        margin: 10px 5px;
    }

    .address-container {
        padding: 10px 20px;
    }

    .address-container-back {
        margin-bottom: 10px;
    }

    .address-container-add-title {
        font-size: 15px;
        color: #FFB800;
        padding: 10px;
    }

    .address-container-add-item {
        margin-bottom: 20px;
    }

    .demonstration {
        color: #666666;
        font-size: 14px;
        padding: 10px;
    }

    .address-container-add {
        padding: 0 200px;
    }

    .address-container-list {
        padding: 30px 100px;
    }

    .idle-item-foot {
        width: 800px;
        display: flex;
        justify-content: space-between;
    }

    /* --- 整体用户信息容器 --- */
        .user-info-container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #fafafa;
        border-radius: 12px;
        margin: 20px;
        padding: 20px 40px; /* 调整内边距 */
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); /* 添加柔和阴影 */
    }

    /* --- 左侧信息详情 --- */
    .user-info-details {
        gap: 25px; /* 替换margin-left，增加元素间距 */
    }

    /* --- 头像上传器样式 --- */
    .avatar-uploader >>> .el-upload { /* 使用>>>穿透scoped限制 */
        width: 120px;
        height: 120px;
        border-radius: 50%;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        border: 3px solid #fff;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        transition: all 0.3s ease;
    }
    .avatar-uploader >>> .el-upload:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .avatar {
        width: 120px;
        height: 120px;
        display: block;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 120px;
        height: 120px;
        line-height: 120px;
        text-align: center;
    }
    .avatar-uploader-mask {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.4);
        color: white;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 24px;
        opacity: 0;
        transition: opacity 0.3s ease;
        pointer-events: none; /* 让遮罩不影响点击 */
    }
    .avatar-uploader:hover .avatar-uploader-mask {
        opacity: 1;
    }

</style>
