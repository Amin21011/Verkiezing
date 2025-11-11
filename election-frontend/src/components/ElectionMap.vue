<script setup lang="ts">
import mapSvg from '@/assets/img/netherlands.svg?raw'


</script>

<template>
  <section class="section-wrapper flex flex-col items-center py-16 px-4 md:px-8 bg-paper">
    <div class="relative w-full max-w-7xl mb-16 bg-white border-4 border-ink shadow-press overflow-hidden">
      <div v-html="mapSvg" class="w-full"></div>

      <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent pointer-events-none"></div>
    </div>
  </section>
</template>

<style>
svg path {
  cursor: pointer;
  transition: fill 0.2s;
}

svg path:hover {
  fill: red;
}
</style>
