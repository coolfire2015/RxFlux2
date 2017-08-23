import React, { Component } from 'react'
import { View, Text, StyleSheet } from 'react-native'

export default class extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>
          Hello React Native!
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#ffffff'
  },
  text: {
    fontSize: 20,
    color: '#333333'
  }
})

作者：慌不要慌
链接：http://www.jianshu.com/p/fc29c86fc2b8
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。