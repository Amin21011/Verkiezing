<script setup lang="ts">
import { ref, watch, nextTick, computed } from "vue";
import Chart from "chart.js/auto";
import { useElectionResults } from "@/helpers/ElectionResultsHelper";

const {
  topParties,
  chartLabels,
  chartValues,
  selectedPartyId,
  isLoading,
} = useElectionResults();

const canvasRef = ref<HTMLCanvasElement | null>(null);
let chart: Chart | null = null;

const totalVotes = computed(() => {
  const party = topParties.value.find(p => p.id === selectedPartyId.value);
  return party ? Number(party.voteCount) : 0;
});

watch([chartLabels, chartValues], async ([labels, values]) => {
  await nextTick();
  renderChart(labels, values);
});

function renderChart(labels: string[], values: number[]) {
  if (!canvasRef.value) return;
  if (chart) chart.destroy();

  const ctx = canvasRef.value.getContext("2d");
  if (!ctx) return;

  if (!labels.length || values.every((v) => v === 0)) {
    ctx.clearRect(0, 0, canvasRef.value.width, canvasRef.value.height);
    ctx.font = "16px 'Merriweather'";
    ctx.fillStyle = "#666";
    ctx.fillText("Geen data beschikbaar", 20, 50);
    return;
  }

  const colors = [
    "#b23a48",
    "#d4a373",
    "#8c9a9e",
    "#c1cfa1",
    "#f4ca64",
  ].slice(0, values.length);

  chart = new Chart(ctx, {
    type: "doughnut",
    data: {
      labels,
      datasets: [
        {
          label: "Stemmen",
          data: values,
          backgroundColor: colors,
          borderWidth: 2,
          borderColor: "#fdfcf7", // papierkleur
        },
      ],
    },
    options: {
      cutout: "72%",
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 800, easing: "easeOutQuart" },
      plugins: {
        legend: {
          position: "bottom",
          labels: {
            color: "#1a1a1a",
            font: { family: "'Merriweather', serif", size: 13 },
            usePointStyle: true,
            padding: 14,
          },
        },
        title: {
          display: true,
          text: `Verdeling`,
          color: "#1a1a1a",
          font: {
            family: "'Playfair Display', serif",
            size: 22,
            weight: "bold",
          },
          padding: { bottom: 14 },
        },
        tooltip: {
          backgroundColor: "#fdfcf7",
          titleColor: "#1a1a1a",
          bodyColor: "#1a1a1a",
          borderColor: "#1a1a1a33",
          borderWidth: 1,
          callbacks: {
            label: (ctx) =>
              `${ctx.label}: ${ctx.parsed.toLocaleString()} stemmen`,
          },
        },
      },
    },
  });
}
</script>

<template>
  <div class="max-w-4xl mx-auto bg-paper border border-ink/30 rounded-xl shadow-[4px_4px_0_#1a1a1a]/10 p-8 md:p-10 relative overflow-hidden">
    <div class="absolute inset-0 opacity-[0.08] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"></div>

    <div class="relative z-10 flex flex-col md:flex-row md:items-center md:justify-between gap-6 mb-8">
      <div>
        <h2 class="text-2xl md:text-3xl font-headline font-bold text-ink tracking-tight">
          Verkiezingskandidaten
        </h2>
        <p class="text-sm text-graymain italic">
          Een blik op de topkandidaten per partij.
        </p>
      </div>

      <select v-model="selectedPartyId" class="bg-paper text-ink border-2 border-ink rounded-md px-4 py-2 font-body hover:bg-ink hover:text-paper transition-all duration-200 w-full md:w-auto shadow-press">
        <option v-for="p in topParties" :key="p.id" :value="p.id">
          {{ p.name }}
        </option>
      </select>
    </div>

    <div class="relative z-10 h-[360px] flex items-center justify-center">
      <div v-if="isLoading" class="w-full h-full flex items-center justify-center text-gray-500 italic">
        Gegevens laden...
      </div>
      <canvas v-else ref="canvasRef" class="w-full h-full"></canvas>


      <div v-if="!isLoading" class="absolute text-center" style="top: 50%; left: 50%; transform: translate(-50%, -50%)">
        <p class="text-xs uppercase tracking-wide text-graymain font-semibold mb-1">
          Totaal stemmen
        </p>
        <p class="text-3xl md:text-4xl font-headline font-bold text-ink drop-shadow-sm">
          {{ totalVotes.toLocaleString() }}
        </p>
      </div>
    </div>

    <div class="absolute bottom-0 left-0 w-full h-[4px] bg-gradient-to-r from-[#b23a48] via-[#d4a373] to-[#f4ca64]"></div>
  </div>
</template>

<style scoped>
canvas {
  filter: drop-shadow(0 2px 6px rgba(0, 0, 0, 0.1));
}
</style>
