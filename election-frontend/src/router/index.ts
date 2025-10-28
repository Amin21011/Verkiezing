import { createRouter, createWebHistory } from 'vue-router'
import HomepageView from '@/views/HomepageView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import PartyListView from '@/views/PartyListView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomepageView,
    },
    {
      path: '/parties',
      name: 'parties',
      component: PartyListView,
    },
    {
      path: '/polls',
      name: 'polls',
      component: () => import('../views/PollView.vue'),
    },
    {
      path: `/latest-news`,
      name: `latest-news`,
      component: () => import(`../views/NewsView.vue`),
    },
    {
      path: `/register`,
      name: `register`,
      component: RegisterView
    },
    {
      path: `/login`,
      name: `login`,
      component: LoginView
    },
  ],
})

export default router
