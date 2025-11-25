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
    polls.value = await res.json();
  } catch (error) {
    console.log("Fout bij het laden van polls:", error);
  }
}

async function vote(pollId: number, optionIndex: number) {
  const previousVote = userVotes.value[pollId];

  if (previousVote === optionIndex) {
    message.value = "Je hebt al gestemd!";
    setTimeout(() => (message.value = ""), 3000); // verdwijnt na 3 sec
    return;
  }

  if (previousVote !== undefined) {
    await fetch(`${API_URL}/${pollId}/reset/${previousVote}`, {
      method: "PUT",
    });
  }

  await fetch(`${API_URL}/${pollId}/vote/${optionIndex}`, {
    method: "POST",
  });

  userVotes.value[pollId] = optionIndex;
  saveUserVotes();

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
