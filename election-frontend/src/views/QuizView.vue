<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { getToken } from "@/services/authService";

const partyIdToDisplayName: Record<string, string> = {
  "vvd": "VVD",
  "groenlinks___partij_van_de_arbeid_(pvda)": "GroenLinks-PvdA",
  "d66": "D66",
  "pvv_(partij_voor_de_vrijheid)": "PVV",
  "cda": "CDA",
  "forum_voor_democratie": "FvD",
  "bij1": "BIJ1",
  "partij_voor_de_dieren": "Partij voor de Dieren",
  "christenunie": "ChristenUnie",
  "sp_(socialistische_partij)": "SP",
  "50plus": "50Plus",
  "denk": "DENK",
  "fnp": "FNP",
  "vrede_voor_dieren": "Vrede voor Dieren",
  "ja21": "JA21",
  "volt": "Volt",
  "staatkundig_gereformeerde_partij_(sgp)": "SGP",
  "bbb": "BBB",
  "nieuw_sociaal_contract": "NSC",
  "bvnl___groep_van_haga": "BVNL",
  "lp_(libertaire_partij)": "LP",
  "piratenpartij___de_groenen": "PiratenPartij",
  "vrij_verbond": "Vrij Verbond",
  "de_linie": "De Linie",
  "nederland_met_een_plan": "NL PLAN",
  "ellect": "ELLECT",
  "partij_voor_de_rechtsstaat": "Partij voor de Rechtsstaat"
};

// Types
type Question = { id: string; text: string };
type QuizResult = { bestMatchingParty: string; percentages: Record<string, number> };
type PartyScore = { party: string; score: number };

// state
const questions = ref<Question[]>([]);
const answers = ref<Record<string, string>>({});
const result = ref<QuizResult | null>(null);

const loading = ref(true);
const errorMessage = ref("");
const globalError = ref("");

const topCandidates = ref<any[]>([]);

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
   TOP 3 KANDIDATEN LADEN
========================= */
async function loadTopCandidates(partyId: string) {
  try {
    const res = await fetch(
      `http://localhost:8080/api/candidates/top?partyId=${partyId}&limit=3`
    );
    if (!res.ok) throw new Error("Kon kandidaten niet laden");
    topCandidates.value = await res.json();
  } catch (e) {
    console.error("Fout bij laden top kandidaten:", e);
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
      body: JSON.stringify(answers.value),
    });

    if (!response.ok) throw new Error();

    result.value = await response.json();

    console.log("Quiz result:", result.value);

    // Top 3 kandidaten ophalen van de beste partij
    const partyId = result.value.bestMatchingParty;
    await loadTopCandidates(partyId);

  } catch (e) {
    console.error(e);
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

/* =========================
   RESET
========================= */
function resetQuiz() {
  answers.value = {};
  result.value = null;
  errorMessage.value = "";
  globalError.value = "";
  topCandidates.value = [];
}

onMounted(loadQuiz);
</script>

<template>
<div class="min-h-screen w-full bg-paper text-ink font-body paper-layer flex flex-col items-center py-16 px-6 relative">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')]"></div>

    <div class="relative z-10 text-center max-w-2xl">
    <h1 class="text-5xl font-headline retro-title mb-4 text-ink tracking-press">
        Welke partij past het beste bij jou? 🤓
      </h1>
      <p class="text-lg text-gray-700 italic mb-10 leading-relaxed">
        Ontdek met een paar korte vragen welke politieke partij het dichtst bij jouw mening staat.
      </p>
    </div>
    <!-- Laden -->
    <div v-if="loading" class="text-gray-600 text-lg animate-pulse mt-10">
      Even geduld... de quiz wordt geladen.
    </div>

    <!-- Fout -->
    <p v-if="!loading && globalError"
      class="relative z-10 mt-6 text-red-700 font-semibold bg-red-50 border border-red-200 rounded-md px-4 py-2">
      {{ globalError }}
    </p>

    <!-- Quiz -->
    <div v-else class="w-full max-w-3xl relative z-10 mt-6">
      <form @submit.prevent="submitQuiz"
        class="space-y-8 bg-paper-soft border-soft border rounded-xl shadow-soft p-8 hover-print">

        <div v-for="question in questions" :key="question.id" class="border-b border-gray-300 pb-4 last:border-none">
          <p class="font-semibold text-xl text-ink mb-3">{{ question.text }}</p>

          <div class="flex flex-wrap gap-6 text-gray-800">
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="radio" :name="question.id" value="Ja" v-model="answers[question.id]" class="accent-indigo-600" />
              Ja
            </label>
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="radio" :name="question.id" value="Nee" v-model="answers[question.id]" class="accent-indigo-600" />
              Nee
            </label>
            <label class="flex items-center gap-2 cursor-pointer">
              <input type="radio" :name="question.id" value="Neutraal" v-model="answers[question.id]" class="accent-indigo-600" />
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
            class="bg-black hover:bg-[#8e2f3b] text-white px-8 py-3 rounded-lg text-lg transition shadow-md">
            Bekijk mijn resultaat
          </button>

          <div v-else class="text-center">
            <button disabled class="bg-gray-400 cursor-not-allowed text-white px-8 py-3 rounded-lg text-lg opacity-70 shadow">
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
        class="mt-10 bg-paper-soft border-soft border rounded-xl shadow-soft p-8 paper-layer">

        <h2 class="text-3xl font-bold text-center mb-6">
          Jouw beste match:
          <span class="text-black-700">
          {{ partyIdToDisplayName[sortedResults[0].party] || sortedResults[0].party }}
          </span>
        </h2>

        <!-- Alle partijen tonen -->
        <div class="bg-gray-50 border border-gray-200 rounded-md text-left max-h-60 overflow-y-auto p-4 mb-10">
          <div v-for="item in sortedResults" :key="item.party"
            class="flex justify-between border-b border-gray-200 py-1">
            <span class="font-medium text-ink">
              {{ partyIdToDisplayName[item.party] || item.party }}
            </span>
            <span class="text-gray-700">{{ item.score.toFixed(1) }}%</span>
          </div>
        </div>

        <!-- TOP 3 KANDIDATEN -->
        <h3 class="text-2xl font-bold text-center mb-4">
          Top 3 kandidaten van {{ partyIdToDisplayName[result.bestMatchingParty] || result.bestMatchingParty }}
        </h3>

        <div class="grid gap-6">
          <div v-for="cand in topCandidates" :key="cand.id"
            class="border border-gray-300 rounded-lg p-5 bg-white">
            <h4 class="text-xl font-bold mb-2">
              {{ cand.firstName }} {{ cand.lastName }}
            </h4>
            <p class="text-gray-700 text-sm">
              Stemmen: {{ cand.votes }}
            </p>

          </div>
        </div>

        <button @click="resetQuiz"
          class="btn-primary text-lg px-8 py-3">
          Opnieuw proberen
        </button>
      </div>
    </div>
  </div>
</template>

<style>
</style>
