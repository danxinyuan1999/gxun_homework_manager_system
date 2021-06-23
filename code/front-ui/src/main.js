import Vue from 'vue'

import 'normalize.css/normalize.css'

import Element from 'element-ui'
import './styles/element-variables.scss'
import '@/styles/index.scss'
import '@/styles/meetboxs.scss'

import App from './App'

import './icons'
import './permission'
import './utils/error-log'

Vue.use(Element, {
  size: 'medium'
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  store,
  render: h => h(App)
})
