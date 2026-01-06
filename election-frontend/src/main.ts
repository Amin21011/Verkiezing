import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

if (localStorage.getItem('theme') === 'dark') {
  document.documentElement.classList.add('dark')
}

const app = createApp(App)
app.use(router)
app.mount('#app')
