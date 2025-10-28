<template>
  <section class="bg-paper relative flex flex-col md:flex-row items-center justify-between max-w-7xl mx-auto px-8 py-16 md:py-20 transition-transform duration-500">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')]"></div>

    <div class="relative z-10 space-y-6 md:w-1/2">
      <p class="font-mono uppercase text-graymain">Editie 2025 • Amsterdam</p>

      <h2 class="text-5xl md:text-6xl font-extrabold leading-tight text-ink uppercase">
        <span class="block text-[3.5rem] text-accent border-r-[3px] border-accent pr-2 animate-typing">Niet stemmen?</span>
        <span class="bg-gradient-to-r from-[#c62828] via-[#ef6c00] to-[#fbc02d] bg-clip-text text-transparent font-black">Anderen beslissen voor jou.</span>
      </h2>

      <p class="relative max-w-md text-[1.2rem] leading-relaxed text-ink italic pl-6 border-l-[6px] border-accent">
        Lees. Denk. Kies. — <span class="font-bold">De toekomst begint bij jouw stem.</span></p>

      <button class="mt-8 px-8 py-3 border-2 border-accent text-accent font-bold uppercase tracking-widest bg-transparent hover:bg-accent hover:text-white transition-all duration-300">
        Ontdek jouw invloed
      </button>
    </div>

    <div class="relative z-10 mt-10 md:mt-0 md:w-1/2 flex justify-center md:justify-end">
      <div class="border-4 border-ink shadow-press hover:shadow-[-10px_10px_0_#1c1c1c] transition-all duration-300"><img src="@/assets/img/hero.png" class="max-h-80 object-cover grayscale hover:grayscale-0 transition duration-500" alt="Stem 2025"/></div>
    </div>

    <div class="absolute -bottom-[6px] left-0 w-full h-[6px] bg-gradient-to-r from-[#c62828] via-[#ef6c00] to-[#fbc02d]"></div>
  </section>
</template>

<style scoped>
@keyframes typing {
  0% { width: 0; }
  45%,55% { width: 100%; }
  100% { width: 0; }
}
@keyframes blink { 50% { border-color: transparent; } }
.animate-typing {
  overflow: hidden; white-space: nowrap;
  animation: typing 4s steps(18) infinite alternate, blink 1s step-end infinite;
  display: inline-block;
}
</style>
