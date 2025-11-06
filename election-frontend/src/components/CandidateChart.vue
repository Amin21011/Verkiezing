<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import Chart from 'chart.js/auto'

const props = defineProps<{
  title: string,
  labels: string[],
  values: number[]
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
let chart: Chart | null = null

watch(() => props.values, () => renderChart())

function renderChart() {
  if (!canvasRef.value) return
  if (chart) chart.destroy()

  chart = new Chart(canvasRef.value, {
    type: 'bar',
    data: {
      labels: props.labels,
      datasets: [{
        label: 'Aantal stemmen',
        data: props.values
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { display: false }
      }
    }
  })
}

onMounted(renderChart)
</script>

<template>
  <div class="w-full">
    <canvas ref="canvasRef"></canvas>
  </div>
</template>
