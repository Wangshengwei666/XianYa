<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>


<style>
/* 
 * 全局基础样式
 */
html {
  /* 
   * 强制垂直滚动条始终显示。
   * 为滚动条预留空间，从而防止页面内容在高度变化时产生左右抖动。
   */
  overflow-y: scroll;
}

html, body, #app {
  /* 防止意外的水平滚动 */
  overflow-x: hidden;
  
  /* 设置我们温暖的背景色 */
  background-color: #FFFBF5 !important;
}
</style>