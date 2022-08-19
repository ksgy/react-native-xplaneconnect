import * as React from 'react';

import {StyleSheet, View, Text, Button} from 'react-native';
import {connect, getDREF} from 'react-native-xplaneconnect';

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();

  React.useEffect(() => {
    connect('192.168.1.7', 49009).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Button
        onPress={() => {
          getDREF('sim/cockpit2/clock_timer/local_time_seconds').then(
            setResult
          );
        }}
        title="get local_time_seconds"
      />
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
