<template>
  <div class="grid-card">
    <h2 class="card-title">{{ title }}</h2>
    <div class="card-content">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ title: string }>()
</script>

<style scoped>
.grid-card {
  border: 2px solid #000;
  background-color: #fff;
  border-radius: 0.5rem;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.card-title {
  font-size: 1rem;
  font-weight: 600;
  color: #181818;
  text-align: center;
  border-bottom: 1px solid #000;
  padding: 0.5rem;
  background-color: #f9fafb;
}

.card-content {
  padding: 1rem;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4b5563;
}
</style>
