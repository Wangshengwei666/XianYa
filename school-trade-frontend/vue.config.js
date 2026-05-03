const webpack = require('webpack')

module.exports = {
  // 基本路径, './' 表示相对路径
  publicPath: './',
  // 打包时，静态资源（js、css、img、fonts）的存放路径
  assetsDir: 'static',
  // 生产环境是否生成 sourceMap 文件，false可以加速生产环境构建
  productionSourceMap: false,
  // Webpack高级配置
  configureWebpack: {
    plugins: [
      // 全局注入jQuery，一些老的Element-UI或插件可能需要
      new webpack.ProvidePlugin({
        $: "jquery",
        jQuery: "jquery",
        "windows.jQuery": "jquery"
      })
    ]
  },
  // 修改webpack的链式操作，用于更精细的控制
  chainWebpack: config => {
    // 修改网站标题
    config.plugin('html').tap(args => {
      args[0].title = '闲鸭 - SYSU 校园二手平台';
      return args;
    });
  },
  // 开发服务器配置
  devServer: {
    // 端口号，必须与后端不同，我们用8081
    port: 8081,
    // 自动打开浏览器
    open: true,
    // // 配置反向代理，解决跨域问题
    // proxy: {
    //   // 当请求路径以'/api'开头时，触发代理
    //   '/api': {
    //     // 目标服务器地址 (你的后端地址)
    //     target: 'http://localhost:8080',
    //     // 是否改变源地址，必须为true
    //     changeOrigin: true,
    //     // 路径重写，将'/api'从请求路径中去掉
    //     // 例如，前端请求/api/user，代理后会变成 http://localhost:8080/user
    //     pathRewrite: {
    //       '^/api': '' 
    //     }
    //   }
    // }
  }
};