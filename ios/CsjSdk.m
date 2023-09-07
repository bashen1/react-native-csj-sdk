#import "CsjSdk.h"

@implementation CsjSdk {
    BUSplashAd *splashAd;
}

RCT_EXPORT_MODULE()

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (NSArray<NSString *> *)supportedEvents {
    return @[
        @"SplashAd-onAdClick",
        @"SplashAd-onAdClose",
        @"SplashAd-onAdShow",
        @"SplashAd-onAdLoadFail",
        @"SplashAd-onAdLoadSuccess"
    ];
}

RCT_EXPORT_METHOD(init:(NSDictionary *)param resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString *appId = @"";

    if ((NSString *)param[@"appId"] != nil) {
        appId = (NSString *)param[@"appId"];
    }

    if (![appId isEqual:@""]) {
        BUAdSDKConfiguration *configuration = [BUAdSDKConfiguration configuration];
        // 设置APPID
        configuration.appID = appId;
        // 是否使用聚合
        configuration.useMediation = YES;

        // 初始化
        [BUAdSDKManager startWithAsyncCompletionHandler:^(BOOL success, NSError *error) {
            if (success) {
                // 处理成功之后的逻辑
                NSDictionary *ret = @{ @"code": @"1", @"message": @"" };
                resolve(ret);
            } else {
                NSDictionary *ret = @{ @"code": @"-1", @"message": [NSString stringWithFormat:@"%@", error] };
                resolve(ret);
            }
        }];
    } else {
        NSDictionary *ret = @{
                @"code": @"0", @"message": @"appId为空"
        };
        resolve(ret);
    }
}

RCT_EXPORT_METHOD(loadSplashAd:(NSDictionary *)param resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString *codeId = @"";

    if ((NSString *)param[@"codeId"] != nil) {
        codeId = (NSString *)param[@"codeId"];
    }

    if (![codeId isEqual:@""]) {
        BUAdSlot *slot = [[BUAdSlot alloc] init];
        slot.ID = codeId;
        BUSplashAd *splashAd = [[BUSplashAd alloc] initWithSlot:slot adSize:CGSizeZero];
        splashAd.delegate = self;
        self->splashAd = splashAd;

        [self->splashAd loadAdData];
        NSDictionary *ret = @{
                @"code": @"1", @"message": @""
        };
        resolve(ret);
    } else {
        NSDictionary *ret = @{
                @"code": @"0", @"message": @"codeId为空"
        };
        resolve(ret);
    }
}

#pragma mark - BUMSplashAdDelegate

- (void)splashAdDidClick:(nonnull BUSplashAd *)splashAd {
    [self sendEventWithName:@"SplashAd-onAdClick" body:@{ @"message": @"" }];
}

- (void)splashAdDidClose:(nonnull BUSplashAd *)splashAd closeType:(BUSplashAdCloseType)closeType {
    // 按照实际情况决定是否销毁广告对象
    [splashAd.mediation destoryAd];
    [self sendEventWithName:@"SplashAd-onAdClose" body:@{ @"message": @"" }];
}

- (void)splashAdDidShow:(nonnull BUSplashAd *)splashAd {
    [self sendEventWithName:@"SplashAd-onAdShow" body:@{ @"message": @"" }];
}

- (void)splashAdLoadFail:(nonnull BUSplashAd *)splashAd error:(BUAdError *_Nullable)error {
    [self sendEventWithName:@"SplashAd-onAdLoadFail" body:@{ @"message": [NSString stringWithFormat:@"%@", error] }];
}

- (void)splashAdLoadSuccess:(nonnull BUSplashAd *)splashAd {
    // 使用应用keyWindow的rootViewController（接入简单，推荐）
    [splashAd showSplashViewInRootViewController:[UIApplication sharedApplication].keyWindow.rootViewController];
    [self sendEventWithName:@"SplashAd-onAdLoadSuccess" body:@{ @"message": @"" }];
}

- (void)splashAdRenderFail:(nonnull BUSplashAd *)splashAd error:(BUAdError *_Nullable)error {
}

- (void)splashAdRenderSuccess:(nonnull BUSplashAd *)splashAd {
}

- (void)splashAdViewControllerDidClose:(nonnull BUSplashAd *)splashAd {
}

- (void)splashAdWillShow:(nonnull BUSplashAd *)splashAd {
}

- (void)splashDidCloseOtherController:(nonnull BUSplashAd *)splashAd interactionType:(BUInteractionType)interactionType {
}

- (void)splashVideoAdDidPlayFinish:(nonnull BUSplashAd *)splashAd didFailWithError:(nonnull NSError *)error {
}

@end
