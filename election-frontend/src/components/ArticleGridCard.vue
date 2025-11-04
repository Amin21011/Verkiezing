<script setup lang="ts">
defineProps<{
  title: string
  date?: string
  summary?: string
  image?: string
  category?: string
}>()
</script>

<template>
  <article
    class="relative bg-white border-4 border-ink shadow-press hover:shadow-[-10px_10px_0_#1c1c1c] transition-all duration-300 flex flex-col overflow-hidden"
  >
    <div v-if="image" class="overflow-hidden h-48 md:h-56">
      <img
        :src="image"
        :alt="title"
        class="w-full h-full object-cover grayscale hover:grayscale-0 transition duration-500"
      />
    </div>

    <div class="flex flex-col justify-between flex-grow p-6 space-y-3">
      <div class="flex justify-between items-center text-xs uppercase font-mono tracking-widest text-graymain">
        <span v-if="category" class="bg-highlight text-ink px-2 py-1 rounded-sm font-semibold">
          {{ category }}
        </span>
        <span v-if="date">{{ date }}</span>
      </div>

      <h3 class="text-2xl font-headline font-bold leading-snug text-ink hover:text-accent cursor-pointer">
        {{ title }}
      </h3>

      <p v-if="summary" class="font-body text-graymain text-base leading-relaxed">
        {{ summary }}
      </p>

      <div class="mt-4">
        <a
          href="#"
          class="font-mono text-xs uppercase tracking-widest text-ink border-b-2 border-ink hover:text-accent hover:border-accent transition"
        >
          Lees meer →
        </a>
      </div>
    </div>

    <div class="absolute bottom-0 left-0 w-full h-[6px] bg-gradient-to-r from-accent via-yellow-400 to-blue-500"></div>
  </article>
</template>
