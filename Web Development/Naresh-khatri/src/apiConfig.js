const localIp = '192.168.29.27'
// const localIp = 'localhost'

const prefix = window.location.port === '8080' ?
    `http://${localIp}:4000` : 'https://d2c.plasmatch.in';
const prefixWs = window.location.port === '8080' ?
    `ws://${localIp}:4000` : 'wss://d2c.plasmatch.in'

export { prefix, prefixWs }