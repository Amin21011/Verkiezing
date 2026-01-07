<script setup lang="ts">
import CandidateChart from '@/components/CandidateChart.vue'
</script>

<template>
  <section class="relative bg-retro-paper py-20 px-6 md:px-12 overflow-hidden">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"></div>
    <header class="relative text-center mb-16">
      <h2 class="font-retroHead text-5xl md:text-6xl font-bold text-retro-ink tracking-tight retro-title">
        Verkiezingsanalyse 🔍
      </h2>
      <p class="text-graymain italic mt-3">
        Historische trends en verdeling van stemmen.
      </p>
      <div class="h-[4px] w-40 bg-gradient-to-r from-retro-red via-retro-gold to-retro-blue mt-5 rounded-full"></div>
    </header>

    <div class="relative max-w-5xl mx-auto bg-white/80 border-[3px] border-retro-ink rounded-xl shadow-[6px_6px_0_var(--ink)] p-10 md:p-14">
      <CandidateChart />
    </div>
  </section>
</template>
