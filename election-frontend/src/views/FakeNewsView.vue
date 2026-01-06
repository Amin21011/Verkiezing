<script setup lang="ts">
import FakeNewsDetector from '@/components/FakeNewsDetector.vue'
</script>

<template>
  <main class="relative flex justify-center py-16 px-4 sm:px-6 lg:px-8 min-h-[90vh]">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] pointer-events-none"></div>
    <div class="relative z-10 w-full max-w-7xl">
      <FakeNewsDetector />
    </div>
  </main>
</template>
