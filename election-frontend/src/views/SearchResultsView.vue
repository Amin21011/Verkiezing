<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

interface Candidate {
  id: number | string
  firstName: string
  lastName: string
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
                v-for="c in candidates"
                :key="c.id"
                class="py-3 hover:bg-gray-50 rounded-lg px-2 transition cursor-pointer"
              >
                <router-link
                  :to="`/candidates`"
                  class="block w-full"
                >
                  {{ c.firstName }} {{ c.lastName }}
                </router-link>
              </li>
            </ul>
          </section>

          <!-- Partijen -->
          <section v-if="parties.length">
            <h2 class="text-xl font-semibold text-[#00712D] mb-3">Partijen</h2>
            <ul class="divide-y divide-gray-200">
              <li
                v-for="p in parties"
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
