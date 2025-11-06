<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterView } from 'vue-router'
import './assets/main.css'
import NavBar from '@/components/NavBar.vue'
import FlashContainer from '@/components/FlashContainer.vue'
import FooterComp from '@/components/FooterComp.vue'

const showButton = ref(false)

function scrollToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

function handleScroll() {
  showButton.value = window.scrollY > 300
}

onMounted(() => window.addEventListener('scroll', handleScroll))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<template>
  <div class="flex flex-col min-h-screen bg-paper text-ink font-body relative">
    <NavBar />
    <FlashContainer />
    <main class="flex-grow relative">
      <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')]"></div>
      <RouterView class="relative z-10" />
    </main>
    <FooterComp/>
    <button v-if="showButton" @click="scrollToTop" class="fixed bottom-4 right-4 p-2 bg-black text-white rounded-full w-10 h-10 cursor-pointer hover:bg-blue-950 z-50 transition-opacity">
      🡩
    </button>
  </div>
</template>

