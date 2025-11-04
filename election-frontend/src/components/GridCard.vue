<script setup lang="ts">
defineProps<{ title: string }>();
</script>

<template>
  <div class="bg-white border-4 border-ink p-6 rounded-none shadow-press hover:shadow-[-8px_8px_0_#1c1c1c] transition-all duration-300 flex flex-col">
    <h3 class="font-headline text-2xl font-bold text-ink border-b-2 border-graymain pb-2 mb-4">
      {{ title }}
    </h3>
    <div class="flex-grow font-body text-graymain text-base leading-relaxed">
      <slot></slot>
    </div>
  </div>
</template>
