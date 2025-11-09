<script setup lang="ts">
import { ref } from 'vue'
import { login, register } from '../services/authService'
import { showToast } from '@/helpers/useFlash.ts'
import { useRouter } from 'vue-router'

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const router = useRouter()

const handleSubmit = async () => {
  if (password.value !== confirmPassword.value) {
    showToast('Wachtwoorden komen niet overeen!', 'error')
    return
  }

  try {
    await register(name.value, email.value, password.value)
    showToast('Account succesvol aangemaakt!', 'success')

    const loginData = await login(email.value, password.value)
    showToast(`Welkom ${loginData.displayName || name.value}!`, 'success')

    await router.push('/')
  } catch (error: unknown) {
    console.error('Fout bij registratie of login:', error)
    showToast('Fout bij registratie', 'error')
  }
}
</script>

<template>
  <form @submit.prevent="handleSubmit" class="w-full max-w-md bg-white border-4 border-ink shadow-press p-8 flex flex-col gap-4">
    <h2 class="text-3xl font-headline font-bold uppercase text-center mb-6 border-b-2 border-graymain pb-2 text-left">
      Registreer</h2>

    <input v-model="name" type="text" placeholder="Naam" class="input-field" required />
    <input v-model="email" type="email" placeholder="E-mail" class="input-field" required />
    <input v-model="password" type="password" placeholder="Wachtwoord" class="input-field" required />
    <input v-model="confirmPassword" type="password" placeholder="Bevestig wachtwoord" class="input-field" required />

    <button type="submit" class="btn-primary w-full text-center mt-2">Maak Account</button>
  </form>
</template>
