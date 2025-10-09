<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen w-screen bg-[#F8F7F3] flex flex-col items-center justify-start overflow-x-hidden overflow-y-auto">
    <main class="w-full max-w-3xl p-6">
      <RouterView />
    </main>
  </div>
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
  overflow-x: hidden;
  overflow-y: hidden;
  background-color: #F8F7F3;
}
</style>
