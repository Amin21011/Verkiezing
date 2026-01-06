<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { type DailyFact, getDailyFacts } from '@/services/FactService'

const facts = ref<DailyFact[] | null>(null)
const loading = ref(true)
const router = useRouter()

onMounted(async () => {
  try {
    facts.value = await getDailyFacts()
  } catch {
    facts.value = null
  } finally {
    loading.value = false
  }
})

const goTo = (link: string) => router.push(link)
</script>

<template>
  <section class="relative max-w-7xl mx-auto my-24 px-6 md:px-10 py-14 bg-paper text-ink dark:bg-[var(--paper)] dark:text-[var(--ink)] border-y-4 border-ink dark:border-[var(--ink)] overflow-hidden">
    <div class="absolute inset-0 pointer-events-none opacity-[0.04]" style="background-image:url('https://www.transparenttextures.com/patterns/newsprint.png')"></div>

    <header class="mb-10 text-center">
      <p class="text-[10px] uppercase tracking-[0.4em] font-bold opacity-70">
        Redactioneel — Binnenland </p>

      <h2 class="font-['Playfair_Display'] text-2xl md:text-4xl font-black tracking-tight mt-3">
        DAGELIJKSE FEITEN </h2>

      <div class="mt-4 flex justify-center items-center gap-3 text-xs opacity-40">
        <span>✦</span><span>✦</span><span>✦</span>
      </div>
    </header>

    <p v-if="loading" class="italic font-serif text-center opacity-60">
      De redactie is nog bezig…</p>

    <p v-else-if="!facts" class="italic font-serif text-center opacity-60">
      Vandaag geen feiten gedrukt.</p>

    <div v-else class="relative grid md:grid-cols-3 gap-10 md:gap-6 text-sm leading-relaxed">
      <article v-for="(fact, index) in facts" :key="fact.title" @click="goTo(fact.link)" class="cursor-pointer font-serif relative px-4" :class="{
      'md:border-r md:border-ink/60 dark:md:border-[var(--ink)]/40': index < facts.length - 1,
      'md:-translate-y-1': index === 1,
      'md:translate-y-1': index === 2 }">
        <h3 class="font-bold uppercase text-[21px] tracking-tight leading-snug mb-3">
          {{ fact.title }}
        </h3>

        <p class="italic opacity-80 mb-4">
          {{ fact.description }}
        </p>

        <p class="font-mono text-[12px] uppercase tracking-widest opacity-70">
          {{ fact.value }}
        </p>

        <div class="mt-6 text-xs opacity-30">
          — ✦ —
        </div>
      </article>
    </div>

  </section>
</template>
