<script setup lang="ts">
import { ref } from 'vue'
import router from '@/router'

const email = ref('')
const password = ref('')
import { login } from "../services/authService";

const handleLogin = async () => {
  try {
    await login(email.value, password.value);
    alert("Succesvol ingelogd!");
    router.push("/");
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
  } catch (err: unknown) {
    alert("Login mislukt.");
  }
};

</script>

<template>
  <div class="text-ink font-body">
    <h2 class="text-3xl font-headline font-bold uppercase mb-6 border-b-2 border-graymain pb-2 text-left">
      Inloggen</h2>

    <form @submit.prevent="handleLogin" class="flex flex-col gap-4 text-left">
      <input v-model="email" type="email" placeholder="E-mail" class="input-field" required />

      <input v-model="password" type="password" placeholder="Wachtwoord" class="input-field" required />

      <button type="submit" class="btn-primary uppercase font-bold tracking-widest mt-4">
        Login</button>
    </form>

    <a href="#" class="block mt-4 text-sm text-graymain hover:text-accent font-semibold underline-offset-2 hover:underline">
      Wachtwoord vergeten?</a>
  </div>
</template>
