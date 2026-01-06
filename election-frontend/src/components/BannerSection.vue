<template>
  <section class="relative overflow-hidden text-center bg-paper text-ink dark:bg-[var(--paper)] dark:text-[var(--ink)] py-24 md:py-32 px-6 md:px-12 border-y-2 border-ink dark:border-[var(--ink)]">
    <div class="absolute inset-0 pointer-events-none opacity-[0.05]" style="background-image:url('https://www.transparenttextures.com/patterns/newsprint.png')"></div>
    <div class="absolute inset-0 pointer-events-none bg-[radial-gradient(circle,_transparent_30%,_rgba(0,0,0,0.08)_100%)] dark:bg-[radial-gradient(circle,_transparent_35%,_rgba(0,0,0,0.35)_100%)]"></div>
    <div class="relative z-10 max-w-4xl mx-auto">

      <p class="uppercase tracking-[0.45em] text-[10px] font-mono font-bold text-graymain dark:text-[#b9b2a5] mb-6">
        Gelimiteerde Oplage — Editie No. 04
      </p>

      <h1 class="mt-5 font-['Crimson_Pro'] text-3xl md:text-5xl font-extrabold uppercase tracking-tight text-ink">
       <span class="block italic serif text-ink dark:text-[var(--ink)]">
          De stem van de toekomst </span>
        <span class="block mt-2 uppercase text-2xl md:text-2xl tracking-[0.3em] font-bold">
          gedrukt in data
        </span>
      </h1>

      <div class="mt-10 mb-10 flex justify-center items-center gap-4 opacity-40"></div>

      <p class="max-w-2xl mx-auto font-['Merriweather'] italic text-base md:text-lg leading-relaxed text-graymain dark:text-[#c9c2b4]">
        Verkiezingen ontleed zoals ze bedoeld zijn: in cijfers, context en scherpe observaties — samengesteld door de redactie van
        <span class="font-bold not-italic">de KIESKRANT</span>.
      </p>

      <div class="mt-16">
        <button class="relative px-10 py-4 font-mono text-[11px] uppercase tracking-[0.35em] border-2 border-ink dark:border-[var(--ink)] bg-paper dark:bg-[var(--paper)] text-ink dark:text-[var(--ink)] hover:bg-ink hover:text-paper dark:hover:bg-[var(--ink)] dark:hover:text-black transition-colors duration-300">
          Bekijk de editie
        </button>
      </div>
    </div>
    <div class="absolute bottom-3 left-1/2 -translate-x-1/2 w-[92%] h-[1px] bg-ink/30 dark:bg-[var(--ink)]/30"></div>
  </section>
</template>

<style scoped>
h1,
p {
  animation: pressFade 1.8s ease-out;
}

@keyframes pressFade {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
