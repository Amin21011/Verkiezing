<template>
  <div class="quiz-container min-h-screen bg-[#d9d9d9] flex flex-col items-center font-sans p-6">
    <h1 class="text-4xl font-serif font-bold text-gray-800 mb-6 border-b-2 border-gray-500 pb-2">
      Politieke Partij Quiz ✏️
    </h1>

    <div v-if="loading" class="text-gray-700 text-lg">Laden...</div>

    <div v-else class="w-full max-w-3xl">
      <form @submit.prevent="submitQuiz" class="space-y-6">
        <!-- Elke vraag -->
        <div
          v-for="question in questions"
          :key="question.id"
          class="bg-[#a0a0a0] border border-gray-600 rounded-lg shadow-md p-5 hover:shadow-lg transition"
        >
          <p class="text-lg font-semibold text-gray-800 mb-3">{{ question.text }}</p>

          <div class="answers flex gap-8 text-gray-900">
            <label class="flex items-center gap-2">
              <input
                type="radio"
                :name="question.id"
                value="Ja"
                v-model="answers[question.id]"
                class="accent-gray-800"
              />
              Ja
            </label>
            <label class="flex items-center gap-2">
              <input
                type="radio"
                :name="question.id"
                value="Nee"
                v-model="answers[question.id]"
                class="accent-gray-800"
              />
              Nee
            </label>
            <label class="flex items-center gap-2">
              <input
                type="radio"
                :name="question.id"
                value="Neutraal"
                v-model="answers[question.id]"
                class="accent-gray-800"
              />
              Neutraal
            </label>
          </div>
        </div>

        <div class="flex justify-center">
          <button
            type="submit"
            class="bg-gray-800 text-white px-6 py-2 rounded-md text-lg hover:bg-black transition"
          >
            Bekijk resultaat
          </button>
        </div>
      </form>

      <!-- Resultaat -->
      <div
        v-if="result"
        class="mt-10 bg-[#a0a0a0] border border-gray-600 rounded-lg p-6 shadow-md text-center"
      >
        <h2 class="text-2xl font-bold text-gray-800 mb-4">
          Beste match: {{ result.bestMatchingParty }}
        </h2>

        <p class="text-gray-700 mb-3 italic">Score per partij:</p>
        <div
          class="bg-white border border-gray-300 rounded-md text-left max-h-60 overflow-y-auto p-4"
        >
          <div
            v-for="(score, party) in result.partyScores"
            :key="party"
            class="flex justify-between border-b border-gray-200 py-1"
          >
            <span class="font-medium text-gray-800">{{ party }}</span>
            <span class="text-gray-700">{{ score.toFixed(1) }}</span>
          </div>
        </div>

        <button
          @click="resetQuiz"
          class="mt-6 bg-gray-800 text-white px-4 py-2 rounded-md hover:bg-black transition"
        >
          Opnieuw proberen
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const questions = ref([]);
const answers = ref({});
const result = ref(null);
const loading = ref(true);

async function loadQuiz() {
  try {
    const response = await fetch("http://localhost:8080/quiz");
    questions.value = (await response.json()).questions;
  } catch (e) {
    console.error("Fout bij laden quiz:", e);
  } finally {
    loading.value = false;
  }
}

async function submitQuiz() {
  try {
    const response = await fetch("http://localhost:8080/quiz/result", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(answers.value),
    });
    result.value = await response.json();
  } catch (e) {
    alert("Er ging iets mis bij het berekenen van de uitslag!");
  }
}

function resetQuiz() {
  answers.value = {};
  result.value = null;
}

onMounted(() => loadQuiz());
</script>

<style scoped>
/* Scroll behavior consistent met andere pagina’s */
* {
  -webkit-overflow-scrolling: touch;
}

.answers input[type="radio"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}
</style>
