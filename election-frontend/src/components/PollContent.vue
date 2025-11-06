<script setup lang="ts">

defineProps<{
  polls: { question: string; options: string[]; votes: number[] }[];
  message: string;
}>();

defineEmits<{
  (e: "vote", pollIndex: number, optionIndex: number): void;
}>();

function getTotalVotes(votes: number[]) {
  return votes.reduce((a, b) => a + b, 0);
}

function getPercentage(votes: number[], i: number) {
  const total = getTotalVotes(votes);
  return total > 0 ? ((votes[i] / total) * 100).toFixed(0) : 0;
}
</script>


<template>
  <div class="poll-container">
    <h2 class="text-xl font-semibold mb-4 text-center">🗳️ Huidige Polls</h2>

    <!-- Melding -->
    <div v-if="message" class="text-center bg-yellow-100 text-yellow-800 p-2 mb-4 rounded-lg">
      {{ message }}
    </div>

    <!-- Geen polls -->
    <div v-if="!polls || polls.length === 0" class="text-gray-700 text-center italic">
      Er zijn nog geen polls beschikbaar.
    </div>

    <div v-else v-for="(poll, pollIndex) in polls" :key="pollIndex" class="bg-white p-4 rounded-lg shadow mb-6 w-full max-w-xl">
      <h3 class="text-lg font-semibold mb-2 border-b pb-1">{{ poll.question }}</h3>

      <!-- Opties -->
      <div class="space-y-2">
        <div
          v-for="(option, optionIndex) in poll.options || []"
          :key="optionIndex"
          class="relative w-full cursor-pointer"
          @click="$emit('vote', pollIndex, optionIndex)"
        >
          <!-- Achtergrond balk -->
          <div class="absolute top-0 left-0 h-full bg-blue-950 rounded-l-lg"
               :style="{ width: getPercentage(poll.votes, optionIndex) + '%' }"></div>

          <!-- Tekst -->
          <div class="relative flex justify-between px-3 py-2 border text-black border-gray-300 rounded-lg">
            <span>{{ option }}</span>
            <span>{{ getPercentage(poll.votes, optionIndex) }}%</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.poll-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
