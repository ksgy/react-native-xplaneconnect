import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-xplaneconnect' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Xplaneconnect = NativeModules.Xplaneconnect  ? NativeModules.Xplaneconnect  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function connect(host: string, port: number): Promise<string> {
  return Xplaneconnect.connect(host, port);
}
