<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { authToken, getAuthUser, getCurrentUser, logout } from '@/services/authService'
import { showToast } from '@/helpers/useFlash.ts'

const router = useRouter()
const isScrolled = ref(false)
const user = ref(getAuthUser())
const showMenu = ref(false)
const showOffcanvas = ref(false)

const goToHome = () => router.push('/')

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

const closeMenu = () => { showMenu.value = false }

onMounted(async () => {
  const restoredUser = getAuthUser()
  if (restoredUser) {
    user.value = restoredUser
  } else {
    try {
      const current = await getCurrentUser()
      if (current) user.value = current
    } catch (err) {
      console.warn('Kon gebruiker niet herstellen:', err)
    }
  }

  document.addEventListener('click', (e) => {
    const target = e.target as HTMLElement
    if (!target.closest('.user-dropdown') && !target.closest('.user-avatar')) {
      showMenu.value = false
    }
  })

  window.addEventListener('scroll', handleScroll)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})

function handleLogout() {
  logout()
  user.value = null
  showMenu.value = false
  goToHome()
  showToast('Succesvol uitgelogd!', 'success')
}

watch(authToken, () => {
  user.value = getAuthUser()
}, { immediate: true })

const toggleMenu = () => {
  showMenu.value = !showMenu.value
}

const goToAccount = () => router.push('/account')

const ticker = [
  'Laatste peilingen tonen verrassende wending',
  'Nieuwe partij groeit explosief in stedelijke gebieden',
  'Quiz: Hoe goed ken jij de top-partijen?',
]

function toggleOffcanvas() {
  showOffcanvas.value = !showOffcanvas.value
}
</script>



<template>
  <header class="w-full py-4 border-b ">
    <div class="flex justify-between items-center pb-4 px-6">
      <div class="flex gap-3">
        <button @click="toggleOffcanvas" class="bg-transparent border-none cursor-pointer text-xl px-2 py-1 transition hover:text-indigo-700" title="Menu">
          ☰ </button>
        <button class="bg-transparent border-none cursor-pointer text-xl px-2 py-1 transition hover:text-indigo-700" title="Dark Mode">
          🌙 </button>
      </div>

      <div v-if="user" class="relative">
        <div class="user-avatar w-[36px] h-[36px] rounded-full bg-[darkslateblue] text-white font-bold flex items-center justify-center cursor-pointer select-none"
          @click="toggleMenu"> {{ user.name.charAt(0).toUpperCase() }} </div>
        <div v-if="showMenu" class="fixed inset-0 z-40" @click="closeMenu"></div>

        <transition name="fade">
          <div v-if="showMenu" class="user-dropdown absolute top-[45px] right-0 bg-white border border-gray-300 rounded-xl shadow-lg p-3 flex flex-col gap-[6px] w-[180px] z-50">
            <div class="flex justify-between items-center border-b pb-1 mb-1">
              <span class="font-semibold text-sm text-gray-700">Sluiten</span>
              <button class="text-gray-500 hover:text-gray-800 text-lg leading-none  cursor-pointer "
                @click="closeMenu">✕</button>
            </div>

            <button class="px-3 py-2 text-left rounded-md text-gray-700 hover:bg-gray-100 transition"
              @click="goToAccount">Mijn Account</button>

            <button class="px-3 py-2 text-left rounded-md text-red-600 hover:bg-red-100 transition"
              @click="handleLogout"> Uitloggen
            </button>
          </div>
        </transition>
      </div>


      <div v-else>
        <button class="cursor-pointer px-2 py-1" @click="() => router.push('/register')">
          <img src="../assets/img/images.png" class="w-8 h-8 object-contain" />
        </button>
      </div>
    </div>

    <div @click="goToHome" class="text-center text-[#111827] mt-4 cursor-pointer">
      <h1 class="font-[Playfair Display] font-black text-[3rem] md:text-[3.5rem] tracking-[2px] bg-gradient-to-r from-[#1c1c1c] to-[#1c1c1c] bg-clip-text text-transparent my-2 transition hover:scale-105 inline-block">
        VERKIEZINGEN 2025</h1>

      <div class="font-semibold bg-yellow-100 px-4 py-2 rounded-lg mt-2">
        Alles wat je moet weten voordat je stemt.
      </div>
    </div>

    <section class="bg-[#e5e5e5] overflow-hidden whitespace-nowrap border-y-[2px] border-[#1c1c1c] w-full mt-4">
      <div class="inline-block py-2 animate-[tickerScroll_20s_linear_infinite]">
        <span v-for="(item, i) in ticker" :key="i" class="mr-16 font-bold uppercase">
          {{ item }}
        </span>
      </div>
    </section>
  </header>

  <teleport to="body">
    <div v-if="showOffcanvas" class="offcanvas-overlay" @click.self="toggleOffcanvas">
      <div class="offcanvas">
        <button class="close-btn" @click="toggleOffcanvas">✕</button>
        <router-link to="/latest-news">Laatste Nieuws</router-link>
        <router-link to="/">Trending</router-link>
        <router-link to="/forum">Forum</router-link>
        <router-link to="/quiz">Quiz</router-link>
        <router-link to="/simulator">Tweede Kamer Simulator</router-link>

        <router-link to="/candidates">Kandidaten</router-link>
        <router-link to="/">FAQ</router-link>
      </div>
    </div>
  </teleport>
</template>

<style scoped>
.offcanvas-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.7);
  z-index: 9999;
}

.offcanvas {
  background: white;
  color: white;
  width: 280px;
  height: 100%;
  padding: 24px;
  animation: slideIn 0.3s ease forwards;
}

@keyframes slideIn {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}

.close-btn {
  font-size: 25px;
  color: black;
  background: none;
  border: none;
  cursor: pointer;
  float: right;
  font-weight: 700;
}

.offcanvas a {
  background-color: transparent;
  display: flex;
  align-items: center;
  color: black;
  text-decoration: none;
  margin: 35px 0;
  font-weight: 700;
  font-size: 1.2rem;
  position: relative;
  padding-left: 1.5rem;
}


.offcanvas a::before {
  content: '▶';
  position: absolute;
  left: 0;
  font-size: 0.9rem;
  color: black;
  transition: transform 0.2s ease;
}

.offcanvas a:hover::before {
  transform: translateX(4px);
}
</style>
