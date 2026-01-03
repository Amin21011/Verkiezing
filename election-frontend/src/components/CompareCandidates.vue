<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import { partyColors } from "@/assets/partyColors";
import { Bar } from "vue-chartjs";
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from "chart.js";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

interface Candidate {
  id: string;
  shortCode: string;
  firstName: string;
  lastName: string;
  partyId: string;
  partyName: string;
  gender: string;
  residence: string;
  votes: number;
}

const allCandidates = ref<Candidate[]>([]);
const selected1 = ref<string>("");
const selected2 = ref<string>("");
const search1 = ref("");
const search2 = ref("");
const comparedCandidates = ref<Candidate[]>([]);
const loading = ref(false);
const error = ref("");

const API_ALL = `${import.meta.env.VITE_API_URL}/candidates`;
const API_COMPARE = `${import.meta.env.VITE_API_URL}/candidates/compare`;

onMounted(async () => {
  try {
    const res = await axios.get<Candidate[]>(API_ALL);
    allCandidates.value = res.data;
  } catch (e) {
    error.value = "Kan kandidaten niet laden";
    console.error(e);
  }
});

// Filter kandidaten voor autocomplete
const filteredCandidates1 = computed(() =>
  allCandidates.value.filter(
    c =>
      `${c.firstName} ${c.lastName}`.toLowerCase().includes(search1.value.toLowerCase()) &&
      `${c.id}-${c.partyId}` !== selected2.value
  )
);

const filteredCandidates2 = computed(() =>
  allCandidates.value.filter(
    c =>
      `${c.firstName} ${c.lastName}`.toLowerCase().includes(search2.value.toLowerCase()) &&
      `${c.id}-${c.partyId}` !== selected1.value
  )
);

const canCompare = computed(() =>
  selected1.value && selected2.value && selected1.value !== selected2.value
);

const compare = async () => {
  try {
    loading.value = true;
    error.value = "";

    const c1 = allCandidates.value.find(
      c => `${c.id}-${c.partyId}` === selected1.value
    );
    const c2 = allCandidates.value.find(
      c => `${c.id}-${c.partyId}` === selected2.value
    );

    if (!c1 || !c2) {
      error.value = "Selecteer 2 geldige kandidaten";
      return;
    }

    const payload = [
      { candidateId: c1.id, partyId: c1.partyId },
      { candidateId: c2.id, partyId: c2.partyId },
    ];

    const res = await axios.post<Candidate[]>(API_COMPARE, payload, {
      headers: { "Content-Type": "application/json" },
    });

    comparedCandidates.value = res.data;
  } catch (e) {
    error.value = "Vergelijken mislukt";
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const getCandidateName = (value: string) => {
  const c = allCandidates.value.find(c => `${c.id}-${c.partyId}` === value);
  return c ? `${c.firstName} ${c.lastName}` : "";
};

// Bar chart data
const chartData = computed(() => ({
  labels: comparedCandidates.value.map(c => `${c.firstName} ${c.lastName}`),
  datasets: [
    {
      label: "Stemmen",
      data: comparedCandidates.value.map(c => c.votes),
      backgroundColor: comparedCandidates.value.map(
        c => partyColors[c.partyName] || "#00712D"
      ),
    },
  ],
}));

// Bar chart opties
const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: false }, // legend uitzetten zodat er geen vinkoptie is
    title: { display: true, text: "Vergelijking stemmen per kandidaat" },
  },
};
</script>

<template>
  <div class="max-w-3xl mx-auto p-6 bg-white rounded-xl shadow space-y-6">
    <h1 class="text-3xl font-bold">Vergelijk kandidaten</h1>

    <!-- Geselecteerde kandidaten als chips -->
    <div class="flex gap-2 mb-4">
      <span
        v-if="selected1"
        class="bg-blue-100 px-3 py-1 rounded-full flex items-center gap-1"
      >
        {{ getCandidateName(selected1) }}
        <button @click="selected1 = ''" class="text-gray-500 hover:text-gray-800">✕</button>
      </span>
      <span
        v-if="selected2"
        class="bg-green-100 px-3 py-1 rounded-full flex items-center gap-1"
      >
        {{ getCandidateName(selected2) }}
        <button @click="selected2 = ''" class="text-gray-500 hover:text-gray-800">✕</button>
      </span>
    </div>

    <div class="grid grid-cols-2 gap-4">
      <div>
        <input
          type="text"
          v-model="search1"
          placeholder="Zoek kandidaat 1..."
          class="border px-3 py-2 rounded w-full"
        />
        <ul v-if="search1 && filteredCandidates1.length"
            class="border mt-1 max-h-48 overflow-y-auto rounded">
          <li
            v-for="c in filteredCandidates1"
            :key="c.id + c.partyId"
            @click="selected1 = `${c.id}-${c.partyId}`; search1 = ''"
            class="px-3 py-2 hover:bg-gray-100 cursor-pointer flex justify-between items-center"
          >
            <span class="flex items-center">
              <span
                class="inline-block w-3 h-3 rounded-full mr-2"
                :style="{ backgroundColor: partyColors[c.partyName] || '#ccc' }"
              ></span>
              {{ c.firstName }} {{ c.lastName }}
            </span>
            <span class="text-gray-400">{{ c.partyName }}</span>
          </li>
        </ul>
      </div>

      <div>
        <input
          type="text"
          v-model="search2"
          placeholder="Zoek kandidaat 2..."
          class="border px-3 py-2 rounded w-full"
        />
        <ul v-if="search2 && filteredCandidates2.length"
            class="border mt-1 max-h-48 overflow-y-auto rounded">
          <li
            v-for="c in filteredCandidates2"
            :key="c.id + c.partyId"
            @click="selected2 = `${c.id}-${c.partyId}`; search2 = ''"
            class="px-3 py-2 hover:bg-gray-100 cursor-pointer flex justify-between items-center"
          >
            <span class="flex items-center">
              <span
                class="inline-block w-3 h-3 rounded-full mr-2"
                :style="{ backgroundColor: partyColors[c.partyName] || '#ccc' }"
              ></span>
              {{ c.firstName }} {{ c.lastName }}
            </span>
            <span class="text-gray-400">{{ c.partyName }}</span>
          </li>
        </ul>
      </div>
    </div>

    <!-- Vergelijk knop -->
    <button
      :disabled="!canCompare || loading"
      @click="compare"
      class="px-6 py-2 bg-blue-950 text-white rounded disabled:opacity-50"
    >
      Vergelijk
    </button>

    <p v-if="loading" class="text-center text-gray-500">Vergelijken...</p>
    <p v-if="error" class="text-center text-red-500">{{ error }}</p>

    <!-- Bar chart -->
    <div v-if="comparedCandidates.length === 2" class="mt-6">
      <Bar :data="chartData" :options="chartOptions" />
    </div>
  </div>
</template>
