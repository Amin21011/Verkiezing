<script setup lang="ts">
import GridCard from './GridCard.vue'
import type { Component } from 'vue'

defineProps<{
  cards: { title: string; component: Component; props?: Record<string, never> }[]
}>()
</script>

<template>
  <section class="relative flex flex-col items-center py-16 md:py-20 px-6 md:px-10 bg-retro-paper overflow-hidden border-y-[6px] border-retro-ink/70 shadow-retroSoft">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"></div>

    <header class="relative text-center mb-14">
      <h2 class="font-retroHead text-4xl md:text-5xl font-bold text-retro-ink tracking-tight retro-title">
        Redactionele Sectie
      </h2>
      <p class="text-graymain italic mt-2 text-sm">
        De laatste analyses en opinies.
      </p>
      <div class="h-[3px] w-16 bg-retro-ink/90 mx-auto mt-3 rounded-full"></div>
    </header>

    <div class="relative z-10 grid w-full max-w-7xl gap-10 sm:grid-cols-1 md:grid-cols-2">
      <GridCard v-for="(card, i) in cards" :key="i" :title="card.title" class="h-full">
        <component :is="card.component" v-bind="card.props ?? {}" />
      </GridCard>
    </div>
  </section>
</template>

<style scoped>
.font-retroHead {
  font-family: "Playfair Display", serif;
}
.bg-retro-paper {
  background-color: var(--paper, #fdfcf7);
}
.text-retro-ink {
  color: var(--ink, #1a1a1a);
}
</style>
