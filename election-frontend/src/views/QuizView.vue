<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import { getToken } from "@/services/authService";

// Types
type Question = {
  id: string;
  text: string;
};

type QuizResult = {
  bestMatch: string;
  percentages: Record<string, number>;
};

type PartyScore = {
  party: string;
  score: number;
};

// state
const questions = ref<Question[]>([]);
const answers = ref<Record<string, string>>({});
const result = ref<QuizResult | null>(null);

const loading = ref(true);
const errorMessage = ref("");
const globalError = ref("");

const bios = ref<Record<string, string>>({});

const isLoggedIn = computed(() => !!getToken());

/* =========================
   QUIZ LADEN
========================= */
async function loadQuiz() {
  loading.value = true;
  globalError.value = "";

  try {
    const response = await fetch("http://localhost:8080/quiz");
    if (!response.ok) throw new Error();

    const data = await response.json();
    questions.value = data.questions ?? [];
  } catch {
    globalError.value = "Er ging iets mis bij het laden van de quiz.";
  } finally {
    loading.value = false;
  }
}

/* =========================
   QUIZ VERSTUREN
========================= */
async function submitQuiz() {
  const unanswered = questions.value.filter(q => !answers.value[q.id]);
  if (unanswered.length) {
    errorMessage.value = "Beantwoord alle vragen voordat je doorgaat.";
    return;
  }

  const token = getToken();
  if (!token) {
    globalError.value = "Je moet ingelogd zijn om de quiz in te sturen.";
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/quiz/result", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(Object.fromEntries(Object.entries(answers.value))),
    });

    if (!response.ok) throw new Error();

    result.value = await response.json();
  } catch {
    globalError.value = "Quiz verwerken mislukt.";
  }
}

/* =========================
   RESULTATEN SORTEREN
========================= */
const sortedResults = computed<PartyScore[]>(() => {
  if (!result.value) return [];

  return Object.entries(result.value.percentages)
    .map(([party, score]) => ({ party, score }))
    .sort((a, b) => b.score - a.score);
});

const topThree = computed(() => sortedResults.value.slice(0, 3));

/* =========================
   BIOGRAPHY OPHALEN
========================= */
async function loadBio(party: string) {
  if (bios.value[party]) return;

  try {
    const res = await fetch(`http://localhost:8080/candidates/${party}/bio`);
    bios.value[party] = await res.text();
  } catch {
    bios.value[party] = "Biography unavailable";
  }
}

watch(topThree, (list) => {
  list.forEach(item => loadBio(item.party));
});

/* =========================
   RESET
========================= */
function resetQuiz() {
  answers.value = {};
  result.value = null;
  errorMessage.value = "";
  globalError.value = "";
  bios.value = {};
}

onMounted(loadQuiz);
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

<!-- RESULTAAT -->
<div v-if="result && sortedResults.length"
  class="mt-10 bg-white/80 border border-gray-300 rounded-xl shadow-lg p-8"
>
  <h2 class="text-3xl font-bold text-center mb-6">
    Jouw beste match:
    <span class="text-indigo-700">
      {{ sortedResults[0].party }}
    </span>
  </h2>

  <!-- TOP 3 KANDIDATEN -->
  <div class="grid gap-6">
    <div
      v-for="item in topThree"
      :key="item.party"
      class="border border-gray-300 rounded-lg p-5 bg-white"
    >
      <h3 class="text-xl font-bold mb-2">
        {{ item.party }} – {{ item.score.toFixed(1) }}%
      </h3>

      <p class="text-gray-700 text-sm leading-relaxed">
        {{ bios[item.party] || "Biography unavailable" }}
      </p>

      <router-link
        :to="`/candidates/${item.party}`"
        class="text-indigo-700 underline mt-3 inline-block"
      >
        Bekijk volledig profiel
      </router-link>
    </div>
  </div>

  <button
    @click="resetQuiz"
    class="mt-8 bg-indigo-700 text-white px-6 py-2 rounded-lg hover:bg-indigo-800"
  >
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
