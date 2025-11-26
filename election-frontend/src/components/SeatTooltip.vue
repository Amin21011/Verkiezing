<script setup lang="ts">
defineProps<{
  x: number;
  y: number;
  visible: boolean;
  party: string;
  seats: number;
  delta: number;
  votesPerSeat: number;
}>();
</script>

<template>
  <div v-if="visible" class="fixed px-4 py-3 bg-paper border border-ink/40 rounded-md shadow-xl pointer-events-none backdrop-blur-sm"
    :style="{ top: y + 'px', left: x + 'px' }">
    <p class="font-headline font-bold text-ink text-sm">{{ party }}</p>
    <p class="text-xs text-graymain">Zetels: {{ seats }}</p>

    <p v-if="delta !== 0" :class="delta > 0 ? 'text-green-600' : 'text-red-600'" class="text-xs font-bold">
      Δ {{ delta > 0 ? '+' : '' }}{{ delta }}
    </p>

    <p class="text-xs text-graymain italic">
      {{ votesPerSeat.toLocaleString() }} stemmen / zetel
    </p>
  </div>
</template>
