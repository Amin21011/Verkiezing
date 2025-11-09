<script setup lang="ts">
import CandidateChart from '@/components/CandidateChart.vue'
</script>

<template>
  <section class="relative bg-retro-paper py-20 px-6 md:px-12 overflow-hidden">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"></div>

    <header class="relative text-center mb-14 flex flex-col items-center justify-center">
      <h2 class="font-retroHead text-5xl md:text-6xl font-bold text-retro-ink tracking-tight retro-title leading-tight">
        Verkiezingsanalyse 2025 🔍
      </h2>
      <p class="text-graymain italic mt-3">
        De verdeling van stemmen over de topkandidaten.
      </p>
      <div class="h-[4px] w-32 bg-gradient-to-r from-retro-red via-retro-gold to-retro-blue mt-5 rounded-full"></div>
    </header>

    <div class="relative max-w-5xl mx-auto bg-white/80 border-[3px] border-retro-ink rounded-xl shadow-[6px_6px_0_var(--ink)] p-10 md:p-14 transition-all hover:shadow-[-8px_8px_0_var(--ink)]">
      <CandidateChart />
    </div>
  </section>
</template>

<style scoped>
.font-retroHead {
  font-family: 'Playfair Display', serif;
}
.bg-retro-paper {
  background-color: var(--paper, #fdfcf7);
}
.text-retro-ink {
  color: var(--ink, #1a1a1a);
}
</style>
