<script setup lang="ts">
import ArticleGridCard from './ArticleGridCard.vue'

const featuredArticle = {
  title: 'Generatie Z toch meer geïnteresseerd in de politiek dan de meeste tot nu toe dachten',
  summary:
    'Jongeren keren zich massaal naar de stembus. Nu nog hopen dat dit het verschil gaat maken wat Nederland nodig had.',
  date: '10 okt 2025',
  category: 'Algemeen',
  image: '/src/assets/img/cover.webp'
}

const subArticles = [
  {
    title: 'Nieuwe partij breekt door bij jongeren',
    summary: 'Steeds meer jongeren sluiten zich aan bij progressieve bewegingen.',
    date: '9 okt 2025',
    category: 'Politiek',
    image: '/src/assets/img/hero.png'
  },
  {
    title: 'Quiz: Hoe goed ken jij de top-partijen?',
    summary: 'Test je kennis en ontdek welke partij het meest bij jou past.',
    date: '9 okt 2025',
    category: 'Interactief',
    image: '/src/assets/img/hero.png'
  }
]
</script>

<template>
  <section class="section-wrapper flex flex-col items-center py-16 px-4 md:px-8 bg-paper">
    <div class="text-center mb-12">
      <span class="font-mono uppercase tracking-widest text-graymain text-sm border-b-4 border-accent pb-1">Actueel Deze Week</span>
      <h2 class="mt-4 font-headline text-5xl md:text-6xl font-extrabold uppercase tracking-tight text-ink">De Stem van een Nieuwe Generatie</h2>
      <p class="subtitle mt-4 max-w-2xl mx-auto">Ontdek de verhalen, cijfers en bewegingen die de verkiezingen van 2025 vormen.</p>
    </div>

    <div class="relative w-full max-w-7xl mb-16 bg-white border-4 border-ink shadow-press hover:shadow-[-10px_10px_0_#1c1c1c] transition-all duration-300 overflow-hidden">
      <img :src="featuredArticle.image" alt="Hoofdartikel afbeelding" class="w-full h-[420px] object-cover grayscale hover:grayscale-0 transition duration-700" />
      <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>

        <div class="absolute bottom-0 p-8 text-white">
          <p class="font-mono text-xs uppercase tracking-widest bg-accent text-ink inline-block px-3 py-1 rounded-sm mb-3">
            {{ featuredArticle.category }}</p>
          <h3 class="text-4xl md:text-5xl font-headline font-extrabold mb-2 leading-tight text-white">
            {{ featuredArticle.title }}</h3>
          <p class="font-body text-lg text-gray-100 max-w-2xl">{{ featuredArticle.summary }}</p>
          <span class="block text-sm mt-2 text-gray-300">{{ featuredArticle.date }}</span>
        </div>
      </div>

      <div class="grid w-full max-w-7xl gap-8 sm:grid-cols-1 md:grid-cols-2 xl:grid-cols-3">
        <ArticleGridCard v-for="(article, i) in subArticles" :key="i" v-bind="article" />
      </div>
      <div class="w-full h-[6px] bg-gradient-to-r from-accent via-yellow-400 to-blue-500 mt-16"></div>
  </section>
</template>
