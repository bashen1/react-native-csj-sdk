import { NativeModules } from 'react-native';

type CsjSdkType = {
  multiply(a: number, b: number): Promise<number>;
};

const { CsjSdk } = NativeModules;

export default CsjSdk as CsjSdkType;
