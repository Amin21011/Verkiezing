<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentUser } from '@/services/authService'
import type { AuthUser } from '@/types/IUser.ts'

const user = ref<AuthUser | null>(null)

onMounted(async () => {
  user.value = await getCurrentUser()
})
</script>

<template>
  <section class="p-6 text-center">
    <div v-if="user" class="space-y-2">
    <h1 class="text-2xl font-bold">Welkom {{ user.name }} </h1>
    <p>Je hebt nu toegang tot alle beheerfunctionaliteiten.</p>
    </div>

    <div class="mt-8 border-t pt-4">
      <h2 class="font-semibold text-xl mb-2">Lijst van alle huidige gebruikers: </h2>
      <p class="text-gray-500 text-sm">Binnenkort beschikbaar…</p>
    </div>

  </section>
</template>

<style></style>
