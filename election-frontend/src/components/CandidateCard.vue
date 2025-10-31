<template>
  <div class="border rounded-md p-4 shadow-sm hover:shadow-md transition">
    <h3 class="font-bold text-lg">{{ candidate.getName() }}</h3>
  </div>
</template>

<script setup lang="ts">
interface Candidate {
  getName: () => string;
}
defineProps<{
  candidate: Candidate;
}>()
</script>
