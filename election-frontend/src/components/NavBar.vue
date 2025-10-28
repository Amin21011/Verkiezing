<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { authToken, getAuthUser, logout } from '@/services/authService'

const router = useRouter()
const isScrolled = ref(false)
const goToHome = () => router.push('/')

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

const user = ref(getAuthUser())

function handleLogout() {
  logout()
  user.value = null
}

watch(authToken, () => {
  user.value = getAuthUser()
})

const showMenu = ref(false)
const toggleMenu = () => {
  showMenu.value = !showMenu.value
}

const goToAccount = () => router.push('/account')

const ticker = [
  'Laatste peilingen tonen verrassende wending',
  'Nieuwe partij groeit explosief in stedelijke gebieden',
  'Quiz: Hoe goed ken jij de top-partijen?',
]
</script>


<template>
  <header :class="['sticky top-0 z-50 bg-paper border-b-8 border-ink shadow-[0_4px_20px_rgba(0,0,0,0.1)] transition-all', isScrolled ? 'py-0' : 'py-0']">
    <div class="flex justify-between items-center pb-4 px-6">
      <div class="flex gap-3">
        <button class="bg-transparent border-none cursor-pointer text-xl px-2 py-1 transition hover:text-indigo-700" title="Menu">
          ☰
        </button>
        <button class="bg-transparent border-none cursor-pointer text-xl px-2 py-1 transition hover:text-indigo-700" title="Dark Mode">
          🌙
        </button>
      </div>

      <div v-if="user" class="relative">
        <div class="w-[36px] h-[36px] rounded-full bg-[darkslateblue] text-white font-bold flex items-center justify-center cursor-pointer select-none" @click="toggleMenu">
          {{ user.name.charAt(0).toUpperCase() }}
        </div>

        <div v-if="showMenu" class="absolute top-[45px] right-0 bg-white border border-gray-300 rounded-lg p-2 flex flex-col gap-[4px] z-50">
          <button class="px-2 py-2 text-left rounded hover:bg-gray-100" @click="goToAccount">
            Mijn Account
          </button>
          <button class="px-2 py-2 text-left rounded text-red-600 hover:bg-gray-100" @click="handleLogout">
            Uitloggen
          </button>
        </div>
      </div>

      <div v-else>
        <button class="cursor-pointer px-2 py-1" @click="() => router.push('/register')">
          <img src="../assets/img/images.png" class="w-8 h-8 object-contain" />
        </button>
      </div>
    </div>

    <div @click="goToHome" class="text-center text-[#111827] mt-4 cursor-pointer transition hover:scale-[1.01]">
      <h1 class="font-[Playfair Display] font-black text-[3rem] md:text-[3.5rem] tracking-[2px] bg-gradient-to-r from-[#1c1c1c] to-[#4b5563] bg-clip-text text-transparent my-2">
        VERKIEZINGEN 2025 ✏️</h1>

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
</template>

