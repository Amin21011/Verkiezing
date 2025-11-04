import { createRouter, createWebHistory } from 'vue-router';
import HomepageView from '@/views/HomepageView.vue';
import CandidatesView from '@/views/CandidatesView.vue';
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import PartyListView from '../views/PartyListView.vue'
import AccountView from '../views/AccountView.vue'

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
    // {
    //   path: '/about',
    //   name: 'about',
    //   component: () => import('../views/AboutView.vue'), // lazy-loaded
    // },
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
    {
      path: '/account',
      name: 'account',
      component: AccountView,
      meta: { requiresAuth: true }
    }
  ],
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    console.warn('Toegang geweigerd: niet ingelogd')
    next('/login')
  } else {
    next()
  }
})

export default router;
