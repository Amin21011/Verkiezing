<script setup lang="ts">
import { toast } from '@/composables/useFlash.ts'
</script>

<template>
  <Transition enter-active-class="transition-all duration-400 ease-out" enter-from-class="opacity-0 -translate-y-3" enter-to-class="opacity-100 translate-y-0"
    leave-active-class="transition-all duration-300 ease-in" leave-from-class="opacity-100 translate-y-0" leave-to-class="opacity-0 -translate-y-2">
    <div v-if="toast" class="fixed top-6 left-1/2 -translate-x-1/2 z-[9998] flex justify-center">
      <div :class="['px-6 py-3 rounded-md shadow-md border border-zinc-800/20 font-headline tracking-wide text-base md:text-lg transition-all duration-300 backdrop-blur-sm',
          toast.type === 'error' ? 'bg-[#fcebea] text-[#611818]'
            : toast.type === 'success' ? 'bg-[#e9f7ef] text-[#1b4332]'
            : 'bg-[#f7f6f2] text-[#1e1e1e]']">
        {{ toast.message }}
      </div>
    </div>
  </Transition>
</template>
