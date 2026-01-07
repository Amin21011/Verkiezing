<script setup lang="ts">
import { ref, onMounted } from "vue";
import PollContent from "../components/PollContent.vue";

interface Poll {
  id: number;
  question: string;
  options: string[];
  votes: number[];
}

const API_URL = `${import.meta.env.VITE_API_URL}/polls/active`;

const polls = ref<Poll[]>([]);
const message = ref("");

// Laad polls
const userVotes = ref<Record<number, number>>({});
// Load keuze uit localStorage
function loadUserVotes() {
  const saved = localStorage.getItem("userVotes");
  userVotes.value = saved ? JSON.parse(saved) : {};
}

// Save keuze
function saveUserVotes() {
  localStorage.setItem("userVotes", JSON.stringify(userVotes.value));
}

// Polls laden
async function loadPolls() {
  try {
    const res = await fetch(API_URL);
    const poll = await res.json();
    polls.value = poll ? [poll] : [];
  } catch (error) {
    console.log("Fout bij laden:", error);
  }
}

async function vote(pollId: number, optionIndex: number) {
  const previous = userVotes.value[pollId];

  if (previous === optionIndex) {
    message.value = "Je hebt al gestemd!";
    setTimeout(() => (message.value = ""), 3000); // verdwijnt na 3 sec
    return;
  }

  // Update UI meteen
  userVotes.value[pollId] = optionIndex;
  saveUserVotes();

  // Oude stem resetten
  if (previous !== undefined) {
    await fetch(`${import.meta.env.VITE_API_URL}/polls/${pollId}/reset/${previous}`, {
      method: "PUT",
    });
  }

  // Nieuwe stem toevoegen
  await fetch(`${import.meta.env.VITE_API_URL}/polls/${pollId}/vote/${optionIndex}`, {
    method: "POST",
  });

  // Polls opnieuw laden om percentages up-to-date te houden
  await loadPolls();
}

onMounted(() => {
  loadUserVotes();
  loadPolls();
});
</script>

<template>
  <PollContent
    :polls="polls"
    :message="message"
    :userVotes="userVotes"
    @vote="vote"
  />
</template>
