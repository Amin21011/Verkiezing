<script setup lang="ts">
import PollView from "@/views/PollView.vue";
import PartijenContent from "@/components/PartijenContent.vue";
import ChartContainer from "@/components/ChartContainer.vue";
import GridSection from "@/components/GridSection.vue";

const tweedeGrid = [
  { title: 'Top Parties', component: PartijenContent },
  { title: 'Jouw Stem', component: PollView },
]
</script>

<template>
  <GridSection :cards="tweedeGrid" />
  <ChartContainer/>
</template>
