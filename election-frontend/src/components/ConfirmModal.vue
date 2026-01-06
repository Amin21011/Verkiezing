<script setup lang="ts">
defineProps<{
  title: string
  message: string
}>()

defineEmits<{
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()
</script>

<template>
  <div class="fixed inset-0 z-[9999] bg-black/40 backdrop-blur-sm" @click="$emit('cancel')" />

  <div class="fixed inset-0 z-[10000] flex items-center justify-center">
    <div class="bg-paper border-2 border-ink shadow-hard p-8 max-w-md w-full space-y-6" @click.stop>
      <h3 class="text-2xl font-serif font-bold">{{ title }}</h3>
      <p class="italic text-graymain">{{ message }}</p>

      <div class="flex justify-end gap-4 pt-4">
        <button @click="$emit('cancel')" class="px-4 py-2 border border-ink/40 uppercase text-xs tracking-widest">
          Annuleren
        </button>

        <button @click="$emit('confirm')" class="px-4 py-2 border-2 border-ink uppercase text-xs tracking-widest hover:bg-ink hover:text-paper transition">
          Bevestigen
        </button>
      </div>
    </div>
  </div>
</template>
