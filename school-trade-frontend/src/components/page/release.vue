<template>
    <div>
        <app-head></app-head>
        <app-body>
            <div class="release-idle-container">
                <div class="release-idle-container-title">物品发布</div>
                <div class="release-idle-container-form">
                    <el-input placeholder="请输入二手物品名称" v-model="idleItemInfo.idleName"
                              maxlength="30"
                              show-word-limit>
                    </el-input>
                    <div class="ai-button-container">
                    <el-button
                        type="warning"
                        plain
                        size="small"
                        icon="el-icon-magic-stick"
                        @click="handleAIGenerate"
                        :loading="aiLoading">
                        {{ aiLoading ? 'AI生成中...' : 'AI 帮你写描述' }}
                    </el-button>
                    </div>
                    <el-input
                            class="release-idle-detiles-text"
                            type="textarea"
                            autosize
                            placeholder="请输入物品的详细介绍..."
                            v-model="idleItemInfo.idleDetails"
                            maxlength="1000"
                            show-word-limit>
                    </el-input>
                    <div class="release-idle-place">
                        <div class="release-tip">您的地区</div>
                        <el-cascader
                                :options="options"
                                v-model="selectedOptions"
                                @change="handleChange"
                                :separator="' '"
                                style="width: 90%;"
                        >
                        </el-cascader>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <div>
                            <div class="release-tip">物品类别</div>
                            <el-select  v-model="idleItemInfo.idleLabel" placeholder="请选择类别">
                                <el-option
                                        v-for="item in options2"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                        <div style="width: 300px;">
                            <el-input-number v-model="idleItemInfo.idlePrice" :precision="2" :step="10" :max="10000000">
                                <div slot="prepend">价格</div>
                            </el-input-number>
                        </div>

                    </div>
                    <div class="release-idle-container-picture">
                        <div class="release-idle-container-picture-title">上传二手物品照片</div>
                        <el-upload
                                :action="uploadUrl"
                                :on-preview="fileHandlePreview"
                                :on-remove="fileHandleRemove"
                                :on-success="fileHandleSuccess"
                                :show-file-list="showFileList"
                                :limit="10"
                                :on-exceed="handleExceed"
                                accept="image/*"
                                drag
                                multiple>
                            <i class="el-icon-upload"></i>
                            <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
                        </el-upload>
                        <div class="picture-list">
                            <el-image style="width: 600px;height:400px; margin-bottom: 2px;" fit="contain"
                                      v-for="(img,index) in imgList" :src="img"
                                      :preview-src-list="imgList"></el-image>
                        </div>
                        <el-dialog :visible.sync="imgDialogVisible">
                            <img width="100%" :src="dialogImageUrl" alt="">
                        </el-dialog>
                    </div>
                    <div style="display: flex;justify-content: center;margin-top: 30px;margin-bottom: 30px;">
                        <el-button type="primary" plain @click="releaseButton">确认发布</el-button>
                    </div>
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
        name: "release",
        components: {
            AppHead,
            AppBody,
            AppFoot
        },
        data() {
            return {
                aiLoading: false, // 用于控制AI按钮的加载状态
                uploadUrl: this.$api.getBaseUrl() + '/file',
                imgDialogVisible:false,
                dialogImageUrl:'',
                showFileList:true,
                options:options,
                selectedOptions:[],
                options2: [{
                    value: 1,
                    label: '数码'
                }, {
                    value: 2,
                    label: '家电'
                }, {
                    value: 3,
                    label: '户外'
                }, {
                    value: 4,
                    label: '图书'
                }, {
                    value: 5,
                    label: '其他'
                }],
                imgList:[],
                idleItemInfo:{
                    idleName:'',
                    idleDetails:'',
                    pictureList:'',
                    idlePrice:0,
                    idlePlace:'',
                    idleLabel:''
                }
            };
        },
        methods: {
            handleAIGenerate() {
                // 1. 检查标题是否已填写
                if (!this.idleItemInfo.idleName) {
                    this.$message.warning('请先输入物品的名称，AI才能帮你哦！');
                    return;
                }
                
                // 2. 进入加载状态
                this.aiLoading = true;

                // 3. 调用后端的AI接口
                this.$api.generateDescription({
                    title: this.idleItemInfo.idleName
                }).then(res => {
                    if (res.status_code === 1) {
                        // 4. 成功后，将AI返回的描述填充到文本框
                        this.$message.success('AI描述生成成功！');
                        this.idleItemInfo.idleDetails = res.data;
                    } else {
                        this.$message.error(res.msg || 'AI服务出错，请稍后再试');
                    }
                }).catch(err => {
                    this.$message.error('网络请求失败，请检查网络连接');
                }).finally(() => {
                    // 5. 无论成功失败，都结束加载状态
                    this.aiLoading = false;
                });
            },
            handleChange(value) {
                console.log(value);
                this.idleItemInfo.idlePlace=value[1];
            },
            fileHandleRemove(file, fileList) {
                console.log(file, fileList);
                for(let i=0;i<this.imgList.length;i++){
                    if(this.imgList[i]===file.response.data){
                        this.imgList.splice(i,1);
                    }
                }
            },
            fileHandlePreview(file) {
                console.log(file);
                this.dialogImageUrl=file.response.data;
                this.imgDialogVisible=true;
            },
            fileHandleSuccess(response, file, fileList) {
                // 检查后端的响应是否成功
                if (response && response.status_code === 1) {
                    console.log("图片上传成功，返回的完整URL是:", response.data);
                    // 【核心】直接将后端返回的完整URL存入imgList
                    this.imgList.push(response.data); 
                } else {
                    // 如果后端返回了错误信息，就弹出一个提示
                    this.$message.error('图片上传失败: ' + (response.msg || '未知错误'));
                }
            },
            releaseButton(){
                this.idleItemInfo.pictureList=JSON.stringify(this.imgList);
                console.log(this.idleItemInfo);
                if(this.idleItemInfo.idleName&&
                    this.idleItemInfo.idleDetails&&
                    this.idleItemInfo.idlePlace&&
                    this.idleItemInfo.idleLabel&&
                    this.idleItemInfo.idlePrice){
                    this.$api.addIdleItem(this.idleItemInfo).then(res=>{
                        if (res.status_code === 1) {
                            this.$message({
                                message: '发布成功！',
                                type: 'success'
                            });
                            console.log(res.data);
                            this.$router.replace({path: '/details', query: {id: res.data.id}});
                        } else {
                            this.$message.error('发布失败！'+res.msg);
                        }
                    }).catch(e=>{
                        this.$message.error('请填写完整信息');
                    })
                }else {
                    this.$message.error('请填写完整信息！');
                }

            },
            handleExceed(files, fileList) {
                this.$message.warning(`限制10张图片，本次选择了 ${files.length} 张图，共选择了 ${files.length + fileList.length} 张图`);
            },
        }
    }
