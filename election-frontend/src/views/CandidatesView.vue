<template>
  <div class="min-h-screen bg-[#FEFDF9] py-10 px-6">
    <div class="max-w-5xl mx-auto">
      <h1 class="text-4xl font-serif font-bold text-center mb-6 text-black">
        Kandidatenlijst
      </h1>

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
          </li>
        </ul>

        <!-- Pagination -->
        <div class="flex justify-center mt-6 gap-2">
          <button
            class="px-4 py-2 bg-blue-950 text-white rounded disabled:opacity-50"
            :disabled="currentPage === 1"
            @click="prevPage"
          >
            Vorige
          </button>
          <span class="px-4 py-2">{{ currentPage }} / {{ totalPages }}</span>
          <button
            class="px-4 py-2 bg-blue-950 text-white rounded disabled:opacity-50"
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

interface Candidate {
  id: string;
  shortCode: string;
  firstName: string;
  lastName: string;
  partyId: string;
  votes: number;
}

const candidates = ref<Candidate[]>([]);
const currentPage = ref(1);
const ITEMS_PER_PAGE = 18;

const API_URL = `${import.meta.env.VITE_API_URL}candidates`;

onMounted(async () => {
  try {
    const response = await axios.get<Candidate[]>(API_URL);
    candidates.value = response.data;
  } catch (error) {
    console.error('Error fetching candidates:', error);
  }
});

const totalPages = computed(() => Math.ceil(candidates.value.length / ITEMS_PER_PAGE));

const paginatedCandidates = computed(() => {
  const start = (currentPage.value - 1) * ITEMS_PER_PAGE;
  const end = start + ITEMS_PER_PAGE;
  return candidates.value.slice(start, end);
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
