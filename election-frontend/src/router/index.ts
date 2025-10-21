import { createRouter, createWebHistory } from 'vue-router'
import HomepageView from '@/views/HomepageView.vue'
import PartyListView from '../views/PartyListView.vue'

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
      component: () => import('../views/PartyListView.vue'),    },
    {
      path: '/polls',
      name: 'polls',
      component: () => import('../views/PollView.vue'),
    },
    {
      path: `/latest-news`,
      name: `latest-news`,
      component: () => import(`../views/NewsView.vue`)
    },
    {
      path: `/register`,
      name: `register`,
      component: () => import(`../views/RegisterView.vue`)
    }
  ],
})

export default router
