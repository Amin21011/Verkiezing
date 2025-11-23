<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import mapSvg from '@/assets/img/netherlands.svg?raw'
import { partyColors } from '@/assets/partyColors.ts'

interface ProvinceResult {
  provinceNaam: string
  stemmenPerPartij: Record<string, number>
}

const provinces = ref<ProvinceResult[]>([])
const selectedProvince = ref<ProvinceResult | null>(null)
const mapContainer = ref<HTMLElement | null>(null)

const selectedYear = ref(2025)
const API_URL = `${import.meta.env.VITE_API_URL}/provinces/results`

const sortedVotes = computed(() => {
  if (!selectedProvince.value) return []

  const votes = selectedProvince.value.stemmenPerPartij
  const total = Object.values(votes).reduce((a, b) => a + b, 0)

  return Object.entries(votes)
    .map(([party, count]) => {
      const percentage = total > 0 ? (count / total) * 100 : 0
      return { party, count, percentage }
    })
    .sort((a, b) => b.count - a.count)
})

async function loadData() {
  try {
    selectedProvince.value = null

    const res = await fetch(`${API_URL}/${selectedYear.value}`)
    provinces.value = await res.json()
    if (mapContainer.value) {
      mapContainer.value.innerHTML = mapSvg
      highlightProvinces()
    }
  } catch (err) {
    console.error('Fout bij ophalen:', err)
  }
}
onMounted(() => {
  loadData()
})

function onYearChange(event: Event) {
  selectedYear.value = Number((event.target as HTMLSelectElement).value)
  loadData()
}

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
      <select id="year" class="border p-2 rounded" @change="onYearChange">
        <option value="2017">2017</option>
        <option value="2021">2021</option>
        <option value="2023">2023</option>
        <option value="2025" selected>2025</option>
      </select>
    </div>

    <!-- Container voor map + resultaten -->
    <div class="flex flex-row items-start w-full max-w-6xl gap-6">

      <!-- Map container -->
      <div
        ref="mapContainer"
        class="flex-1 border border-gray-400 overflow-hidden rounded shadow-lg bg-white p-4 md:p-6 h-[800px]"
      ></div>

      <div
        v-if="selectedProvince"
        class="flex-1 p-5 border rounded bg-white shadow h-[800px] overflow-y-auto"
      >
        <h2 class="font-bold text-lg mb-4">{{ selectedProvince.provinceNaam }}</h2>

        <div class="relative w-full border-l border-gray-300">

          <div class="absolute -bottom-6 left-0 right-0 flex justify-between text-xs text-gray-500">
            <span v-for="n in 9" :key="n">{{ n * 2 }}%</span>
          </div>

          <div class="absolute top-0 bottom-0 left-0 right-0 flex">
            <div
              v-for="n in 9"
              :key="n"
              class="border-r border-gray-200"
              :style="{ width: '11.11%' }"
            ></div>
          </div>

          <div class="space-y-2 relative">
            <div
              v-for="item in sortedVotes"
              :key="item.party"
              class="flex items-center gap-2"
            >
              <span
                class="text-sm font-bold text-white px-2 py-1 rounded block"
                style="width: 120px"
                :style="{ backgroundColor: partyColors[item.party] || '#000' }"
              >
                {{ item.party }}
              </span>

              <div class="flex-1 h-6 bg-gray-200 rounded">
                <div
                  class="h-6 rounded transition-all"
                  :style="{
                    width: item.percentage + '%',
                    backgroundColor: partyColors[item.party] || '#000'
                  }"
                ></div>
              </div>
            </div>
          </div>
        </div>
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
