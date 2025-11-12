<script setup lang="ts">
import { onMounted, ref } from 'vue'
import mapSvg from '@/assets/img/netherlands.svg?raw'

interface ProvinceResult {
  provinceNaam: string
  stemmenPerPartij: Record<string, number>
}

const provinces = ref<ProvinceResult[]>([])
const selectedProvince = ref<ProvinceResult | null>(null)
const mapContainer = ref<HTMLElement | null>(null)
const API_URL = `${import.meta.env.VITE_API_URL}/provinces/results`;

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
    <!-- Map container -->
    <div
      ref="mapContainer"
      class="relative w-full max-w-5xl border border-gray-400 overflow-hidden rounded shadow-lg bg-white"
    ></div>

    <!-- Stemmen overzicht alleen tonen als er een provincie is geselecteerd -->
    <div v-if="selectedProvince" class="mt-6 p-4 border rounded bg-white shadow w-96">
      <h2 class="font-bold text-lg mb-2">{{ selectedProvince.provinceNaam }} - Stemmen</h2>
      <ul class="list-disc list-inside">
        <li v-for="(votes, party) in selectedProvince.stemmenPerPartij" :key="party">
          {{ party }}: {{ votes }}
        </li>
      </ul>
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
