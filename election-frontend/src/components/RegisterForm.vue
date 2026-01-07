<script setup lang="ts">
import { ref } from 'vue'
import { login, register } from '@/services/authService'
import { showToast } from '@/composables/useFlash'
import { useRouter } from 'vue-router'

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const router = useRouter()

const handleSubmit = async () => {
  if (password.value !== confirmPassword.value) {
    showToast('De wachtwoorden komen niet overeen.', 'error')
    return
  }

  if (password.value.length < 8) {
    showToast('Wachtwoord moet minimaal 8 tekens bevatten.', 'error')
    return
  }

  loading.value = true

  try {
    await register(name.value.trim(), email.value.trim(), password.value)

    showToast('Account succesvol aangemaakt.', 'success')

    const loginData = await login(email.value, password.value)
    showToast(`Welkom ${loginData.displayName || name.value}!`, 'success')

    await router.push('/')
  } catch (err: any) {
    console.error('Registratiefout:', err)

    const message =
      err?.response?.data?.message ||
      err?.message ||
      'Registratie mislukt. Probeer het opnieuw.'

    showToast(message, 'error')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <form @submit.prevent="handleSubmit" class="relative w-full max-w-md bg-paper border-4 border-ink shadow-press p-8 space-y-6">
    <div class="absolute inset-0 opacity-[0.04] pointer-events-none" style="background-image:url('https://www.transparenttextures.com/patterns/newsprint.png')">
    </div>

    <header class="relative z-10 space-y-2">
      <p class="uppercase tracking-[0.35em] text-[10px] font-mono text-graymain">
        Account aanmaken
      </p>

      <h2 class="text-3xl font-headline font-black border-b-2 border-ink/40 pb-2">
        Registreer
      </h2>
    </header>

    <div class="relative z-10 space-y-4">
      <input v-model="name" type="text" placeholder="Naam" class="input-field" required />
      <input v-model="email" type="email" placeholder="E-mailadres" class="input-field" required />
      <input v-model="password" type="password" placeholder="Wachtwoord (min. 8 tekens)" class="input-field" required />
      <input v-model="confirmPassword" type="password" placeholder="Bevestig wachtwoord" class="input-field" required />
    </div>

    <button type="submit" :disabled="loading" class="btn-primary w-full uppercase tracking-widest text-sm relative z-10
    dark:bg-[var(--ink)] dark:text-[var(--paper)] dark:border-[var(--border-soft)]
    disabled:opacity-60 disabled:cursor-not-allowed disabled:dark:opacity-40">
      <span v-if="!loading">Maak account</span>
      <span v-else>Bezig met registreren…</span>
    </button>
  </form>
</template>
