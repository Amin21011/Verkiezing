<script setup lang="ts">
const currentDate = new Date().toLocaleDateString('nl-NL', {
  day: '2-digit',
  month: 'long',
  year: 'numeric',
})
</script>

<template>
  <section class="relative bg-retro-paper border-b-[6px] border-retro-ink/70 py-24 md:py-32 px-6 md:px-12 text-center overflow-hidden">
    <div class="absolute inset-0 opacity-[0.06] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"></div>

    <div class="absolute inset-0 bg-gradient-to-b from-transparent via-[#f6f3ed]/60 to-[#f6f3ed] pointer-events-none"></div>

    <div class="relative z-10 max-w-4xl mx-auto">
      <p class="uppercase tracking-[0.2em] text-sm font-mono text-gray-500 mb-4">
        Editie — {{ currentDate }}</p>

      <h1 class="font-retroHead text-5xl md:text-7xl font-extrabold text-retro-ink tracking-tight leading-[1.15] retro-title">
        <span class="block font-normal italic text-graymain">
          De stem van de toekomst in druk
        </span>
      </h1>

      <p class="mt-6 text-lg md:text-xl font-retroBody text-graymain leading-relaxed max-w-2xl mx-auto italic">
        Ontdek de resultaten, kandidaten en inzichten in onze digitale editie van vandaag.
      </p>

      <div class="mt-10 h-[4px] w-32 mx-auto bg-gradient-to-r from-retro-red via-retro-gold to-retro-blue rounded-full"></div>

      <div class="mt-10">
        <button class="px-10 py-3 font-retroHead text-lg bg-retro-ink text-retro-paper border-2 border-retro-ink hover:bg-transparent hover:text-retro-ink shadow-press transition-all duration-300 uppercase tracking-wide">
          Lees de editie ☕
        </button>
      </div>
    </div>

    <div class="absolute bottom-0 left-0 w-full h-[5px] bg-gradient-to-r from-retro-red via-retro-gold to-retro-blue opacity-90"></div>
  </section>
</template>

<style scoped>
.font-retroHead {
  font-family: 'Playfair Display', serif;
}
.font-retroBody {
  font-family: 'Merriweather', serif;
}
.bg-retro-paper {
  background-color: var(--paper, #fdfcf7);
}
.text-retro-ink {
  color: var(--ink, #1a1a1a);
}
.retro-title {
  animation: fadeInHeadline 1s ease forwards;
}
@keyframes fadeInHeadline {
  from {
    opacity: 0;
    letter-spacing: 0.1em;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    letter-spacing: 0;
    transform: translateY(0);
  }
}
</style>
