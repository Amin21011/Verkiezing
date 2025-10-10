<script setup lang="ts">
import NavBar from './NavBar.vue';
import FooterComp from './FooterComp.vue';
import GridSection from './GridSection.vue';

// const hero = {
//   title: 'VERKIEZINGEN 2025',
//   subtitle: 'Alles wat je moet weten voordat je stemt.',
//   image: '../assets/img/hero.jpg',
//   date: '10 oktober 2025'
// };

import NieuwsContent from './NieuwsContent.vue'
import StatistiekenContent from './StatistiekenContent.vue'
import PartijenContent from './PartijenContent.vue'
import PollView from '../views/PollView.vue'

const eersteGrid = [
  { title: 'Laatste Nieuws', component: NieuwsContent },
  { title: 'Hete Statistieken', component: StatistiekenContent }
];

const tweedeGrid = [
  { title: 'Top Parties', component: PartijenContent },
  { title: 'Poll', component: PollView  }
]
</script>
<template>
  <div class="page-container">

    <NavBar />

<!--    <section class="hero">-->
<!--      <div class="hero-content">-->
<!--        <h2 class="hero-title">{{ hero.title }}</h2>-->
<!--        <p class="hero-subtitle">{{ hero.subtitle }}</p>-->
<!--        <span class="hero-date">{{ hero.date }}</span>-->
<!--      </div>-->
<!--      <div class="hero-image">-->
<!--        <img :src="hero.image" alt="Hero Image">-->
<!--      </div>-->
<!--    </section>-->


    <GridSection :cards="eersteGrid" />
    <GridSection :cards="tweedeGrid" />

    <FooterComp />

  </div>
</template>


<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fdfcf7;
  min-height: 100vh;
  width: 100%;
  font-family: 'Merriweather', serif;
  color: #1c1c1c;
}


@media (min-width: 768px) {
  .grid-layout { grid-template-columns: repeat(2, 1fr); } }

footer { width: 100%; max-width: 1600px; margin-top: 2rem; padding: 1rem; text-align: center; color: #4b5563; }
</style>
