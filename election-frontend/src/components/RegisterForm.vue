<script setup lang="ts">
import { ref } from "vue"
import { register } from "../services/authService"

const name = ref("")
const email = ref("")
const password = ref("")
const confirmPassword = ref("")

const handleSubmit = async () => {
  if (password.value !== confirmPassword.value) {
    return alert("Wachtwoorden komen niet overeen!")
  }

  try {
    await register(name.value, email.value, password.value)
    alert("Account aangemaakt! Je kunt nu inloggen.")
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
  } catch (error: unknown) {
    alert("Fout bij registratie.")
  }
}

</script>

<template>
  <form @submit.prevent="handleSubmit" class="w-full max-w-md bg-white border-4 border-ink shadow-press p-8 flex flex-col gap-4">
    <h2 class="text-3xl font-headline font-bold uppercase text-center mb-6 border-b-2 border-graymain pb-2 text-left">Registreer</h2>

    <input v-model="name" type="text" placeholder="Naam" class="input-field" required />
    <input v-model="email" type="email" placeholder="E-mail" class="input-field" required />
    <input v-model="password" type="password" placeholder="Wachtwoord" class="input-field" required />
    <input v-model="confirmPassword" type="password" placeholder="Bevestig wachtwoord" class="input-field" required />

    <button type="submit" class="btn-primary w-full text-center mt-2">
      Maak Account</button>
  </form>
</template>
