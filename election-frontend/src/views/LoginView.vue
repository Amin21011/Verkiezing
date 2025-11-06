<script setup lang="ts">
import FooterComp from '../components/FooterComp.vue'
import LoginForm from '../components/LoginForm.vue'
import RegisterPrompt from '../components/RegisterPrompt.vue'
import '../assets/main.css'
</script>

<template>
  <div class="min-h-screen w-full bg-paper text-ink font-body flex flex-col relative">
    <section class="w-full bg-graymain flex justify-center">
      <div class="w-full max-w-6xl flex flex-col md:flex-row items-center justify-between py-20 px-6 gap-10">
        <div class="bg-white border-4 border-ink shadow-press p-10 w-full max-w-sm z-10">
          <LoginForm/>
        </div>
        <div class="text-white text-xl font-headline md:w-1/3 text-center z-10">
          <RegisterPrompt/>
        </div>
      </div>
    </section>
    <div class="flex-1 bg-paper"></div>
    <FooterComp/>
  </div>
</template>
