<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import mapSvg from '@/assets/img/netherlands.svg?raw'
import { partyColors } from '@/assets/partyColors.ts'
import { useProvinceCompare } from '@/services/useProvinceCompare.ts'

interface ProvinceResult {
  provinceNaam: string
  stemmenPerPartij: Record<string, number>
}

const {
  compareResults,
  selectedProvinces,
  toggleProvince,
  isComparing,
  setYear,
  sortedVotes: sortedCompareVotes,
} = useProvinceCompare()

const provinces = ref<ProvinceResult[]>([])
const selectedProvince = ref<ProvinceResult | null>(null)
const mapContainer = ref<HTMLElement | null>(null)

const selectedYear = ref(2025)

const constituencyPositions: Record<string, { x: number; y: number }> = {
  Amsterdam: { x: 650, y: 475 },

  Den_Helder: { x: 615, y: 330 },
  Haarlem: { x: 600, y: 435 },
  Leiden: { x: 630, y: 510 },
  "s-Gravenhage": { x: 610, y: 540 },
  Rotterdam: { x: 590, y: 600 },
  Dordrecht: { x: 620, y: 620 },

  Utrecht: { x: 678, y: 550 },
  Lelystad: { x: 730, y: 450 },
  Zwolle: { x: 830, y: 440 },

  Arnhem: { x: 780, y: 550 },
  Nijmegen: { x: 770, y: 600 },

  "s-Hertogenbosch": { x: 700, y: 600 },
  Tilburg: { x: 630, y: 680 },
  Middelburg: { x: 442, y: 705 },
  Maastricht: { x: 770, y: 875 },
  Assen: { x: 760, y: 330 },
  Groningen: { x: 880, y: 250 },
  Leeuwarden: { x: 775, y: 260 },
};




const currentMode = ref<`provinces` | `constituencies` | `national`>(`provinces`)
const constituencies = ref<any[]>([])

const API_URL = `${import.meta.env.VITE_API_URL}/provinces/results`
const API_CONSTITUENCIES = `${import.meta.env.VITE_API_URL}/constituencies/results`

