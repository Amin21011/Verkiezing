<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import mapSvg from '@/assets/img/netherlands.svg?raw'

interface ProvinceResult {
  provinceNaam: string
  stemmenPerPartij: Record<string, number>
}

const provinces = ref<ProvinceResult[]>([])
const selectedProvince = ref<ProvinceResult | null>(null)
const mapContainer = ref<HTMLElement | null>(null)
const API_URL = `${import.meta.env.VITE_API_URL}/provinces/results`;

const sortedVotes = computed(() => {
  if (!selectedProvince.value) return []
  return Object.entries(selectedProvince.value.stemmenPerPartij)
    .sort((a, b) => b[1] - a[1])
})
onMounted(async () => {
  try {
    const res = await fetch(API_URL)
    provinces.value = await res.json()
    if (mapContainer.value) {
      mapContainer.value.innerHTML = mapSvg
      highlightProvinces()
    }
  } catch (err) {
    console.error('Fout bij ophalen:', err)
  }
})

function highlightProvinces() {
  setTimeout(() => {
    provinces.value.forEach((p) => {
      const el = Array.from(document.querySelectorAll('svg path')).find(
        (path) => path.getAttribute('title') === p.provinceNaam
      )
      if (!el || !(el instanceof SVGPathElement)) return

      const path = el

      // Click effect
      path.addEventListener('click', () => {
        selectedProvince.value = p

        // Reset alle paden naar standaardkleur
        document.querySelectorAll('svg path').forEach((other) => {
          if (other instanceof SVGPathElement) {
            other.style.fill = 'gray'
          }
        })
        // Kleur actieve provincie
        path.style.fill = '#0056b3'
      })
    })
  }, 200)
}
</script>

<template>
  <section class="flex flex-col items-center py-8">
    <!-- Jaar selectie -->
    <div class="w-full max-w-6xl mb-4">
      <label for="year" class="font-semibold mr-2">Kies jaar:</label>
      <select id="year" class="border p-2 rounded">
        <option value="2017">2017</option>
        <option value="2021">2021</option>
        <option value="2023">2023</option>
      </select>
    </div>

    <!-- Container voor map + resultaten -->
    <div class="flex flex-col md:flex-row items-start w-full max-w-6xl gap-6">

      <!-- Map container -->
      <div
        ref="mapContainer"
        class="flex-1 border border-gray-400 overflow-hidden rounded shadow-lg bg-white p-4 md:p-6 h-[600px] md:h-[800px]"
      ></div>

      <!-- Stemmen overzicht alleen tonen als er een provincie is geselecteerd -->
      <div v-if="selectedProvince" class="w-full md:w-96 p-5 border rounded bg-white shadow">
        <h2 class="font-bold text-lg mb-2">{{ selectedProvince.provinceNaam }} - Stemmen</h2>
        <ul class="list-disc list-inside">
          <li v-for="([party, votes]) in sortedVotes" :key="party">
            {{ party }}: {{ votes }} stemmen
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<style>
/* Maak de map responsive */
#mapContainer svg {
  width: 100%;
  height: auto;
  max-width: 100%;
}

/* Basis styling van provincies */
svg path {
  fill: gray;
  cursor: pointer;
  stroke: black;
  stroke-width: 1.2;
  transition: fill 0.2s, stroke 0.3s;
}
</style>
