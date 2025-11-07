<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useElectionResults } from "../helpers/ElectionResultsHelper";

const { topParties, message, loadTopParties } = useElectionResults();
const selectedYear = ref<number>(2023);
const availableYears = [2017, 2021, 2023];

onMounted(() => {
  loadTopParties(5);
});

</script>

<template>
  <div class="max-w-md mx-auto bg-white/90 rounded-xl shadow-md p-6 space-y-4 backdrop-blur-sm">
    <div class="flex items-center gap-3">
      <label for="year" class="font-semibold text-gray-800">Selecteer verkiezingsjaar:</label>
      <select id="year" v-model="selectedYear" class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500">
        <option v-for="year in availableYears" :key="year" :value="year">
          {{ year }}
        </option>
      </select>
    </div>

    <p v-if="message" class="text-red-600 text-sm font-medium">{{ message }}</p>

    <ul v-else class="divide-y divide-gray-200">
      <li v-for="(party, index) in topParties" :key="party.id"
        class="flex justify-between items-center py-2 text-gray-800">
        <span class="font-bold w-5 text-indigo-600">{{ index + 1 }}.</span>
        <span class="flex-1 ml-2">{{ party.name }}</span>
        <span class="text-sm text-gray-500">{{ party.voteCount.toLocaleString() }} stemmen</span>
      </li>
    </ul>
  </div>
</template>
