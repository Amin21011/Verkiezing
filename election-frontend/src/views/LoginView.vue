<script setup lang="ts">
import LoginForm from '../components/LoginForm.vue'
import RegisterPrompt from '../components/RegisterPrompt.vue'
import '../assets/main.css'
</script>

<template>
    <section class="w-full bg-graymain flex justify-center">
      <div class="w-full max-w-6xl flex flex-col md:flex-row items-center justify-between py-20 px-6 gap-10">
        <div class="bg-white border-4 border-ink shadow-press p-10 w-full max-w-sm z-10">
          <LoginForm/></div>

        <div class="text-white text-xl font-headline md:w-1/3 text-center z-10">
          <RegisterPrompt/></div>
      </div>
    </section>
</template>
