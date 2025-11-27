<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentUser } from '../services/authService'
import type { AuthUser } from '../types/IUser.ts'
import { fetchAllUsers, deleteUser, updateUserRole } from "../services/adminService";

const usersLoading = ref(false)
const usersError = ref<string | null>(null)
const user = ref<AuthUser | null>(null)
const users = ref<AuthUser[]>([])
const loading = ref(true)

async function loadUsers() {
  users.value = await fetchAllUsers()
}

onMounted(async () => {
  try {
    user.value = await getCurrentUser()

    if (user.value?.role === "ADMIN") {
      await loadUsers()
    }
  } finally {
    loading.value = false
  }
})

async function removeUser(u: AuthUser) {
  await deleteUser(u.id)
  await loadUsers()
}

async function promoteAdmin(u: AuthUser) {
  await updateUserRole(u.id, "ADMIN")
  await loadUsers()
}
</script>

<template>
  <section class="relative min-h-screen bg-paper text-ink font-body py-20 px-6 md:px-10 flex flex-col items-center">
    <div class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] opacity-[0.06] pointer-events-none"></div>
    <header class="relative z-10 flex flex-col items-center text-center mb-14">
      <div
        class="w-24 h-24 rounded-full bg-[#e6d6a9] flex items-center justify-center text-3xl font-bold font-serif text-[#1a1a1a] mb-5 shadow-inner border border-[#d1c498]">
        {{ user?.name?.charAt(0).toUpperCase() }}
      </div>

      <h1 class="text-5xl md:text-6xl font-serif font-bold leading-tight tracking-tight">
        Redactiebeheer
      </h1>

      <div class="h-[3px] w-20 bg-[#1a1a1a] mt-3 rounded-full"></div>

      <p class="text-gray-700 italic mt-3 text-lg">
        Overzicht van gebruikers, rollen en redactiestatistieken.
      </p>
    </header>


    <div v-if="loading" class="animate-pulse text-gray-500 text-lg mt-10">
      Gegevens worden geladen...
    </div>


    <div v-else class="relative z-10 w-full max-w-6xl bg-white/80 backdrop-blur-md rounded-xl
             border border-gray-300/40 shadow-[0_6px_20px_rgba(0,0,0,0.1)] p-10 space-y-12">

      <section class="bg-[#fdfcf7]/90 rounded-lg shadow-inner border border-gray-300/60 p-8">
        <h2 class="text-3xl font-serif font-semibold text-center mb-8">
          Welkom, hoofdredacteur {{ user?.name }}
        </h2>

        <div class="grid sm:grid-cols-2 gap-6 text-left text-[15px] leading-relaxed">
          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="text-xs uppercase text-gray-500 font-semibold">Naam</p>
            <p class="text-lg font-medium">{{ user?.name }}</p>
          </div>

          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="text-xs uppercase text-gray-500 font-semibold">E-mail</p>
            <p class="text-lg font-medium">{{ user?.email }}</p>
          </div>

          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="text-xs uppercase text-gray-500 font-semibold">Rol</p>
            <p class="text-lg font-medium">{{ user?.role }}</p>
          </div>

          <div class="border-l-4 border-[#1a1a1a]/70 pl-4">
            <p class="text-xs uppercase text-gray-500 font-semibold">Lid sinds</p>
            <p class="text-lg font-medium italic">{{ new Date().getFullYear() }} (demo)</p>
          </div>
        </div>
      </section>

      <section v-if="user?.role === 'ADMIN'" class="space-y-6">
        <header class="flex justify-between items-center">
          <h3 class="text-3xl font-serif font-semibold">Gebruikersbeheer</h3>

          <button @click="loadUsers"
            class="px-5 py-2 bg-ink text-paper rounded-full text-sm tracking-wide shadow hover:bg-gray-700 transition">
            Lijst vernieuwen
          </button>
        </header>

        <p class="text-sm text-gray-700 max-w-lg">
          Hier kun je alle gebruikers beheren: rollen aanpassen, accounts verwijderen en
          redacteurs aanstellen.
        </p>

        <div v-if="usersError" class="bg-red-50 border border-red-300 text-red-700 p-3 rounded">
          {{ usersError }}
        </div>

        <div
          class="overflow-x-auto border border-gray-300 rounded-lg bg-white shadow-inner">
          <table class="min-w-full text-sm">
            <thead class="bg-gray-100 text-left text-gray-600 uppercase text-[11px] tracking-widest">
            <tr>
              <th class="py-3 px-3">Naam</th>
              <th class="py-3 px-3">Email</th>
              <th class="py-3 px-3">Rol</th>
              <th class="py-3 px-3 text-right">Acties</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="usersLoading">
              <td colspan="4" class="py-4 text-center italic text-gray-500">Laden...</td>
            </tr>

            <tr v-for="u in users" :key="u.email" class="border-t hover:bg-[#faf7f0]">
              <td class="py-2 px-3 font-medium">{{ u.name }}</td>
              <td class="py-2 px-3">{{ u.email }}</td>
              <td class="py-2 px-3">
                  <span class="px-2 py-1 text-xs rounded-full"
                        :class="u.role === 'ADMIN'
                                ? 'bg-black text-white'
                                : 'bg-gray-200 text-gray-700'">
                    {{ u.role }}
                  </span>
              </td>

              <td class="py-2 px-3 text-right space-x-2">
                <button @click="promoteAdmin(u)"
                  class="px-3 py-1 bg-black text-white rounded text-xs hover:bg-gray-800 transition">
                  Maak Admin
                </button>

                <button @click="removeUser(u)"
                  class="px-3 py-1 bg-red-200 rounded text-xs hover:bg-red-300 transition">
                  Verwijder
                </button>
              </td>
            </tr>

            <tr v-if="!usersLoading && !users">
              <td colspan="4" class="py-4 text-center italic text-gray-500">
                Geen gebruikers gevonden. Klik op <strong>Lijst vernieuwen</strong>.
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section class="border border-gray-300/50 rounded-lg p-8 bg-white/60 shadow-inner">
        <h3 class="text-xl font-serif font-semibold mb-3">Statistieken & Activiteit</h3>
        <ul class="list-disc list-inside text-gray-600 italic">
          <li>Actieve gebruikers per week</li>
          <li>Populairste forumposts</li>
          <li>Nieuwste registraties</li>
        </ul>
      </section>
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
