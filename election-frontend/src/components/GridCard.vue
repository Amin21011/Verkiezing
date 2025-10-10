<script setup lang="ts">
defineProps<{
  title: string
}>()
</script>

<template>
  <div class="grid-card">
    <h3 class="grid-card-title">{{ title }}</h3>
    <div class="grid-card-content">
      <slot></slot>
    </div>
  </div>
</template>

<style scoped>
.grid-card {
  background: linear-gradient(145deg, #ffffff, #fffbf0);
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
  padding: 1.5rem;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  flex-direction: column;
}

.grid-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 22px rgba(0, 0, 0, 0.12);
}

.grid-card-title {
  font-family: 'Playfair Display', serif;
  font-weight: 700;
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #1c1c1c;
  border-bottom: 2px solid #e0d8c0;
  padding-bottom: 0.5rem;
}

.grid-card-content {
  font-family: 'Merriweather', serif;
  font-size: 0.95rem;
  line-height: 1.6;
  color: #333;
}

.grid-card-content a {
  color: darkslateblue;
  text-decoration: underline;
  transition: color 0.2s ease;
}

.grid-card-content a:hover {
  color: #1c1c1c;
}
</style>
