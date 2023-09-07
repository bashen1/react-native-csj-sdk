# react-native-csj-sdk

[![npm version](https://badge.fury.io/js/react-native-csj-sdk.svg)](https://badge.fury.io/js/react-native-csj-sdk)

穿山甲广告SDK，目前只有开屏广告功能

Android SDK Version: 5.5.1.5

iOS SDK Version: 5.3.6.1

## 开始

`$ npm install react-native-csj-sdk -E`

>在使用前，你需要先[注册穿山甲账号](https://www.csjplatform.com/)并且创建一个应用。

## 插件安装与初始化

### iOS

1. 可能需要配置广告跟踪

```xml
<key>NSUserTrackingUsageDescription</key>
<string>请放心，开启权限不会获取您在其他站点的隐私信息，该权限仅用于标识设备、第三方广告、并保障服务安全与提示浏览体验</string>
```

### Android

1. 在 android 根目录 的 build.gradle 中添加:

```javascript
buildscript {
  repositories {
    ...
    maven {url "https://artifact.bytedance.com/repository/pangle"}
  }
}

allprojects {
  repositories {
    ...
    maven {url "https://artifact.bytedance.com/repository/pangle"}
  }
}

```

## 插件接口文档

```javascript
import CsjSdk from 'react-native-csj-sdk';

// 初始化
let init = await CsjSdk.init({
    appId: 'XXXXX', //穿山甲应用ID
});

// 加载闪屏广告
let ret = await CsjSdk.loadSplashAd({
    codeId: 'XXXX', //广告ID，GroMore
    width: width, //广告宽度，仅Android有效
    height: height, //广告高度，仅Android有效
    timeout: 1500, //获取广告超时，仅Android有效
});

// 添加监听
// SplashAd-onAdClick 广告点击
// SplashAd-onAdClose 广告关闭
// SplashAd-onAdShow 广告显示
// SplashAd-onAdLoadFail 加载广告配置失败
// SplashAd-onAdLoadSuccess 加载广告配置成功
const listener = CsjSdk.addListener('SplashAd-onAdClose', e => {
    console.log('SplashAd-onAdClose');
});

// 移除监听
listener?.remove?.();
```

## License

MIT
