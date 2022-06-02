module.exports = {
  // publicPath: "/poplar",
  pluginOptions: {
    i18n: {
      locale: "zh",
      fallbackLocale: "en",
      localeDir: "locales",
      enableInSFC: true,
    },
  },
  devServer: {
    host: "0.0.0.0", // 允许外部ip访问
    port: 8080, // 端口
    https: false, // 启用https
    proxy: "",
    proxy: {
      "/": {
        //这里的/api表示的意思是以/api开头的才生效 ->刷下面的重点
        // target: "http://10.16.109.50:8081",
        // target: "http://10.16.22.0:8081",
        // target: "http://localhost:8081",
        // target: "http://10.16.79.168:8081",
        // target: "http://10.16.107.2:18081",
        target: "10.16.22.0:8081",
        changOrigin: true, //如果接口跨域这里就要这个参数配置
        pathRewrite: {
          //'^/api': '/api'  //实际请求地址是http://baidu.com/api/news/list
          "^/": "/", //实际请求地址是http://baidu.com/news/list
          // 我的理解就是http://baidu.com替换了/api/news/list里面的/api
        },
      },
      "/permit": {
        // target: "http://10.16.79.168:3004",
        target: "10.16.22.0:3004",
        changOrigin: true, //如果接口跨域这里就要这个参数配置
        pathRewrite: {
          "^/permit": "/permit",
        },
      },
    },
  },
};