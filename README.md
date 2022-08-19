# react-native-xplaneconnect

React Native X-Plane Connect
A react native library to connect to NASA's X-Plane Connect plugin
https://github.com/nasa/XPlaneConnect

## Installation

```sh
npm install react-native-xplaneconnect
```

## Usage

```js
import {getDREF, sendDREF} from 'react-native-xplaneconnect';

// get dataref
const result = await getDREF('sim/cockpit2/clock_timer/local_time_seconds');

// set dataref
sendDREF('sim/cockpit/autopilot/autopilot_mode', 2);
```

## Running the Example project

```
yarn example android
```

## Limitations

The library can only set/get float values. At the moment not all functions implemented, see the full list here:
https://github.com/nasa/XPlaneConnect/wiki/XPC-Client-Reference

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
