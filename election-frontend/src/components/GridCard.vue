<script setup lang="ts">
defineProps<{ title: string }>();
</script>

<template>
  <div class="relative bg-retro-paper border-[3px] border-retro-ink p-8 rounded-none shadow-[4px_4px_0_var(--ink)] hover:shadow-[-8px_8px_0_var(--ink)] transition-all duration-300 flex flex-col overflow-hidden">
    <div class="absolute inset-0 opacity-[0.06] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"
    ></div>

    <h3 class="relative font-retroHead text-3xl font-bold text-retro-ink tracking-tight border-b-[3px] border-retro-ink/40 pb-3 mb-5 leading-tight">
      {{ title }}
    </h3>

    <div class="relative flex-grow font-retroBody text-[15px] leading-relaxed text-[#333] tracking-wide">
      <slot></slot>
    </div>

    <div class="absolute bottom-0 left-0 w-full h-[4px] bg-gradient-to-r from-retro-red via-retro-gold to-retro-blue opacity-80"></div>
  </div>
</template>

<style scoped>
.font-retroHead {
  font-family: "Playfair Display", serif;
}
.font-retroBody {
  font-family: "Merriweather", serif;
}
.bg-retro-paper {
  background-color: var(--paper, #fdfcf7);
}
.text-retro-ink {
  color: var(--ink, #1a1a1a);
}
</style>
