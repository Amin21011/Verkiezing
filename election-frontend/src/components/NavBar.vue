<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { authToken, getAuthUser, getCurrentUser, logout } from '@/services/authService'
import SearchBar from '@/components/SearchBar.vue'
import { showToast } from '@/composables/useFlash.ts'
import { useTheme } from '@/composables/useTheme.ts'

const { isDark, toggleTheme } = useTheme()
const router = useRouter()

const user = ref(getAuthUser())
const showMenu = ref(false)
const showOffcanvas = ref(false)
const search = ref('')
const isScrolled = ref(false)

const currentDate = new Date().toLocaleDateString('nl-NL', {
  weekday: 'long',
  day: '2-digit',
  month: 'long',
  year: 'numeric',
})

const ticker = [
  'Laatste peilingen tonen verrassende wending',
  'Nieuwe partij groeit explosief in stedelijke gebieden',
  'Quiz: Welke partij past het best bij jou?',
  'Debat vanavond live te volgen via onze simulator'
]

const goToHome = () => router.push('/')
const goToAccount = () => router.push('/profile')
const toggleMenu = () => (showMenu.value = !showMenu.value)
const toggleOffCanvas = () => (showOffcanvas.value = !showOffcanvas.value)

const handleScroll = () => {
  isScrolled.value = window.scrollY > 40
}

function handleLogout() {
  logout()
  user.value = null
  showMenu.value = false
  goToHome()
  showToast('Succesvol uitgelogd!', 'success')
}

onMounted(async () => {
  window.addEventListener('scroll', handleScroll)

  const restored = getAuthUser()
  if (restored) user.value = restored
  else {
    try {
      const current = await getCurrentUser()
      if (current) user.value = current
    } catch {}
  }

  document.addEventListener('click', (e: MouseEvent) => {
    const target = e.target as HTMLElement
    if (
      target.closest('.user-dropdown') ||
      target.closest('.user-avatar') ||
      target.closest('.offcanvas')
    ) return
    showMenu.value = false
  })
})

onBeforeUnmount(() => window.removeEventListener('scroll', handleScroll))
watch(authToken, () => (user.value = getAuthUser()), { immediate: true })

const handleSearchInput = (query: string) => (search.value = query)
</script>

