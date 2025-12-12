<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

interface Candidate {
  id: number | string
  firstName: string
  lastName: string
  partyId?: number | string
  partyName?: string | null
}

interface Party {
  id: number | string
  name: string
}

const route = useRoute()

const candidates = ref<Candidate[]>([])
const parties = ref<Party[]>([])

const loading = ref(false)
const queryString = ref('')

// Pagination state
const currentPageCandidates = ref(1)
const currentPageParties = ref(1)
const itemsPerPage = 8

const loadResults = async () => {
  const q = (route.query.q as string) ?? ''
  queryString.value = q

  if (!q) {
    candidates.value = []
    parties.value = []
    return
  }

  loading.value = true

  const API_URL = `${import.meta.env.VITE_API_URL}/search?name=${encodeURIComponent(q)}`

  try {
    const res = await axios.get(API_URL)
    candidates.value = res.data.candidates ?? []
    parties.value = res.data.parties ?? []

    currentPageCandidates.value = 1
    currentPageParties.value = 1
  } catch (err) {
    console.error('Error loading search results', err)
    candidates.value = []
    parties.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadResults)
watch(() => route.query.q, loadResults)

// Computed properties for paginated data
const paginatedCandidates = computed(() => {
  const start = (currentPageCandidates.value - 1) * itemsPerPage
  return candidates.value.slice(start, start + itemsPerPage)
})

const paginatedParties = computed(() => {
  const start = (currentPageParties.value - 1) * itemsPerPage
  return parties.value.slice(start, start + itemsPerPage)
})

const totalPagesCandidates = computed(() => Math.ceil(candidates.value.length / itemsPerPage))
const totalPagesParties = computed(() => Math.ceil(parties.value.length / itemsPerPage))

const nextPageCandidates = () => {
  if (currentPageCandidates.value < totalPagesCandidates.value) currentPageCandidates.value++
}
const prevPageCandidates = () => {
  if (currentPageCandidates.value > 1) currentPageCandidates.value--
}

const nextPageParties = () => {
  if (currentPageParties.value < totalPagesParties.value) currentPageParties.value++
}
const prevPageParties = () => {
  if (currentPageParties.value > 1) currentPageParties.value--
}
</script>

<template>
  <div class="min-h-screen bg-[#F8F7F3] flex items-start justify-center px-6 pt-20">
    <!-- Resultaten container -->
    <main class="w-full max-w-2xl space-y-6">
      <div class="bg-white rounded-2xl border border-gray-200 shadow-sm p-6">
        <h1 class="text-3xl font-bold text-gray-800 mb-4">
          Zoekresultaten voor "{{ queryString }}"
        </h1>

        <div v-if="loading" class="text-center text-gray-500 py-10">Bezig met laden…</div>

        <div v-else>
          <!-- Kandidaten -->
          <section v-if="candidates.length">
            <h2 class="text-xl font-semibold text-[#00712D] mb-3">Kandidaten</h2>
            <ul class="divide-y divide-gray-200">
              <li
                v-for="c in paginatedCandidates"
                :key="c.id"
                class="py-3 hover:bg-gray-50 rounded-lg px-2 transition cursor-pointer"
              >
                <router-link
                  :to="`/candidates`"
                  class="block w-full"
                >
                  {{ c.firstName }} {{ c.lastName }}
                  <span v-if="c.partyName" class="text-gray-500 ml-2">
                    ({{ c.partyName }})
                  </span>
                </router-link>
              </li>
            </ul>

            <!-- Pagination controls -->
            <div class="flex justify-between mt-3">
              <button
                class="px-4 py-2 bg-blue-950 text-white font-semibold rounded disabled:opacity-50"
                :disabled="currentPageCandidates === 1"
                @click="prevPageCandidates"
              >
                Vorige
              </button>
              <span class="px-4 py-2 text-black font-semibold">{{ currentPageCandidates }} / {{ totalPagesCandidates }}</span>
              <button
                class="px-4 py-2 bg-blue-950 text-white font-semibold rounded disabled:opacity-50"
                :disabled="currentPageCandidates === totalPagesCandidates"
                @click="nextPageCandidates"
              >
                Volgende
              </button>
            </div>
          </section>

          <!-- Partijen -->
          <section v-if="parties.length">
            <h2 class="text-xl font-semibold text-[#00712D] mb-3">Partijen</h2>
            <ul class="divide-y divide-gray-200">
              <li
                v-for="p in paginatedParties"
                :key="p.id"
                class="py-3 hover:bg-gray-50 rounded-lg px-2 transition cursor-pointer"
              >
                <router-link
                  :to="`/parties`"
                  class="block w-full"
                >
                  {{ p.name }}
                </router-link>
              </li>
            </ul>

            <!-- Pagination controls -->
            <div class="flex justify-between mt-3">
              <button @click="prevPageParties" :disabled="currentPageParties === 1" class="px-4 py-2 bg-blue-950 text-white font-semibold rounded disabled:opacity-50">Vorige</button>
              <span>Pagina {{ currentPageParties }} / {{ totalPagesParties }}</span>
              <button @click="nextPageParties" :disabled="currentPageParties === totalPagesParties" class="px-4 py-2 bg-blue-950 text-white font-semibold rounded disabled:opacity-50">Volgende</button>
            </div>
          </section>

          <!-- Geen resultaten -->
          <p v-if="!candidates.length && !parties.length" class="mt-4 text-center text-gray-500">
            Geen resultaten gevonden.
          </p>
        </div>
      </div>
    </main>
  </div>
</template>



<style scoped>
</style>
