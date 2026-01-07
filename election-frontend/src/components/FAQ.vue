<script setup lang="ts">
import { ref } from 'vue'

interface FAQ {
  question: string
  answer: string
}

const faqs = ref<FAQ[]>([
  {
    question: 'Hoe worden de verkiezingsdata verzameld?',
    answer:
      'De data is gebaseerd op officiële openbare verkiezingsbestanden (EML/XML) en wordt automatisch verwerkt en geanalyseerd.'
  },
  {
    question: 'Is dit platform onafhankelijk?',
    answer:
      'Ja. Het platform is volledig onafhankelijk en heeft geen politieke voorkeur of affiliatie.'
  },
  {
    question: 'Hoe betrouwbaar zijn de analyses?',
    answer:
      'Alle analyses zijn reproduceerbaar, transparant en gebaseerd op controleerbare datasets.'
  },
  {
    question: 'Moet ik een account hebben?',
    answer:
      'Nee. De meeste functies zijn vrij toegankelijk. Voor persoonlijke dashboards is een account nodig.'
  }
])

const openIndex = ref<number | null>(null)

const toggle = (index: number) => {
  openIndex.value = openIndex.value === index ? null : index
}
</script>

<template>
  <section class="bg-paper dark:bg-black/40 border-2 border-ink/40 shadow-press p-8 md:p-10 space-y-8">
    <header class="border-b-2 border-ink/30 pb-4">
      <h2 class="text-3xl font-serif font-bold tracking-tight">
        Veelgestelde vragen
      </h2>
      <p class="mt-2 text-sm italic text-graymain dark:text-gray-400">
        Antwoorden op de meest voorkomende vragen over het platform
      </p>
    </header>

    <ul class="divide-y divide-ink/20">
      <li v-for="(faq, index) in faqs" :key="index" class="py-4">

        <button @click="toggle(index)" class="w-full text-left flex justify-between items-center gap-4">
          <span class="font-serif text-lg">
            {{ faq.question }}
          </span>

          <span class="text-xl font-mono transition-transform duration-200" :class="{ 'rotate-45': openIndex === index }">
            +
          </span>
        </button>

        <div v-if="openIndex === index" class="mt-3 pl-1 text-graymain dark:text-gray-300 leading-relaxed">
          {{ faq.answer }}
        </div>
      </li>
    </ul>
  </section>
</template>
