<template>
  <div class="min-h-screen bg-[#FEFDF9] py-10 px-6">
    <div class="max-w-5xl mx-auto">
      <h1 class="text-4xl font-serif font-bold text-center mb-6 text-gray-800">
        Kandidatenlijst
      </h1>

      <div class="bg-[#FFFDF7] shadow-md rounded-2xl p-6 border border-gray-100">
        <div v-if="candidates.length === 0" class="text-gray-500 text-center py-6">
          Geen kandidaten gevonden...
        </div>

        <!-- lijst van kandidaten -->
        <ul v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          <li
            v-for="candidate in candidates"
            :key="candidate.id"
            class="p-4 bg-white border rounded-xl shadow-sm hover:shadow-md transition-shadow duration-200"
          >
            <p class="font-semibold text-gray-800">
              {{ candidate.firstName }} {{ candidate.lastName }}
            </p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
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

const API_URL = `${import.meta.env.VITE_API_URL}candidates`;

onMounted(async () => {
  try {
    const response = await axios.get<Candidate[]>(API_URL);
    candidates.value = response.data;
  } catch (error) {
    console.error('Error fetching candidates:', error);
  }
});
</script>

<style scoped>

</style>
