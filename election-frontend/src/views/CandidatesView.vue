<template>
  <div class="min-h-screen bg-[#FEFDF9] py-10 px-6">
    <div class="max-w-5xl mx-auto">
      <div class="flex flex-col sm:flex-row items-center justify-between mb-6 gap-4">
        <h1 class="text-4xl font-serif font-bold mb--4 text-black text-center sm:text-left">
          Kandidatenlijst
        </h1>

        <!-- SEARCHBAR -->
        <SearchBar v-model="search" @search="handleSearchInput" />

        <select
          v-model="selectedParty"
          class="border border-gray-300 rounded-lg px-4 py-2 text-black bg-white"
        >
          <option value="">Alle partijen</option>
          <option
            v-for="party in uniqueParties"
            :key="party"
            :value="party"
          >
            {{ party }}
          </option>
        </select>
      </div>

      <div class="bg-[#FFFDF7] shadow-md rounded-2xl p-6 border border-gray-100">
        <div v-if="paginatedCandidates.length === 0" class="text-gray-500 text-center py-6">
          Geen kandidaten gevonden...
        </div>

        <!-- lijst van kandidaten -->
        <ul v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          <li
            v-for="candidate in paginatedCandidates"
            :key="candidate.id"
            class="p-4 bg-white border rounded-xl shadow-sm hover:shadow-md transition-shadow duration-200"
          >
            <p class="font-semibold text-black">
              {{ candidate.firstName }} {{ candidate.lastName }}
            </p>
            <p class="text-gray-600 text-sm">
              Partij: {{ candidate.partyName }}
            </p>
          </li>
        </ul>

        <!-- Pagination -->
        <div class="flex justify-center mt-6 gap-2">
          <button
            class="px-4 py-2 bg-blue-950 text-white font-semibold rounded disabled:opacity-50"
            :disabled="currentPage === 1"
            @click="prevPage"
          >
            Vorige
          </button>
          <span class="px-4 py-2 text-black font-semibold">{{ currentPage }} / {{ totalPages }}</span>
          <button
            class="px-4 py-2 bg-blue-950 text-white font-semibold rounded disabled:opacity-50"
            :disabled="currentPage === totalPages"
            @click="nextPage"
          >
            Volgende
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import SearchBar from '@/components/SearchBar.vue';

interface Candidate {
  id: string;
  shortCode: string;
  firstName: string;
  lastName: string;
  partyId: string;
  partyName: string;
  votes: number;
}

const candidates = ref<Candidate[]>([]);
const search = ref("");
const currentPage = ref(1);
const ITEMS_PER_PAGE = 18;
const selectedParty = ref("");

const API_URL = `${import.meta.env.VITE_API_URL}/candidates`;
const SEARCH_URL = `${import.meta.env.VITE_API_URL}/search`;

// Backend zoekfunctie
async function searchCandidatesBackend(query: string) {
  const response = await axios.get(SEARCH_URL, {
    params: { name: query }
  });
  return response.data;
}


// Search + reset
async function handleSearchInput(query: string) {
  search.value = query;

  if (search.value.trim() === "") {
    // reset naar volledige lijst
    const response = await axios.get(API_URL);
    candidates.value = response.data;
    currentPage.value = 1;
    return;
  }

  const result = await searchCandidatesBackend(search.value);
  candidates.value = result;
  currentPage.value = 1;
}

onMounted(async () => {
  try {
    const response = await axios.get<Candidate[]>(API_URL);
    candidates.value = response.data;
  } catch (error) {
    console.error('Error fetching candidates:', error);
  }
});

const uniqueParties = computed(() => {
  const names = candidates.value.map((c) => c.partyName);
  return [...new Set(names)].filter((n) => n);
});

const filteredCandidates = computed(() => {
  if (!selectedParty.value) return candidates.value;
  return candidates.value.filter(
    (c) => c.partyName === selectedParty.value
  );
});

const totalPages = computed(() =>
  Math.ceil(filteredCandidates.value.length / ITEMS_PER_PAGE)
);

const paginatedCandidates = computed(() => {
  const start = (currentPage.value - 1) * ITEMS_PER_PAGE;
  const end = start + ITEMS_PER_PAGE;
  return filteredCandidates.value.slice(start, end);
});

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};
</script>

<style scoped>
</style>
