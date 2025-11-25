<script setup lang="ts">
import { defineProps, defineEmits, ref, watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

interface Candidate {
  id: number | string
  firstName: string
  lastName: string
  partyId?: number | string
  partyName?: string | null
  votes?: number
  name?: string
}

interface Party {
  id: number | string
  name: string
}

const router = useRouter()
const candidates = ref<Candidate[]>([])
const parties = ref<Party[]>([])

const props = defineProps<{ modelValue: string }>()
const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'search', value: string): void
}>()

const searchValue = ref(props.modelValue ?? '')
const noResults = ref(false)

// Sync v-model
watch(() => props.modelValue, (val) => {
  searchValue.value = val ?? ''
})

// ENTER → navigeren
const onEnter = () => {
  if (!searchValue.value) return

  router.push({ name: 'search', query: { q: searchValue.value } }).catch(() => {
    router.push({ path: '/search', query: { q: searchValue.value } })
  })
}

// Input → zoeken
const onInput = async () => {
  emit('update:modelValue', searchValue.value)
  emit('search', searchValue.value)

  if (!searchValue.value) {
    candidates.value = []
    parties.value = []
    noResults.value = false
    return
  }

  const API_URL = `${import.meta.env.VITE_API_URL}/search?name=${encodeURIComponent(
    searchValue.value
  )}`

  try {
    const response = await axios.get(API_URL)

    candidates.value = response.data.candidates ?? []
    parties.value = response.data.parties ?? []
    noResults.value =
      candidates.value.length === 0 && parties.value.length === 0
  } catch (error) {
    console.error('Search error', error)
  }
}
</script>

<template>
  <div class="search-wrapper relative">
    <input
      v-model="searchValue"
      @input="onInput"
      @keyup.enter="onEnter"
      type="text"
      placeholder="Zoeken..."
      class="search-input"
    />
    <span class="search-icon cursor-pointer" @click="onEnter">🔍</span>
  </div>
</template>

<style scoped>

.results-list li {
  padding: 0.25rem 0.5rem;
  cursor: pointer;
  transition: background 0.2s;
}

.results-list li:hover {
  background: #f0f0f0;
}
</style>
