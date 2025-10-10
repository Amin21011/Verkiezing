<script setup lang="ts">
import GridCard from './GridCard.vue';
import type { Component } from 'vue'

defineProps<{
  cards: { title: string; component: Component; props?: Record<string, any> }[];
}>();
</script>

<template>
  <section class="grid-section">
    <div class="grid-layout">
      <GridCard
        v-for="(card, index) in cards"
        :key="index"
        :title="card.title"
      >
        <component :is="card.component" v-bind="card.props ?? {}" />
      </GridCard>
    </div>
  </section>
</template>

<style scoped>
.grid-section {
  width: 100%;
  padding: 2rem 1rem;
  display: flex;
  justify-content: center;
  background-color: #fdfcf7;
}

.grid-layout {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem;
  width: 100%;
  max-width: 1200px;
}

@media (min-width: 768px) {
  .grid-layout {
    grid-template-columns: repeat(2, 1fr);
  }
}

</style>
