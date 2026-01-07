<script setup lang="ts">
import { ref } from 'vue'
import { verifyResetIdentity, resetPassword } from '@/services/authService'
import { showToast } from '@/composables/useFlash'

const step = ref<'verify' | 'reset'>('verify')
const email = ref('')
const birthDate = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const loading = ref(false)

async function verify() {
  try {
    loading.value = true
    await verifyResetIdentity(email.value, birthDate.value)
    step.value = 'reset'
  } catch {
    showToast("Gegevens komen niet overeen.", "error")
  } finally {
    loading.value = false
  }
}

async function submitNewPassword() {
  if (newPassword.value !== confirmPassword.value) {
    showToast("Wachtwoorden komen niet overeen.", "error")
    return
  }

  try {
    loading.value = true
    await resetPassword(email.value, newPassword.value)
    showToast("Wachtwoord succesvol gewijzigd.", "success")
  } catch {
    showToast("Wachtwoord wijzigen mislukt.", "error")
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="space-y-8 animate-fadeIn">
    <header>
      <h2 class="text-2xl font-headline font-bold">
        Wachtwoord herstellen </h2>
      <p class="text-sm text-graymain dark:text-[var(--muted)]">
        Ter verificatie vragen we enkele gegevens. </p>
    </header>

    <form v-if="step === 'verify'" @submit.prevent="verify" class="space-y-4">
      <input v-model="email" type="email" placeholder="E-mailadres" required class="input-field" />
      <input v-model="birthDate" type="date" required class="input-field" />
      <button class="btn-primary" :disabled="loading">
        Verifiëren </button>
    </form>

    <form v-else @submit.prevent="submitNewPassword" class="space-y-4">
      <input v-model="newPassword" type="password" placeholder="Nieuw wachtwoord" required class="input-field" />
      <input v-model="confirmPassword" type="password" placeholder="Bevestig wachtwoord" required class="input-field" />
      <button class="btn-primary" :disabled="loading">
        Wachtwoord opslaan
      </button>
    </form>
  </section>
</template>
