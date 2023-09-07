#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <BUAdSDK/BUAdSDK.h>

@interface CsjSdk : RCTEventEmitter <RCTBridgeModule, BUMSplashAdDelegate>

@end
