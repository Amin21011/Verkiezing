<script setup lang="ts">
import { ref, onMounted } from 'vue'
import FooterComp from '@/components/FooterComp.vue'
import UserDashboard from '../components/UserDashboard.vue'
import AdminDashboard from '../components/AdminDashboard.vue'
import { getAuthUser, getCurrentUser } from '@/services/authService'

const userRole = ref<string | null>(null)
const loading = ref(true)

onMounted(async () => {
  try {
    // haalt meest actuele data op
    const currentUser = await getCurrentUser()
    if (currentUser) {
      userRole.value = currentUser.role
    } else {
      const decoded = getAuthUser()
      userRole.value = decoded?.role ?? null
    }
  } catch (error) {
    console.error('Fout bij het ophalen van gebruiker:', error)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div v-if="loading" class="p-8 text-center">
    <p>Gebruikersgegevens worden geladen...</p>
  </div>

  <div v-else>
    <UserDashboard v-if="userRole === 'USER'" />
    <AdminDashboard v-else-if="userRole === 'ADMIN'" />
    <p v-else class="text-center text-red-500 mt-6">
      Geen geldige rol gevonden of niet ingelogd.
    </p>
  </div>
</template>

<style scoped>
p {
  font-size: 1rem;
}
</style>
