<script setup lang="ts">
import { ref, onMounted } from "vue";
import PollContent from "../components/PollContent.vue";

interface Poll {
  question: string;
  options: string[];
  votes: number[];
}

const API_URL = `${import.meta.env.VITE_API_URL}polls`;

const polls = ref<Poll[]>([]);
const message = ref("");

// Laad polls
async function loadPolls() {
  try {
    const res = await fetch(API_URL);
    polls.value = await res.json();

    const votedPolls = JSON.parse(localStorage.getItem("votedPolls") || "{}");
    if (Object.keys(votedPolls).length > polls.value.length) {
      localStorage.removeItem("votedPolls");
    }
    console.log("Polls geladen:", polls.value); // Check
  } catch (error) {
    console.log("Fout bij het laden van polls:", error);
  }
}

// Stemmen
async function vote(pollIndex: number, optionIndex: number) {
  const votedPolls = JSON.parse(localStorage.getItem("votedPolls") || "{}");

  if (votedPolls[pollIndex] !== undefined) {
    const previousOption = votedPolls[pollIndex];

    if (previousOption === optionIndex) {
      message.value = "Je hebt al op deze optie gestemd!";
      setTimeout(() => (message.value = ""), 3000);
      return;
    }

    await fetch(`${API_URL}/${pollIndex}/reset/${previousOption}`, {
      method: "PUT",
    });
  }

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
  <PollContent :polls="polls" :message="message" @vote="vote" />
</template>
