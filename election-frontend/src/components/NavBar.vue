<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { authToken, getAuthUser, getCurrentUser, logout } from '@/services/authService'
import SearchBar from '@/components/SearchBar.vue'
import { showToast } from '@/helpers/useFlash'
import { useTheme } from '@/helpers/useTheme'

const { isDark, toggleTheme } = useTheme()
const router = useRouter()

const user = ref(getAuthUser())
const showMenu = ref(false)
const showOffcanvas = ref(false)
const search = ref('')
const isScrolled = ref(false)

const ticker = [
  'Laatste peilingen tonen verrassende wending',
  'Nieuwe partij groeit explosief in stedelijke gebieden',
  'Quiz: Hoe goed ken jij de top-partijen?',
]

const goToHome = () => router.push('/')
const goToAccount = () => router.push('/account')

const toggleMenu = () => (showMenu.value = !showMenu.value)
const closeMenu = () => (showMenu.value = false)
const toggleOffCanvas = () => (showOffcanvas.value = !showOffcanvas.value)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

function handleLogout() {
  logout()
  user.value = null
  showMenu.value = false
  goToHome()
  showToast('Succesvol uitgelogd!', 'success')
}

onMounted(async () => {
  const restored = getAuthUser()
  if (restored) {
    user.value = restored
  } else {
    try {
      const current = await getCurrentUser()
      if (current) user.value = current
    } catch {}
  }

  window.addEventListener('scroll', handleScroll)

  document.addEventListener('click', (e) => {
    const target = e.target as HTMLElement
    if (!target.closest('.user-dropdown') && !target.closest('.user-avatar')) {
      showMenu.value = false
    }
  })
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})

watch(authToken, () => {
  user.value = getAuthUser()
}, { immediate: true })

const handleSearchInput = (query: string) => {
  search.value = query
}
</script>

<template>
  <header class="w-full border-b transition-colors bg-retro-paper text-retro-ink
           dark:bg-retro-paperDark dark:text-retro-inkDark
           border-retro-ink dark:border-retro-inkDark">
    <div class="flex justify-between items-center px-6 py-4">
      <div class="flex gap-3 items-center">
        <button @click="toggleOffCanvas"
          class="text-xl px-2 py-1 hover:text-indigo-600 dark:hover:text-indigo-400" title="Menu">
          ☰
        </button>

        <button @click="toggleTheme"
          class="text-xl transition-transform hover:scale-110"
          aria-label="Toggle theme">
          <span v-if="isDark">☀️</span>
          <span v-else>🌙</span>
        </button>
      </div>

      <div class="flex items-center gap-4">
        <SearchBar v-model="search" @search="handleSearchInput" />

        <div v-if="user" class="relative">
          <div class="user-avatar w-9 h-9 rounded-full bg-indigo-700 text-white font-bold
                   flex items-center justify-center cursor-pointer select-none" @click="toggleMenu">
            {{ user.name.charAt(0).toUpperCase() }}
          </div>

          <div v-if="showMenu" class="fixed inset-0 z-40" @click="closeMenu"></div>

          <transition name="fade">
            <div v-if="showMenu"
              class="user-dropdown absolute right-0 top-11 z-50
                     w-48 rounded-xl shadow-lg p-3
                     bg-white text-gray-800
                     dark:bg-neutral-900 dark:text-gray-200
                     border border-gray-300 dark:border-neutral-700">
              <div class="flex justify-between items-center border-b pb-2 mb-2
                          border-gray-200 dark:border-neutral-700">
                <span class="text-sm font-semibold">Menu</span>
                <button class="text-gray-500 hover:text-black dark:text-gray-400 dark:hover:text-white" @click="closeMenu">
                  ✕
                </button>
              </div>

              <button class="w-full text-left px-3 py-2 rounded-md
                       hover:bg-gray-100 dark:hover:bg-neutral-800"
                @click="goToAccount">
                Mijn account
              </button>

              <button class="w-full text-left px-3 py-2 rounded-md text-red-600 hover:bg-red-100 dark:text-red-400 dark:hover:bg-red-900/30" @click="handleLogout">
                Uitloggen
              </button>
            </div>
          </transition>
        </div>

        <div v-else>
          <button @click="() => router.push('/register')">
            <img src="../assets/img/images.png" class="w-8 h-8" alt="login" />
          </button>
        </div>
      </div>
    </div>

    <div @click="goToHome" class="text-center cursor-pointer pb-4">
      <h1 class="font-[Playfair Display] font-black text-4xl md:text-5xl tracking-wider transition hover:scale-105">
        VERKIEZINGEN 2025
      </h1>

      <div class="inline-block mt-3 px-4 py-2 rounded-lg font-semibold bg-yellow-100 text-black dark:bg-yellow-200">
        Alles wat je moet weten voordat je stemt.
      </div>
    </div>

    <section class="overflow-hidden whitespace-nowrap border-y bg-neutral-200 border-neutral-400 dark:bg-neutral-800 dark:border-neutral-600">
      <div class="inline-block py-2 animate-[tickerScroll_20s_linear_infinite]">
        <span v-for="(item, i) in ticker" :key="i" class="mx-12 font-bold uppercase">
          {{ item }}
        </span>
      </div>
    </section>
  </header>

  <teleport to="body">
    <div v-if="showOffcanvas" class="fixed inset-0 z-[9999] bg-black/70" @click.self="toggleOffCanvas">
      <div class="h-full w-[280px] p-6 bg-white text-black dark:bg-neutral-900 dark:text-white animate-[slideIn_0.3s_ease_forwards]">
        <button class="text-2xl font-bold mb-6" @click="toggleOffCanvas">
          ✕
        </button>

        <router-link
          v-for="link in [
            ['/latest-news','Laatste Nieuws'],
            ['/forum','Forum'],
            ['/quiz','Quiz'],
            ['/simulator','Tweede Kamer Simulator'],
            ['/map','De Provincies'],
            ['/candidates','Kandidaten'],
            ['/parties','Partijen'],
            ['/faq','FAQ']
          ]"
:key="link[0]" :to="link[0]" class="block my-5 font-bold hover:underline">
          {{ link[1] }}
        </router-link>
      </div>
    </div>
  </teleport>
</template>

<style scoped>
@keyframes slideIn {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}
</style>
