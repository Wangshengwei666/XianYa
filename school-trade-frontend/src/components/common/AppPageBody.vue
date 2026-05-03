<template>
  <div class="main-container">
    <div class="main-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "PageBody"
}
</script>

<style scoped>
.main-container {
  /* 
   * 使用padding，为内容区创造一个“呼吸空间”。
   */
  padding-top: 80px; /* 60px的header高度 + 20px的间距 */
  padding-bottom: 20px;
  display: flex;
  justify-content: center;
}

.main-content {
  width: 1000px;
  background-color: #ffffff; /* 保持内容区为纯白色 */
  min-height: 85vh; /* 稍微调整高度 */
  
  border-radius: 12px; /*! 为内容区添加柔和的圆角 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); /*! 添加非常淡的阴影，营造悬浮感 */
  padding: 20px; /*! 为内容区内部也增加一些内边距，避免内容紧贴边缘 */
  box-sizing: border-box; /*! 确保padding不会撑大盒子的总宽度 */
}
</style>