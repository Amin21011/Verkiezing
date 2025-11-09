<script setup lang="ts">
import HeroSection from '@/components/HeroSection.vue'
import ArticleSection from '@/components/ArticleSection.vue'
import GridSection from '@/components/GridSection.vue'
import PartijenContent from '@/components/PartijenContent.vue'
import PollView from '@/views/PollView.vue'
import ChartContainer from '@/components/ChartContainer.vue'
import BannerSection from '@/components/BannerSection.vue'

const tweedeGrid = [
  { title: 'Top Parties', component: PartijenContent },
  { title: 'Jouw Stem', component: PollView },
]
</script>

<template>
  <BannerSection/>
  <ArticleSection/>
  <HeroSection/>
  <GridSection :cards="tweedeGrid" />
  <ChartContainer/>
</template>
