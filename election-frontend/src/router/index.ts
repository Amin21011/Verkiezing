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
      component: () => import('../views/PartyListView.vue'),
     },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
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
    path: '/quiz',
    name: 'quiz',
    component: () => import('../views/QuizView.vue')}
  ],
})

export default router
