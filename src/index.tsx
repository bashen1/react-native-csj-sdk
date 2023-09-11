import {EmitterSubscription, NativeEventEmitter, NativeModules} from 'react-native';
const {CsjSdk} = NativeModules;

type EventListener = (event: any) => void;

export type EventName =
  | 'SplashAd-onAdClick'
  | 'SplashAd-onAdClose'
  | 'SplashAd-onAdShow'
  | 'SplashAd-onAdLoadFail'
  | 'SplashAd-onAdLoadSuccess'
  | 'SplashAd-onRenderFail';

const eventEmitter = new NativeEventEmitter(CsjSdk);

class CsjAd {
  // 初始化
  static init = async(params:{
    appId: string
  }): Promise<{code: string, message: string}> => {
    return await CsjSdk.init(params);
  }

  // 加载启动屏广告
  static loadSplashAd = async(params:{
    codeId: string
  }): Promise<{code: string, message: string}> => {
    return await CsjSdk.loadSplashAd(params);
  }

  static addListener = (eventType: EventName, listener: EventListener): EmitterSubscription => {
    return eventEmitter.addListener(eventType, listener);
  }
}




export default CsjAd
