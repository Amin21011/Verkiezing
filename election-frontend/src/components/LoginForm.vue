<script setup lang="ts">
import { ref } from 'vue'
import router from '@/router'
import { login } from '@/services/authService'
import { showToast } from '@/composables/useFlash'
import ModalOverlay from '@/components/ModalOverlay.vue'
import ForgotPassword from '@/components/ForgotPassword.vue'

const email = ref('')
const password = ref('')
const loading = ref(false)
const showForgotPassword = ref(false)

const handleLogin = async () => {
  loading.value = true

  try {
    await login(email.value.trim(), password.value)
    showToast('Inloggen gelukt!', 'success')
    await router.push('/')
  } catch (err: any) {
    console.error('Login error:', err)

    const message =
      err?.response?.data?.message ||
      'Inloggen mislukt. Controleer je gegevens.'

    showToast(message, 'error')
  } finally {
    loading.value = false
  }
}
</script>
<template>
  <form @submit.prevent="handleLogin" class="relative w-full max-w-md bg-paper shadow-press space-y-6">
    <div class="absolute inset-0 opacity-[0.04] pointer-events-none" style="background-image:url('https://www.transparenttextures.com/patterns/newsprint.png')">
    </div>

    <header class="relative z-10 space-y-2">
      <p class="uppercase tracking-[0.35em] text-[10px] font-mono text-graymain">
        Account
      </p>

      <h2 class="text-3xl font-headline font-black border-b-2 border-ink/40 pb-2">
        Inloggen
      </h2>
    </header>

    <div class="relative z-10 space-y-4">
      <input v-model="email" type="email" placeholder="E-mailadres" class="input-field" required />
      <input v-model="password" type="password" placeholder="Wachtwoord" class="input-field" required />
    </div>

    <button type="submit" :disabled="loading" class="btn-primary w-full uppercase tracking-widest text-sm relative z-10
             dark:bg-[var(--ink)] dark:text-[var(--paper)] dark:border-[var(--border-soft)]
             disabled:opacity-60 disabled:cursor-not-allowed disabled:dark:opacity-40">

      <span v-if="!loading">Login</span>
      <span v-else>Bezig met inloggen…</span>
    </button>

    <button type="button" @click="showForgotPassword = true" class="relative z-10 block mt-5 text-sm font-semibold text-graymain dark:text-[var(--muted)]
         hover:text-accent dark:hover:text-[var(--accent)] underline-offset-2 hover:underline">
      Wachtwoord vergeten?
    </button>
  </form>

  <ModalOverlay v-if="showForgotPassword" @close="showForgotPassword = false">
    <ForgotPassword @success="showForgotPassword = false" />
  </ModalOverlay>
</template>


