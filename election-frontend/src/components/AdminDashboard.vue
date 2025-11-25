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
  <section class="relative min-h-screen bg-paper text-ink font-body flex flex-col items-center py-20 px-6 md:px-10">
    <div class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] opacity-[0.06] pointer-events-none">
    </div>

    <header class="flex flex-col items-center text-center mb-14">
      <div class="w-24 h-24 rounded-full bg-[#e6d6a9] flex items-center justify-center text-3xl font-bold font-serif text-[#1a1a1a] mb-5 shadow-inner border border-[#d1c498]">
        {{ user?.name?.charAt(0).toUpperCase() }}
      </div>
      <h1 class="text-5xl md:text-6xl font-serif font-bold tracking-tight leading-tight">
        Redactiebeheer
      </h1>
      <div class="h-[3px] w-16 bg-[#1a1a1a] mt-3 rounded-full"></div>
      <p class="text-gray-700 italic mt-3">
        Overzicht van gebruikers, publicaties en statistieken.
      </p>
    </header>

    <div v-if="loading" class="animate-pulse text-gray-500 text-lg mt-10">
      Beheerdersgegevens worden geladen...
    </div>

    <div v-else class="relative z-10 w-full max-w-5xl bg-white/80 backdrop-blur-md rounded-xl shadow-[0_6px_20px_rgba(0,0,0,0.1)] border border-gray-300/40 p-10">
      <div class="bg-[#fdfcf7]/90 border border-gray-300/60 rounded-lg shadow-inner p-8 mb-10">
        <h2 class="text-3xl font-serif font-semibold text-center mb-8 tracking-tight">
          Welkom, hoofdredacteur {{ user?.name }}
        </h2>

        <div class="grid sm:grid-cols-2 gap-6 text-left font-body text-[15px] leading-relaxed">
          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="uppercase text-xs text-gray-500 font-semibold">
              Naam
            </p>
            <p class="text-lg font-medium text-[#1a1a1a]">
              {{ user?.name }}
            </p>
          </div>
          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="uppercase text-xs text-gray-500 font-semibold">
              E-mail
            </p>
            <p class="text-lg font-medium text-[#1a1a1a]">
              {{ user?.email }}
            </p>
          </div>

          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="uppercase text-xs text-gray-500 font-semibold">
              Rol
            </p>
            <p class="text-lg font-medium">
              {{ user?.role ? user.role : 'ADMIN' }}
            </p>
          </div>
          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="uppercase text-xs text-gray-500 font-semibold">
              Lid sinds
            </p>
            <p class="text-lg font-medium italic">
              {{ new Date().getFullYear() }} (demo)
            </p>
          </div>
        </div>
      </div>

      <div class="grid md:grid-cols-2 gap-8">
        <article class="border border-gray-300/50 rounded-lg p-8 bg-white/60 shadow-inner">
          <header class="mb-4 flex items-center justify-between">
            <h3 class="text-xl font-serif font-semibold text-[#1a1a1a]">
              Gebruikersoverzicht
            </h3>
            <span class="text-xs uppercase text-gray-500 tracking-widest font-semibold">Beheer</span>
          </header>
          <p class="text-gray-700 text-sm mb-4">
            Binnenkort verschijnt hier een lijst met alle geregistreerde
            gebruikers. Je kunt dan rollen aanpassen, accounts blokkeren en
            statistieken bekijken.
          </p>
          <ul class="list-disc list-inside text-gray-600 italic">
            <li>Bekijk of verwijder accounts</li>
            <li>Promoot gebruikers tot redacteur of beheerder</li>
            <li>Controleer laatste inlogmomenten</li>
            <li>Voeg en verwijder polls</li>
          </ul>
        </article>

        <article
          class="border border-gray-300/50 rounded-lg p-8 bg-white/60 shadow-inner">
          <h3 class="text-xl font-serif font-semibold mb-3">
            Statistieken & Activiteit
          </h3>
          <p class="text-gray-700 text-sm mb-2">
            Binnenkort kun je hier inzicht krijgen in:
          </p>
          <ul class="list-disc list-inside text-gray-600 italic">
            <li>Actieve gebruikers per week</li>
            <li>Populairste forumposts</li>
            <li>Nieuwste registraties</li>
          </ul>
        </article>
      </div>

      <div class="mt-12 text-center">
        <button class="px-8 py-3 font-serif bg-ink text-paper text-lg tracking-wide rounded-full hover:bg-gray-300 transition-all duration-200 shadow-md hover:shadow-lg">
          Beheerderspaneel openen ⚙️
        </button>
      </div>
    </div>
  </section>
</template>

<style scoped>
.font-body {
  font-family: "Merriweather", serif;
}
.font-serif {
  font-family: "Playfair Display", "Times New Roman", serif;
}
.text-ink {
  color: #1a1a1a;
}
.bg-paper {
  background-color: #f9f7f4;
}
</style>