</script>

<style scoped>
    .release-idle-container {
        min-height: 85vh;
    }

    .release-idle-container-title {
        font-size: 18px;
        padding: 30px 0;
        font-weight: 600;
        width: 100%;
        text-align: center;
    }

    .release-idle-container-form {
        padding: 0 180px;
    }

    .release-idle-place{
        margin-bottom: 15px;
    }
    .release-tip{
        color: #555555;
        float: left;
        padding-right: 5px;
        height: 36px;
        line-height: 36px;
        font-size: 14px;
    }
    .release-idle-container-picture{
        width: 500px;
        height: 400px;
        margin: 20px 0;

    }
    .release-idle-container-picture-title{
        margin: 10px 0;
        color: #555555;
        font-size: 14px;
    }
    .picture-list {
        margin: 20px 0;
        display: flex;
        flex-direction: column;
        align-items: center;
        height: 100px;
    }
    .ai-button-container {
        display: flex;
        justify-content: flex-end; /* 让按钮靠右对齐 */
        margin-top: -10px; /* 向上移动一点，紧贴在详情输入框下方 */
        margin-bottom: 20px;
        margin-right: 2px;
    }
    .ai-button-container {
        display: flex;
        justify-content: flex-end; /* 保持按钮靠右 */
        margin-bottom: 10px;      /*! 将下边距改为10px */
        margin-top: 8px;          /*! 新增一个8px的上边距 */
    }
    .release-idle-detiles-text {
        margin: 0;
    }

</style>
