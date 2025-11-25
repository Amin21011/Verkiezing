<script setup lang="ts">
import { ref, onMounted } from "vue";
import PollContent from "../components/PollContent.vue";

interface Poll {
  id: number;
  question: string;
  options: string[];
  votes: number[];
}

const API_URL = `${import.meta.env.VITE_API_URL}/polls`;

const polls = ref<Poll[]>([]);
const message = ref("");

// Laad polls
async function loadPolls() {
  try {
    const res = await fetch(API_URL);
    polls.value = await res.json();
    console.log("Polls geladen:", polls.value);
  } catch (error) {
    console.log("Fout bij het laden van polls:", error);
  }
}

async function vote(pollId: number, optionIndex: number) {
  await fetch(`${API_URL}/${pollId}/vote/${optionIndex}`, {
    method: "POST",
  });

  await loadPolls();
}

onMounted(loadPolls);
</script>

<template>
  <PollContent :polls="polls" :message="message" @vote="vote" />
</template>
