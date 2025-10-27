<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()
const goToLogin = () => router.push('/login')
</script>

<template>
  <div class="flex flex-col items-center justify-center p-8 text-center text-black">
    <p class="mb-4 text-lg font-medium">Heb je al een account?</p>
    <button class="bg-gray-300 text-ink px-5 py-2 rounded-md font-medium hover:bg-gray-200 transition" @click="goToLogin">
      Log in
    </button>
  </div>
</template>
