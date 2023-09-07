require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-csj-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/bashen1/react-native-csj-sdk.git.git", :tag => "#{s.version}" }


  s.source_files = "ios/**/*.{h,m,mm,swift}"


  s.dependency "React-Core"

  s.subspec 'Ads-Fusion-CN-Beta' do |a|
    a.dependency "Ads-Fusion-CN-Beta/BUAdSDK", '5.3.6.1'
    a.dependency "Ads-Fusion-CN-Beta/CSJMediation", '5.3.6.1'
  end
  # s.dependency 'BUAdTestMeasurement','5.3.6.1' # 上线前禁止带到线上

  # s.dependency 'CSJMAdmobAdapter', '10.0.0.0'
  # s.dependency 'CSJMBaiduAdapter', '5.300.0'
  # s.dependency 'CSJMGdtAdapter', '4.14.30.0'
  # s.dependency 'CSJMKlevinAdapter', '2.11.0.211.1'
  # s.dependency 'CSJMKsAdapter', '3.3.47.0'
  # s.dependency 'CSJMMintegralAdapter', '7.3.6.0.2'
  # s.dependency 'CSJMSigmobAdapter', '4.8.0.0'
  # s.dependency 'CSJMUnityAdapter', '4.3.0.0'
end
