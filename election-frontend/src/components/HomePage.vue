<script setup lang="ts">
import NavBar from './NavBar.vue'
import FooterComp from './FooterComp.vue'
import GridSection from './GridSection.vue'

import NieuwsContent from './NieuwsContent.vue'
import StatistiekenContent from './StatistiekenContent.vue'
import PartijenContent from './PartijenContent.vue'
import PollContent from './PollContent.vue'

const eersteGrid = [
  { title: 'Latest News', component: NieuwsContent },
  { title: 'Hot Data', component: StatistiekenContent }
]

const tweedeGrid = [
  { title: 'Top Parties', component: PartijenContent },
  { title: 'Quiz', component: PollContent }
]
</script>

<template>
  <div class="page-container">
    <div class="content-container">
      <NavBar />
      <GridSection :cards="eersteGrid" />
      <GridSection :cards="tweedeGrid" />

      <FooterComp />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  justify-content: center;
  background-color: #f3f4f6; /* bg-gray-100 */
  min-height: 100vh;
  padding: 2.5rem 1rem;
}

.content-container {
  width: 100%;
  max-width: 1600px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

</style>
