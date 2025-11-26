<script setup lang="ts">
defineProps<{
  data: { party:string; seats:number; color:string; delta:number }[]
}>();
</script>

<template>
  <div class="mt-8 p-6 bg-paper border border-ink/40 rounded-xl shadow-sm">
    <h3 class="font-headline font-bold text-ink text-xl mb-4">Partijverdeling</h3>

    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
      <div v-for="entry in data" :key="entry.party"
        class="flex items-center gap-3">

        <span class="w-5 h-5 rounded-full border border-ink/30 shadow-sm"
              :style="{ backgroundColor: entry.color }"></span>

        <div>
          <p class="text-sm text-ink font-medium">
            {{ entry.party }}
            <span class="text-graymain">({{ entry.seats }})</span>
          </p>

          <p v-if="entry.delta !== 0" :class="entry.delta > 0 ? 'text-green-600' : 'text-red-600'"
             class="text-xs font-semibold"> Δ {{ entry.delta > 0 ? '+' : '' }}{{ entry.delta }}
          </p>

          <p v-else class="text-xs text-gray-400 italic">Δ 0</p>
        </div>
      </div>
    </div>
  </div>
</template>
