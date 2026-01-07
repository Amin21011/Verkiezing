<script setup lang="ts">
import { onMounted, onBeforeUnmount } from 'vue'

const emit = defineEmits(['close'])

function onKey(e: KeyboardEvent) {
  if (e.key === 'Escape') {
    emit('close')
  }
}

onMounted(() => window.addEventListener('keydown', onKey))
onBeforeUnmount(() => window.removeEventListener('keydown', onKey))
</script>

<template>
  <teleport to="body">
    <div class="fixed inset-0 z-[9999] bg-black/40 dark:bg-black/60 backdrop-blur-sm
             flex items-center justify-center animate-fadeIn" @click.self="$emit('close')">

      <div class="relative w-full max-w-md
               bg-paper dark:bg-[var(--paper)] text-ink dark:text-[var(--ink)]
               border border-ink/40 dark:border-[var(--border-soft)]
               shadow-retroSoft dark:shadow-none px-8 py-10 animate-slideUp">

        <button class="absolute top-4 right-4 text-graymain hover:text-ink" @click="$emit('close')">
          ✕
        </button>
        <slot />
      </div>
    </div>
  </teleport>
</template>
