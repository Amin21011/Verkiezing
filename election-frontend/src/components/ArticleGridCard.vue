<script setup lang="ts">
defineProps<{
  title: string
  summary: string
  date: string | null
  category: string
  link: string
}>()
</script>

<template>
  <article class="group relative bg-paper border-t-2 border-ink pt-6 pb-5 px-1 transition-colors duration-300 hover:bg-white/60">
    <div class="flex items-center gap-3 mb-3 text-xs font-mono uppercase tracking-widest">
      <span class="text-ink">{{ category }}</span>
      <span class="text-graymain">—</span>
      <time class="text-graymain">
        {{ date }}
      </time>
    </div>

    <h3 class="font-headline text-xl md:text-2xl font-extrabold leading-snug text-ink mb-3 group-hover:underline underline-offset-4">
      {{ title }}
    </h3>

    <p class="font-body text-base md:text-lg leading-relaxed max-w-prose line-clamp-3 text-graymain dark:text-[#c9c2b4]">
      {{ summary }}
    </p>

    <div class="mt-4">
      <a :href="link" target="_blank" rel="noopener" class="inline-flex items-center gap-1 font-mono text-xs uppercase tracking-widest text-[var(--accent)] hover:underline underline-offset-4">
        Lees artikel
        <span class="transition-transform group-hover:translate-x-1">→</span>
      </a>
    </div>

    <a :href="link" target="_blank" rel="noopener" class="absolute inset-0" aria-label="Lees artikel"></a>
  </article>
</template>
