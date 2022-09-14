# react-native-xplaneconnect

React Native X-Plane Connect (Android)
A react native library to connect to NASA's X-Plane Connect plugin
https://github.com/nasa/XPlaneConnect

**iOS is not supported at the moment**

## Installation

```sh
npm install react-native-xplaneconnect
```

## Usage

```js
import {getDREF, sendDREF} from 'react-native-xplaneconnect';

// get dataref
const result = await getDREF('sim/cockpit2/clock_timer/local_time_seconds');

// result =>
// { isConnected: boolean, message: string, value: string }
```

```
// set dataref
sendDREF('sim/cockpit/autopilot/autopilot_mode', 2);
```

## Running the Example project

```
yarn example android
```

## Limitations

The library can only set/get float values. 

At the moment only `getDREF` and `setDREF` functions are implemented.

The full list for all other functions X-Plane Connect supports natively:
https://github.com/nasa/XPlaneConnect/wiki/XPC-Client-Reference

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
