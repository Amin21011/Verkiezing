<script setup lang="ts">
import { Bar } from "vue-chartjs";
import {
  Chart,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
} from "chart.js";

Chart.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const props = defineProps<{
  data: { party: string; seats: number }[];
}>();
</script>

<template>
  <Bar
    :data="{
      labels: props.data.map((d) => d.party),
      datasets: [
        {
          label: 'Zetels',
          data: props.data.map((d) => d.seats),
        },
      ],
    }"
    :options="{
      responsive: true,
      animation: true,
      plugins: {
        legend: { display: false },
      }
    }"
  />
</template>
