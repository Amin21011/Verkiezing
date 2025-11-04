<script setup lang="ts">
import { RouterView } from 'vue-router'
import './assets/main.css'

</script>

<template>
  <div class="min-h-screen w-full bg-paper text-ink font-body relative">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')]"></div>
    <RouterView class="relative z-10" />
  </div>
</template>
