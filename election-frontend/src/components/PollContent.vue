<script setup lang="ts">
defineProps<{
  polls: { question: string; options: string[]; votes: number[] }[];
  message: string;
}>();

defineEmits<{
  (e: "vote", pollIndex: number, optionIndex: number): void;
}>();
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

    <!-- Polls lijst -->
    <div v-else v-for="(poll, pollIndex) in polls" :key="pollIndex" class="bg-white p-4 rounded-lg shadow mb-6">
      <h3 class="text-lg font-semibold mb-2 border-b pb-1">{{ poll.question }}</h3>

      <!-- Opties -->
      <div class="space-y-2">
        <button
          v-for="(option, optionIndex) in poll.options || []"
          :key="optionIndex"
          @click="$emit('vote', pollIndex, optionIndex)"
          class="w-full border border-gray-300 rounded-lg px-3 py-2 text-left hover:bg-green-50"
        >
          {{ option }}
        </button>
      </div>

      <!-- Stemverdeling -->
      <div class="mt-3 bg-gray-50 p-2 rounded-lg text-sm">
        <p class="font-bold mb-2">📊 Stemverdeling:</p>
        <ul>
          <li v-for="(v, i) in poll.votes || []" :key="i" class="flex justify-between">
            <span>{{ poll.options?.[i] }}</span>
            <span>{{ v }} stemmen</span>
          </li>
        </ul>
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
