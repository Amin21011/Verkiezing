<script setup lang="ts">
import { ref, onMounted } from "vue";

interface Poll {
  question: string;
  options: string[];
  votes: number[];
}

const API_URL = `${import.meta.env.VITE_API_URL}polls`;

const polls = ref<Poll[]>([]);
const message = ref(""); // Meldingstekst


async function loadPolls() {
  const res = await fetch(API_URL);
  polls.value = await res.json();

  // Controleer of localStorage nog klopt met de huidige polls
  const votedPolls = JSON.parse(localStorage.getItem("votedPolls") || "{}");

  // Als er meer stemmen in localStorage dan reset polls
  if (Object.keys(votedPolls).length > polls.value.length) {
    localStorage.removeItem("votedPolls");
  }
}

async function vote(pollIndex: number, optionIndex: number) {
  const votedPolls = JSON.parse(localStorage.getItem("votedPolls") || "{}");

  if (votedPolls[pollIndex] !== undefined) {
    const previousOption = votedPolls[pollIndex];

    if (previousOption === optionIndex) {
      message.value = "Je hebt al op deze optie gestemd!";
      setTimeout(() => (message.value = ""), 3000); // Verdwijnt na 3 sec
      return;
    }

    // Oude stem verwijderen
    await fetch(`${API_URL}/${pollIndex}/reset/${previousOption}`, {
      method: "PUT",
    });

  }

  // Nieuwe stem toevoegen
  await fetch(`${API_URL}/${pollIndex}/vote/${optionIndex}`, {
    method: "POST",
  });

  votedPolls[pollIndex] = optionIndex;
  localStorage.setItem("votedPolls", JSON.stringify(votedPolls));

  await loadPolls();
}

onMounted(loadPolls);
</script>

<template>
  <div class="min-h-screen bg-[#F8F7F3] p-10">
    <div class="max-w-4xl mx-auto relative -left-35">
      <h1 class="text-3xl font-bold text-center mb-8 py-4 text-black">
        🗳️ Huidige Polls
      </h1>

      <!-- Melding -->
      <div v-if="message" class="text-center bg-yellow-100 text-yellow-800 p-4 mb-6 rounded-lg">
        {{ message }}
      </div>

      <div v-if="polls.length === 0" class="text-black text-center italic bg-white p-6 rounded-xl shadow-sm">
        Er zijn nog geen polls beschikbaar.
      </div>

      <div v-for="(poll, pollIndex) in polls" :key="pollIndex" class="bg-white p-6 rounded-2xl shadow-md mb-8 hover:shadow-lg transition-shadow duration-300">
        <h2 class="text-xl font-semibold mb-4 text-black border-b pb-2">{{ poll.question }}</h2>

        <div class="space-y-12">
          <button
            v-for="(option, optionIndex) in poll.options"
            :key="optionIndex"
            @click="vote(pollIndex, optionIndex)"
            class="w-full border border-gray-300 rounded-lg px-4 py-3 text-left font-medium text-black hover:bg-green-50"
          >
            {{ option }}
          </button>
        </div>

        <div class="mt-4 bg-gray-50 p-3 rounded-lg text-sm text-black">
          <p class="text-2xl font-bold mb-4">📊 Stemverdeling:</p>
          <ul class="text-base font-semibold text-black py-2">
            <li v-for="(v, i) in poll.votes" :key="i" class="flex justify-between py-1">
              <span>{{ poll.options[i] }}</span>
              <span class="font-medium">{{ v }} stemmen</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>
