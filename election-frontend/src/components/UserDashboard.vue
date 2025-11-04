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
  <section class="p-8 max-w-3xl mx-auto bg-paper">
    <h1 class="text-4xl font-bold mb-6">Mijn Account</h1>

    <div v-if="user" class="space-y-2">
      <p><strong>Naam:</strong> {{ user.name }}</p>
      <p><strong>Email:</strong> {{ user.email }}</p>
    </div>

    <div class="mt-8 border-t pt-4">
      <h2 class="font-semibold text-xl mb-2">Mijn Forum Vragen</h2>
      <p class="text-gray-500 text-sm">Binnenkort beschikbaar…</p>
    </div>
  </section>
</template>
