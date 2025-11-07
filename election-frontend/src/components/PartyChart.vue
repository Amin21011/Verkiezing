<script setup lang="ts">
import { ref, onMounted } from "vue"
import Chart from "chart.js/auto"
import { useElectionResults } from "../helpers/ElectionResultsHelper.ts"

const { topParties, loadTopParties } = useElectionResults()
const canvasRef = ref<HTMLCanvasElement | null>(null)
let chart: Chart | null = null

onMounted(async () => {
  await loadTopParties(5)
  renderChart()
})

function renderChart() {
  if (!canvasRef.value) return
  if (chart) chart.destroy()

  const labels = topParties.value.map(p => p.name)
  const votes = topParties.value.map(p => p.voteCount)

  const ctx = canvasRef.value.getContext("2d")!
  const gradient = ctx.createLinearGradient(0, 0, 0, 400)
  gradient.addColorStop(0, "#FF00FF")
  gradient.addColorStop(1, "#00FFFF")

  chart = new Chart(ctx, {
    type: "bar",
    data: {
      labels,
      datasets: [
        {
          label: "Totaal aantal stemmen",
          data: votes,
          backgroundColor: gradient,
          borderRadius: 10,
          borderWidth: 0,
          hoverBackgroundColor: "#F5F5F5",
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false,
        },
        title: {
          display: true,
          text: "Top Partijen",
          color: "#E0E0E0",
          font: { size: 20, family: "'Orbitron', sans-serif" },
        },
      },
      scales: {
        x: {
          ticks: {
            color: "#C0C0C0",
            font: { family: "'Space Grotesk', sans-serif" },
          },
          grid: {
            color: "rgba(255,255,255,0.05)",
          },
        },
        y: {
          beginAtZero: true,
          ticks: {
            color: "#AAAAAA",
            font: { family: "'Space Grotesk', sans-serif" },
          },
          grid: {
            color: "rgba(255,255,255,0.1)",
          },
        },
      },
    },
  })
}
</script>

<template>
  <div
    class="relative w-full h-[400px] bg-[#101020] rounded-2xl shadow-xl p-6 flex flex-col justify-center items-center border border-[#303040] hover:border-[#00FFFF]/50 transition-all duration-300">
    <canvas ref="canvasRef" class="w-full"></canvas>
  </div>
</template>

<style scoped>
canvas {
  filter: drop-shadow(0 0 6px #00ffff55);
}
</style>
