import { createRouter, createWebHistory } from 'vue-router';
import HomepageView from '@/views/HomepageView.vue';
import PartyListView from '@/views/PartyListView.vue';
import CandidatesView from '@/views/CandidatesView.vue';

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
      path: '/candidates',
      name: 'candidates',
      component: CandidatesView,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'), // lazy-loaded
    },
    {
      path: '/polls',
      name: 'polls',
      component: () => import('../views/PollView.vue'), // lazy-loaded
    },
    {
      path: '/latest-news',
      name: 'latest-news',
      component: () => import('../views/NewsView.vue'), // lazy-loaded
    },
  ],
});

export default router;
