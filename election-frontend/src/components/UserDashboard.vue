<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentUser } from '@/services/authService'
import type { AuthUser } from '@/types/IUser.ts'

const user = ref<AuthUser | null>(null)
const loading = ref(true)

onMounted(async () => {
  try {
    user.value = await getCurrentUser()
  } catch (e) {
    console.error('Kon gebruiker niet ophalen:', e)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <section
    class="relative min-h-screen bg-paper text-ink font-body flex flex-col items-center py-20 px-6 md:px-10">
    <div class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] opacity-[0.06] pointer-events-none"></div>

    <header class="text-center mb-12 border-b border-ink/30 pb-6 max-w-2xl">
      <h1 class="text-4xl md:text-5xl font-serif font-bold tracking-tight leading-tight">
        Mijn Account</h1>
      <p class="text-gray-600 italic mt-2">
        Jouw persoonlijke redactionele ruimte.</p>
    </header>

    <div v-if="loading" class="animate-pulse text-gray-500 text-lg mt-10">
      Gebruikersgegevens worden geladen...
    </div>

    <div v-else class="relative z-10 w-full max-w-3xl bg-white/70 backdrop-blur-md rounded-xl shadow-[0_6px_20px_rgba(0,0,0,0.1)] border border-gray-300/40 p-8 md:p-10">
      <div class="text-center mb-10">
        <h2 class="text-3xl font-serif font-semibold text-ink mb-2">
          Welkom terug, {{ user?.name }}
        </h2>
        <p class="text-gray-700 text-sm">
          Je bevindt je in de hoofdredactie van jouw profiel.
        </p>
      </div>

      <div class="divide-y divide-gray-300/40 border-y border-gray-300/40 mb-10">
        <div class="py-4 flex justify-between items-center">
          <span class="font-semibold text-gray-600 uppercase text-sm tracking-wide">Naam</span>
          <span class="text-lg font-medium">{{ user?.name }}</span>
        </div>
        <div class="py-4 flex justify-between items-center">
          <span class="font-semibold text-gray-600 uppercase text-sm tracking-wide">E-mail</span>
          <span class="text-lg font-medium">{{ user?.email }}</span>
        </div>
      </div>

      <article class="border border-gray-300/40 rounded-lg p-6 md:p-8 bg-white/50 shadow-inner">
        <header class="mb-3 flex items-center justify-between">
          <h3 class="text-xl font-serif font-semibold text-ink">Mijn Forumrubriek</h3>
          <span class="text-xs uppercase text-gray-500 tracking-widest font-semibold">Binnenkort</span>
        </header>
        <p class="text-gray-700 leading-relaxed text-sm md:text-base">
          Binnenkort kun je hier jouw ingezonden vragen, columns en reacties teruglezen — allemaal onder jouw eigen digitale byline.
        </p>
      </article>

      <div class="mt-10 text-center">
        <button
          class="px-8 py-3 font-serif bg-ink text-paper text-lg tracking-wide rounded-full hover:bg-gray-800 transition-all duration-200 shadow-md hover:shadow-lg"
        >
          Naar het Forum ☕
        </button>
      </div>
    </div>
  </section>
</template>

<style scoped>
.font-body {
  font-family: 'Inter', system-ui, sans-serif;
}
.font-serif {
  font-family: 'Playfair Display', 'Times New Roman', serif;
}
.text-ink {
  color: #1a1a1a;
}
.bg-paper {
  background-color: #f9f7f4;
}
</style>
