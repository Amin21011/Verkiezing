import { createRouter, createWebHistory } from 'vue-router';
import HomepageView from '@/views/HomepageView.vue';
import CandidatesView from '@/views/CandidatesView.vue';
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import PartyListView from '../views/PartyListView.vue'
import AccountView from '../views/AccountView.vue'
import ForumPostView from '@/views/ForumPostView.vue'
import ForumView from '@/views/ForumView.vue'
import ForumDetailView from '@/views/ForumDetailView.vue'
import MapView from '@/views/MapView.vue'
import SimulatorView from '@/views/SimulatorView.vue'

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
      path: '/polls',
      name: 'polls',
      component: () => import('../views/PollView.vue'),
    },
    {
      path: '/latest-news',
      name: 'latest-news',
      component: () => import('../views/NewsView.vue'),
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
      meta: { requiresAuth: true },
    },
    {
      path: `/simulator`,
      name: `seats`,
      component: SimulatorView
    },
    {
      path: '/quiz',
      name: 'quiz',
      component: () => import('../views/QuizView.vue'),
    },
    {
      path: `/forum`,
      name: `forum`,
      component: ForumView
    },
    {
      path: `/post`,
      name: `post`,
      component: ForumPostView
    },
    {
      path: "/forum/:id",
      name: "ForumDetail",
      component: ForumDetailView
    },
    {
      path: `/map`,
      name: `map`,
      component: MapView
    },
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

export default router
