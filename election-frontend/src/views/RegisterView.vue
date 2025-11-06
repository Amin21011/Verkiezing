<script setup lang="ts">
import RegisterForm from '../components/RegisterForm.vue'
import LoginPrompt from '../components/LoginPrompt.vue'
</script>


<template>
    <div class="flex-grow flex flex-col md:flex-row justify-center items-center gap-12 py-20 px-6 relative z-10 max-w-7xl mx-auto">
      <div class="flex justify-center w-full md:w-1/2">
        <RegisterForm /> </div>

      <div class="text-white text-xl font-headline md:w-1/3 text-center z-10">
        <LoginPrompt /> </div>
    </div>
</template>