function switchMode(mode: `provinces` | `constituencies` | `national`) {
  currentMode.value = mode

  if (mode === `provinces`) loadData()
  if (mode === `constituencies`) loadConstituencies()
  if (mode === `national`) console.log("nog niet geimplementeerd")
}
const sortedVotes = computed(() => {
  if (!selectedProvince.value) return []

  const votes = selectedProvince.value.stemmenPerPartij
  const maxCount = Math.max(...Object.values(votes))

  return Object.entries(votes)
    .map(([party, count]) => ({
      party,
      count,
      percentage: maxCount > 0 ? (count / maxCount) * 100 : 0,
    }))
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

async function loadConstituencies() {
  selectedProvince.value = null

  const res = await fetch(`${API_CONSTITUENCIES}/${selectedYear.value}`)
  constituencies.value = await res.json()

  if (mapContainer.value) {
    mapContainer.value.innerHTML = mapSvg

    highlightConstituencies()
  }
}


onMounted(loadData)

function onYearChange(event: Event) {
  const target = event.target as HTMLSelectElement
  setYear(Number(target.value))
}

function highlightConstituencies() {
  setTimeout(() => {
    const svg = mapContainer.value?.querySelector("svg")
    if (!svg) return

    // Verwijder oude markers
    const oldMarkers = mapContainer.value!.querySelectorAll(".kies-marker")
    oldMarkers.forEach(m => m.remove())

    // Voor elke kieskring → marker toevoegen
    constituencies.value.forEach((c: any) => {
      const pos = constituencyPositions[c.constituencyName]
      if (!pos) return

      const marker = document.createElement("div")

      marker.className =
        "kies-marker absolute w-4 h-4 bg-red-600 rounded-full cursor-pointer shadow-md \
         transition-transform duration-150 hover:scale-150 hover:bg-red-700"

      marker.style.left = pos.x + "px"
      marker.style.top = pos.y + "px"

// tooltip bij hover
      marker.title = c.constituencyName

      marker.addEventListener("click", () => {
        const data = constituencies.value.find(
          (x: any) => x.constituencyName === c.constituencyName
        )

        if (!data) return

        selectedProvince.value = {
          provinceNaam: c.constituencyName,
          stemmenPerPartij: data.stemmenPerPartij
        }
      })

      mapContainer.value!.appendChild(marker)

    })
  }, 200)
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
        if (isComparing.value) return

        if (selectedProvince.value?.provinceNaam === p.provinceNaam) {
          // Klik op dezelfde provincie → deselecteer
          selectedProvince.value = null
          // Reset alle kleuren
          document.querySelectorAll('svg path').forEach((other) => {
            if (other instanceof SVGPathElement) {
              other.style.fill = 'gray'
            }
          })
        } else {
          // Selecteer nieuwe provincie
          selectedProvince.value = p

          // Reset alle paden naar standaardkleur
          document.querySelectorAll('svg path').forEach((other) => {
            if (other instanceof SVGPathElement) {
              other.style.fill = 'gray'
            }
          })
          // Kleur actieve provincie
          path.style.fill = '#0056b3'
        }
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

    <div class="flex gap-3 mb-4">
      <div
        class="px-4 py-2 rounded-md border-2 shadow cursor-pointer bg-amber-200 border-amber-300 font-semibold"
        :class="{ '!bg-amber-300 border-amber-400 shadow-md': currentMode === 'provinces' }"
        @click="switchMode('provinces')"
      >
        Provincies
      </div>

      <div
        class="px-4 py-2 rounded-md border-2 shadow cursor-pointer bg-amber-200 border-amber-300 font-semibold"
        :class="{ '!bg-amber-300 border-amber-400 shadow-md': currentMode === 'constituencies' }"
        @click="switchMode('constituencies')"
      >
        Kieskringen
      </div>

      <div
        class="px-4 py-2 rounded-md border-2 shadow cursor-pointer bg-amber-200 border-amber-300 font-semibold"
        :class="{ '!bg-amber-300 border-amber-400 shadow-md': currentMode === 'national' }"
        @click="switchMode('national')"
      >
        Nationaal
      </div>
    </div>


    <!-- Container voor map + resultaten -->
    <div class="flex flex-col md:flex-row items-start w-full max-w-6xl gap-6">

      <!-- Map container -->
      <div
        ref="mapContainer"
        class="w-full md:flex-[1.5] border border-gray-400 overflow-hidden rounded shadow-lg bg-white p-4 md:p-6 h-[400px] md:h-[800px]"
      />

      <div
        v-if="selectedProvince && !isComparing"
        class="w-full md:flex-1 p-5 border rounded bg-white shadow h-[400px] md:h-[800px] overflow-y-auto"
      >
        <h2 class="font-bold text-lg mb-4">{{ selectedProvince.provinceNaam }}</h2>

        <!-- Vergelijk knop -->
        <button
          class="mb-4 bg-blue-600 text-white px-4 py-2 rounded"
          @click="toggleProvince(selectedProvince.provinceNaam)"
        >
          Vergelijk deze provincie
        </button>

        <!-- Geselecteerde provincies -->
        <div class="mb-4">
          <p class="font-semibold">Geselecteerd voor vergelijking:</p>
          <ul class="text-sm list-disc ml-4">
            <li v-for="p in selectedProvinces" :key="p">{{ p }}</li>
          </ul>
        </div>

        <div class="relative w-full border-l border-gray-300">

          <div class="absolute -bottom-6 left-0 right-0 flex justify-between text-xs text-gray-500">
            <span v-for="n in 4" :key="n">{{ n * 25 }}%</span>
          </div>

          <div class="absolute top-0 bottom-0 left-0 right-0 flex">
            <div
              v-for="n in 4"
              :key="n"
              class="border-r border-gray-200"
              :style="{ width: '11.11%' }"
            />
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
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div
        v-if="isComparing"
        class="w-full md:flex-1 p-5 border rounded bg-white shadow h-[400px] md:h-[800px] overflow-y-auto"
      >
        <h2 class="font-bold text-lg mb-4 md:mb-8">Geselecteerd voor vergelijking</h2>

        <div class="flex flex-col md:flex-row w-full gap-6">
          <div
            v-for="prov in compareResults"
            :key="prov.provinceNaam"
            class="w-full md:flex-1 p-4 bg-white border rounded shadow mb-6 md:mb-0"
          >
            <h3 class="font-bold text-lg mb-3">{{ prov.provinceNaam }}</h3>

            <button
              class="mb-4 bg-red-600 text-white px-4 py-2 rounded"
              @click="toggleProvince(prov.provinceNaam)"
            >
              Verwijder uit vergelijking
            </button>

            <div v-for="row in sortedCompareVotes(prov)" :key="row.party" class="mb-3">
              <div class="text-sm font-bold mb-1">{{ row.party }} — {{ row.count }}</div>
              <div class="h-4 bg-gray-200 rounded">
                <div
                  class="h-4 rounded"
                  :style="{ width: row.percentage + '%', backgroundColor: partyColors[row.party] || '#000' }"
                />
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
  height: 100%;
  max-width: 100%;
  max-height: 100%;
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
