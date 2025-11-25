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
  <div class="p-6 max-w-4xl mx-auto">
    <h1 class="text-3xl font-bold mb-4">Zoekresultaten voor "{{ queryString }}"</h1>

    <div v-if="loading">Bezig met laden...</div>

    <div v-else>
      <h2 class="text-xl font-semibold mt-4">Kandidaten</h2>
      <ul>
        <li v-for="c in candidates" :key="c.id">
          {{ c.firstName }} {{ c.lastName }}
        </li>
      </ul>

      <h2 class="text-xl font-semibold mt-6">Partijen</h2>
      <ul>
        <li v-for="p in parties" :key="p.id">
          {{ p.name }}
        </li>
      </ul>

      <p v-if="!candidates.length && !parties.length && !loading" class="mt-4">
        Geen resultaten gevonden.
      </p>
    </div>
  </div>
</template>
