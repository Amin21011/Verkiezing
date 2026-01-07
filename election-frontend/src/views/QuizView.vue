<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { getToken } from "@/services/authService.ts";

// Types
type Question = { id: string; text: string; };
type PartyScore = { party: string; score: number; };
type QuizResult = { bestMatchingParty?: string; bestMatch?: string; partyScores: Record<string, number>; };

// state
const questions = ref<Question[]>([]);
const answers = ref<Record<string, string>>({});
const result = ref<QuizResult | null>(null);
const loading = ref(true);
const errorMessage = ref("");
const globalError = ref("");

const API_URL = import.meta.env.VITE_API_URL;

const isLoggedIn = computed(() => !!getToken());
async function loadQuiz() {
  loading.value = true;
  globalError.value = "";

  try {
    const response = await fetch(`${API_URL}/quiz`);


    if (!response.ok) {
      throw new Error("Kon de quiz niet laden");
    }

    const data = await response.json();
    questions.value = data.questions ?? [];
  } catch (e) {
    console.error("Fout bij laden quiz:", e);
    globalError.value = "Er ging iets mis bij het laden van de quiz.";
  } finally {
    loading.value = false;
  }
}

// quiz versturen
async function submitQuiz() {
  // validatie
  const unanswered = questions.value.filter((q) => !answers.value[q.id]);
  if (unanswered.length > 0) {
    errorMessage.value = "Beantwoord alle vragen voordat je doorgaat.";
    return;
  }
  errorMessage.value = "";
  globalError.value = "";

  const token = getToken();
  if (!token) {
    globalError.value = "Je moet ingelogd zijn om de quiz in te sturen.";
    return;
  }

  try {
    const response = await fetch(`${API_URL}/quiz/result`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(answers.value),
    });

    if (!response.ok) {
      const text = await response.text();
      console.error("Backend fout bij quiz/result:", text);
      throw new Error("Quiz verwerken mislukt");
    }

    const data: QuizResult = await response.json();
    result.value = data;
  } catch (e) {
    console.error(e);
    globalError.value =
      "Er ging iets mis bij het berekenen of opslaan van jouw resultaat.";
  }
}

// gesorteerde resultaten
const sortedResults = computed<PartyScore[]>(() => {
  if (!result.value || !result.value.partyScores) return [];
  return Object.entries(result.value.partyScores)
    .map(([party, score]) => ({ party, score }))
    .sort((a, b) => b.score - a.score);
});

// reset
function resetQuiz() {
  answers.value = {};
  result.value = null;
  errorMessage.value = "";
  globalError.value = "";
}

onMounted(() => {
  loadQuiz();
});
</script>

<template>
  <div class="min-h-screen w-full bg-paper text-ink font-body flex flex-col items-center py-16 px-6 relative">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')]"></div>

    <div class="relative z-10 text-center max-w-2xl">
      <h1
        class="text-5xl font-headline font-bold mb-4 tracking-tight text-ink drop-shadow-sm">
        Welke partij past het beste bij jou? 🤓
      </h1>
      <p class="text-lg text-gray-700 italic mb-10 leading-relaxed">
        Ontdek met een paar korte vragen welke politieke partij het dichtst bij
        jouw mening staat. Geen stress — gewoon eerlijk invullen!
      </p>
    </div>

    <!-- Laden -->
    <div v-if="loading" class="text-gray-600 text-lg animate-pulse mt-10">
      Even geduld... de quiz wordt geladen.
    </div>

    <!-- Fout bij laden -->
    <p v-if="!loading && globalError"
      class="relative z-10 mt-6 text-red-700 font-semibold bg-red-50 border border-red-200 rounded-md px-4 py-2">
      {{ globalError }}
    </p>

    <!-- Quiz -->
    <div v-else class="w-full max-w-3xl relative z-10 mt-6">
      <form @submit.prevent="submitQuiz"
        class="space-y-8 bg-white/70 backdrop-blur-sm border border-gray-300 rounded-xl shadow-lg p-8">
        <div v-for="question in questions"
          :key="question.id"
          class="border-b border-gray-300 pb-4 last:border-none">
          <p class="font-semibold text-xl text-ink mb-3">
            {{ question.text }}
          </p>

          <div class="flex flex-wrap gap-6 text-gray-800">
            <label class="flex items-center gap-2 cursor-pointer">
              <input
                type="radio"
                :name="question.id"
                value="Ja"
                v-model="answers[question.id]"
                class="accent-indigo-600" />
              Ja
            </label>
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="radio"
                :name="question.id"
                value="Nee"
                v-model="answers[question.id]"
                class="accent-indigo-600" />
              Nee
            </label>
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="radio"
                :name="question.id"
                value="Neutraal"
                v-model="answers[question.id]"
                class="accent-indigo-600" />
              Neutraal
            </label>
          </div>
        </div>

        <p v-if="errorMessage"
          class="text-red-700 text-center font-semibold bg-red-50 border border-red-200 rounded-md py-2">
          {{ errorMessage }}
        </p>

        <div class="flex justify-center mt-6">
          <button v-if="isLoggedIn"
            type="submit"
            class="bg-indigo-700 text-white px-8 py-3 rounded-lg text-lg hover:bg-indigo-800 transition shadow-md">
            Bekijk mijn resultaat
          </button>

          <div v-else class="text-center">
            <button
              disabled
              class="bg-gray-400 cursor-not-allowed text-white px-8 py-3 rounded-lg text-lg opacity-70 shadow">
              Log in om jouw resultaat te zien
            </button>

            <p class="mt-2 text-indigo-900 cursor-pointer" @click="$router.push('/login')">
              Inloggen / Registreren
            </p>
          </div>
        </div>
      </form>

      <!-- Resultaat -->
      <div v-if="result && sortedResults.length"
        class="mt-10 bg-white/80 backdrop-blur-sm border border-gray-300 rounded-xl shadow-lg p-8 text-center">
        <h2 class="text-3xl font-bold text-ink mb-3">
          Jouw beste match:
          <span class="text-indigo-700">
            {{ sortedResults[0].party }} 🎉
          </span>
        </h2>

        <p class="text-gray-700 mb-4 italic">
          Bekijk hoe de andere partijen scoren:
        </p>
        <div class="bg-gray-50 border border-gray-200 rounded-md text-left max-h-60 overflow-y-auto p-4">
          <div
            v-for="item in sortedResults"
            :key="item.party"
            class="flex justify-between border-b border-gray-200 py-1">
            <span class="font-medium text-ink">{{ item.party }}</span>
            <span class="text-gray-700">
              {{ item.score.toFixed(1) }}%
            </span>
          </div>
        </div>

        <button @click="resetQuiz"
          class="mt-6 bg-indigo-700 text-white px-5 py-2 rounded-lg hover:bg-indigo-800 transition shadow-md">
          Opnieuw proberen
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import "@/assets/base.css";
@import "tailwindcss";

.answers input[type="radio"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}
</style>
