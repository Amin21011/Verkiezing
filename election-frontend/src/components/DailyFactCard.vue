<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getDailyFacts } from '@/services/FactService.ts'

interface DailyFact {
  type: 'region' | 'candidate' | 'party'
  title: string
  description: string
  value: string
  link: string
}

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

function goTo(link: string) {
  router.push(link)
}
</script>

<template>
  <section class="relative max-w-7xl mx-auto my-20 px-6 md:px-10 py-12 bg-retro-paper border-[4px] border-retro-ink shadow-press overflow-hidden">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] pointer-events-none"></div>
    <header class="relative text-center mb-10">
      <p class="uppercase tracking-widest font-mono text-sm text-gray-500">
        Daily Election Fact
      </p>
      <h2 class="font-retroHead text-4xl md:text-5xl font-extrabold text-retro-ink mt-2">
        Wist je dit al?
      </h2>
      <div class="h-[3px] w-20 bg-retro-ink mx-auto mt-4 rounded-full"></div>
    </header>

    <div v-if="loading" class="text-center italic text-graymain">
      De redactie verzamelt feiten…
    </div>

    <div v-else-if="!facts" class="text-center italic text-graymain">
      Geen feiten beschikbaar vandaag.
    </div>

    <div v-else class="grid gap-8 md:grid-cols-3">
      <article v-for="fact in facts" :key="fact.title" @click="goTo(fact.link)" class="group cursor-pointer bg-white border-2 border-retro-ink p-6 shadow-press hover:shadow-[-8px_8px_0_var(--ink)] transition-all duration-300">
        <h3 class="font-retroHead text-xl font-bold mb-3 text-retro-ink group-hover:underline">
          {{ fact.title }}
        </h3>

        <p class="font-retroBody italic text-graymain mb-4">
          {{ fact.description }}
        </p>

        <p class="font-mono text-sm uppercase tracking-widest text-retro-ink">
          {{ fact.value }}
        </p>
        <div class="mt-4 h-[2px] w-full bg-gradient-to-r from-retro-red via-retro-gold to-retro-blue"></div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.font-retroHead {
  font-family: 'Playfair Display', serif;
}
.font-retroBody {
  font-family: 'Merriweather', serif;
}
.bg-retro-paper {
  background-color: var(--paper, #fdfcf7);
}
.text-retro-ink {
  color: var(--ink, #1a1a1a);
}
.shadow-press {
  box-shadow: 6px 6px 0 var(--ink);
}
</style>
