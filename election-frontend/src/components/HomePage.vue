<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
import HeroSection from '@/components/HeroSection.vue'
import ArticleSection from '@/components/ArticleSection.vue'
import GridSection from '@/components/GridSection.vue'
import PartijenContent from '@/components/PartijenContent.vue'
import PollView from '@/views/PollView.vue'
import FooterComp from '@/components/FooterComp.vue'

const tweedeGrid = [
  { title: 'Top Parties', component: PartijenContent },
  { title: 'Jouw Stem', component: PollView }
]
</script>

<template>
    <NavBar/>
    <HeroSection/>
    <ArticleSection/>
    <GridSection :cards="tweedeGrid" />
    <FooterComp/>
</template>