<template>
  <header class="relative z-50 w-full bg-paper text-ink dark:bg-[var(--paper)] dark:text-[var(--ink)] transition-all duration-300" :class="{ 'sticky top-0 shadow-xl': isScrolled }">
    <div class="flex justify-between items-center px-6 py-2 text-xs font-bold uppercase tracking-widest
             bg-ink text-paper dark:bg-black dark:text-[var(--ink)] border-b-2 border-ink dark:border-[var(--ink)]">
      <div class="flex items-center gap-4">
        <span class="hidden md:inline">Onafhankelijk & Actueel</span>
        <span class="text-red-500">● Live</span>
      </div>

        <div class="flex items-center gap-4">
        <SearchBar v-model="search" @search="handleSearchInput" class="w-[180px] hidden sm:block" />

        <button @click="toggleTheme" class="hover:opacity-80">
          {{ isDark ? 'Licht modus' : 'Donker modus' }} </button>

        <div v-if="!user" @click="router.push('/register')" class="cursor-pointer hover:underline">
          Abonneren</div>
        </div>

    </div>

    <div class="px-6 py-8 border-b-[4px] border-double border-ink dark:border-[var(--ink)]">
      <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 items-center gap-6">

        <div class="hidden md:block text-sm font-serif italic border-r border-gray-300 dark:border-gray-600 pr-4">
          <p>Vandaag:</p>
          <p class="font-bold not-italic mt-1 uppercase text-xs tracking-tighter">
            {{ currentDate }}
          </p>
        </div>

        <div class="text-center cursor-pointer" @click="goToHome">
          <h1 class="font-['Playfair_Display'] font-black text-3xl md:text-6xl tracking-tighter leading-none">
            de KIESKRANT
            <span class="block text-2xl md:text-2xl mt-2 tracking-[0.3em] font-light border-t border-ink dark:border-[var(--ink)] pt-2">
              2025
            </span>
          </h1>
        </div>

        <div class="flex justify-end items-center gap-4">
          <div v-if="user" class="relative">
            <div class="user-avatar w-10 h-10 border-2 border-ink dark:border-[var(--ink)]
                     bg-ink dark:bg-[var(--ink)] text-paper dark:text-black
                     flex items-center justify-center font-bold cursor-pointer"  @click="toggleMenu">
              {{ user.name.charAt(0).toUpperCase() }}
            </div>

            <transition name="fade">
              <div v-if="showMenu" class="user-dropdown absolute right-0 top-12 z-50 w-56 p-4 bg-paper dark:bg-[var(--paper)]
                       border-2 border-ink dark:border-[var(--ink)] shadow-hard">
                <p class="text-[10px] uppercase font-bold text-gray-400 mb-2">
                  Account instellingen
                </p>
                <button class="w-full text-left font-serif font-bold py-2" @click="goToAccount">
                  Mijn Profiel </button>
                <button class="w-full text-left font-serif font-bold py-2 text-red-700"  @click="handleLogout">
                  Uitloggen </button>
              </div>
            </transition>
          </div>

          <button v-else @click="router.push('/login')" class="bg-ink dark:bg-[var(--ink)] text-paper dark:text-black px-4 py-2 font-bold uppercase text-xs tracking-widest">
            Inloggen </button>
        </div>
      </div>
    </div>


    <nav class="bg-paper dark:bg-[var(--paper)] border-b-2 border-ink dark:border-[var(--ink)] px-6">
      <div class="max-w-7xl mx-auto flex justify-between items-center h-12">
        <button @click="toggleOffCanvas" class="relative z-50 font-bold flex items-center gap-2 px-3 py-1 hover:bg-ink hover:text-paper dark:hover:bg-[var(--ink)] dark:hover:text-black">
          ☰ <span class="uppercase text-sm tracking-widest">Secties</span>
        </button>

        <div class="hidden lg:flex gap-8 text-xs font-bold uppercase tracking-widest">
          <router-link to="/latest-news">Laatste Nieuws</router-link>
          <router-link to="/forum">Forum</router-link>
          <router-link to="/candidates">Kandidaten</router-link>
          <router-link to="/quiz" class="text-red-700">Stemwijzer</router-link>
          <router-link to="/map">Provincies</router-link>
        </div>

        <div class="hidden sm:block text-sm italic font-serif">
          "Alles wat wel ertoe doet"
        </div>
      </div>
    </nav>

    <section class="relative overflow-hidden whitespace-nowrap bg-paper dark:bg-[var(--paper)]
             border-b border-ink dark:border-[var(--ink)]">
      <div class="absolute inset-y-0 left-0 bg-red-700 text-white px-4 flex items-center z-10 italic font-bold">
        BREAKING: </div>

      <div class="inline-block py-2 pl-32 hover:[animation-play-state:paused]" style="animation: headerTickerScroll 30s linear infinite;">
        <span v-for="(item, i) in [...ticker,...ticker, ...ticker]"
          :key="i" class="mx-8 font-serif italic text-sm">
          {{ item }}
          <span class="ml-16 text-red-700 not-italic">✦</span>
        </span>
      </div>
    </section>
  </header>


  <teleport to="body">
    <div v-if="showOffcanvas" class="fixed inset-0 z-[9999] bg-black/40 backdrop-blur-sm" @click.self="toggleOffCanvas">
      <aside class="offcanvas h-full w-[320px] p-8 bg-paper dark:bg-[var(--paper)] border-r-4 border-ink dark:border-[var(--ink)] animate-[slideIn_0.3s_ease-out]">
        <div class="flex justify-between items-center mb-10 border-b pb-4">
          <h2 class="font-black text-2xl uppercase">Navigatie</h2>
          <button class="text-3xl" @click="toggleOffCanvas">×</button>
        </div>

        <nav class="space-y-4">
          <router-link v-for="link in [
              ['/latest-news','Laatste Nieuws'], ['/forum','Forum'], ['/quiz','Stemwijzer'], ['/simulator','Tweede kamer'],
              ['/parties','Partijprogramma’s'], ['/candidates','Kandidatenlijst'],
              ['/analyse','Data Analyses'], ['/fake-news','Nieuws-Checker'], ['/map','Kaart per Gemeente'],
              ['/faq','Veelgestelde vragen']
            ]" :key="link[0]" :to="link[0]" class="block text-xl font-serif font-bold hover:text-red-700">
            {{ link[1] }}
          </router-link>
        </nav>
      </aside>
    </div>
  </teleport>
</template>

<style>
@keyframes slideIn {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}

@keyframes headerTickerScroll {
  from { transform: translateX(0); }
  to { transform: translateX(-100%); }
}

header::before {
  content: "";
  position: absolute;
  inset: 0;
  opacity: 0.03;
  pointer-events: none;
  background-image: url('https://www.transparenttextures.com/patterns/paper-fibers.png');
}
</style>
