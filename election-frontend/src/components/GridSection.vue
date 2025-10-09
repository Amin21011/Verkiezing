<script setup lang="ts">
import GridCard from './GridCard.vue'

defineProps<{
  cards: { title: string; component: typeof GridCard }
  }>()
</script>

<template>
  <section class="grid-section">
    <div class="grid-layout">
      <GridCard
        v-for="(card, index) in cards"
        :key="index"
        :title="card.title"
      >
        <component :is="card.component" />
      </GridCard>
    </div>
  </section>
</template>

<style scoped>
.grid-section {
  width: 100%;
  margin-top: 2rem;
  display: flex;
  justify-content: center;
}

.grid-layout {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
  width: 100%;
  max-width: 1200px;
}

/* Vanaf medium schermen (tablet en groter) -> 2 kolommen */
@media (min-width: 768px) {
  .grid-layout {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
