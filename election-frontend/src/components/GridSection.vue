<script setup lang="ts">
import GridCard from './GridCard.vue'
import type { Component } from 'vue'

defineProps<{
  cards: { title: string; component: Component; props?: Record<string, never> }[] }>()
</script>

<template>
  <section class="flex flex-col items-center py-12 md:py-16 px-4 md:px-8 bg-paper">
    <div class="grid w-full max-w-7xl gap-8 sm:grid-cols-1 md:grid-cols-3">
      <GridCard v-for="(card, i) in cards" :key="i" :title="card.title" class="h-full">
        <component :is="card.component" v-bind="card.props ?? {}" />
      </GridCard>
    </div>
  </section>
</template>
