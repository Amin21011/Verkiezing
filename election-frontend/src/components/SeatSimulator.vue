<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import { simulateSeats } from "@/services/SimulationService";
import { normalizeParty, getPartyColor } from "@/helpers/SimulatorHelper";

import SeatChart from "@/components/SeatChart.vue";
import SeatChamber from "@/components/SeatChamber.vue";
import SeatLegend from "@/components/SeatLegend.vue";

const turnout = ref(80);
const threshold = ref(0.67);

const results = ref<
  { party: string; seats: number; color: string; delta: number }[]
>([]);
const votesPerSeat = ref(0);

const baseline = ref<Record<string, number>>({});

async function load() {
  const data = await simulateSeats(turnout.value, threshold.value);

  if (Object.keys(baseline.value).length === 0) {
    data.seats.forEach((entry: any) => {
      baseline.value[normalizeParty(entry.party)] = entry.seats;
    });
  }

  results.value = data.seats.map((entry: any) => {
    const key = normalizeParty(entry.party);
    const originalSeats = baseline.value[key] ?? entry.seats;

    return {
      ...entry,
      color: getPartyColor(key),
      delta: entry.seats - originalSeats,
    };
  });

  votesPerSeat.value = data.votesPerSeat;
}

watch([turnout, threshold], load);
onMounted(load);
</script>

<template>
  <section class="relative mx-auto max-w-[1150px] bg-paper text-ink dark:bg-[var(--paper)] dark:text-[var(--ink)]
           border-y-4 border-ink/60 dark:border-[var(--border-soft)] px-8 sm:px-12 py-12 sm:py-16 space-y-20 shadow-retroSoft dark:shadow-none">

    <div class="absolute inset-0 opacity-[0.05] pointer-events-none" style="background-image:url('https://www.transparenttextures.com/patterns/newsprint.png')">
    </div>

    <header class="relative z-10 space-y-6">
      <div class="inline-flex items-center gap-3">
        <span class="h-px w-10 bg-ink/60 dark:bg-[var(--border-soft)]"></span>
        <p class="uppercase tracking-[0.35em] text-[10px] font-mono
                 text-graymain dark:text-[var(--muted)]">
          Analyse — Zetelverdeling
        </p>
      </div>

      <h2 class="text-4xl sm:text-5xl font-headline font-black leading-tight">
        Simulatie Tweede Kamer
        <span
          class="block mt-3 text-base sm:text-lg uppercase tracking-[0.3em] font-semibold text-graymain dark:text-[var(--muted)]">
          experimenteer met de uitslag
        </span>
      </h2>

      <p class="text-base sm:text-lg max-w-2xl leading-relaxed
               text-graymain dark:text-[var(--muted)]">
        De Nederlandse Tweede Kamer telt
        <strong class="text-ink dark:text-[var(--ink)]">150 zetels</strong>.
        Met deze simulator zie je hoe veranderingen in
        <strong class="text-ink dark:text-[var(--ink)]">opkomst</strong> en
        <strong class="text-ink dark:text-[var(--ink)]">kiesdrempel</strong>
        de verdeling beïnvloeden. </p>
    </header>

    <section class="relative z-10  bg-[rgba(0,0,0,0.02)] dark:bg-[rgba(255,255,255,0.03)] px-6 py-6 space-y-3
             border-l-4 border-ink/60 dark:border-[var(--border-soft)]">

      <h3 class="font-headline text-xl font-bold flex items-center gap-3">
        🗳️ Wat je hier ziet
      </h3>

      <p class="text-sm sm:text-base max-w-3xl leading-relaxed
               text-graymain dark:text-[var(--muted)]">
        Deze simulator gebruikt dezelfde rekenregels als bij echte verkiezingen.
        De zetels worden verdeeld met de
        <strong class="text-ink dark:text-[var(--ink)]">D’Hondt-methode</strong>.
      </p>

      <ul class="list-disc ml-6 text-sm space-y-1
               text-graymain dark:text-[var(--muted)]">
        <li><strong class="text-ink">Opkomst</strong>: hoeveel mensen stemmen</li>
        <li><strong class="text-ink">Kiesdrempel</strong>: minimaal aandeel</li>
        <li><strong class="text-ink">Δ (delta)</strong>: verschil t.o.v. echt</li>
      </ul>
    </section>

    <section class="relative z-10 grid grid-cols-1 md:grid-cols-2 gap-12">
      <div class="space-y-3">
        <label class="font-semibold text-sm uppercase tracking-widest
                 text-graymain dark:text-[var(--muted)]">
          Opkomst
        </label>

        <div class="flex justify-between text-sm font-mono opacity-80">
          <span>{{ turnout }}%</span>
          <span>kiezers</span>
        </div>

        <input type="range" min="10" max="100" v-model="turnout"
          class="w-full cursor-pointer
                 accent-ink dark:accent-[var(--ink)]
                 opacity-90 dark:opacity-80" />

        <p class="text-sm max-w-sm leading-relaxed
                 text-graymain dark:text-[var(--muted)]">
          Hoe hoger de opkomst, hoe kleiner de invloed van kleine verschuivingen.
        </p>
      </div>

      <div class="space-y-3">
        <label class="font-semibold text-sm uppercase tracking-widest text-graymain dark:text-[var(--muted)]">
          Kiesdrempel
        </label>

        <div class="flex justify-between text-sm font-mono opacity-80">
          <span>{{ threshold }}%</span>
          <span>grens</span>
        </div>

        <input type="range" min="0" max="5" step="0.1" v-model="threshold" class="w-full cursor-pointer
                 accent-ink dark:accent-[var(--ink)] opacity-90 dark:opacity-80" />

        <p class="text-sm max-w-sm leading-relaxed
                 text-graymain dark:text-[var(--muted)]">
          Rond <strong class="text-ink"> 0,67% </strong> ligt de natuurlijke grens.
        </p>
      </div>
    </section>

    <section class="relative z-10 py-4
             border-t border-ink/40 dark:border-[var(--border-soft)]">
      <p class="text-sm sm:text-base text-graymain dark:text-[var(--muted)]">
        <strong class="text-ink">Stemmen per zetel:</strong>
        gemiddeld <strong>{{ votesPerSeat.toLocaleString() }}</strong> </p>
    </section>

    <section class="relative z-10 space-y-4">
      <h3 class="font-headline text-lg font-bold flex items-center gap-3">
        <span class="h-px flex-1 bg-ink/30 dark:bg-[var(--border-soft)]"></span>
        📊 Aandeel per partij </h3>
      <SeatChart :data="results" />
    </section>

    <section class="relative z-10 space-y-4">
      <h3 class="font-headline text-lg font-bold flex items-center gap-3">
        <span class="h-px flex-1 bg-ink/30 dark:bg-[var(--border-soft)]"></span>
        Tweede Kamer — 150 zetels </h3>

      <p class="text-sm max-w-xl text-graymain dark:text-[var(--muted)]">
        Elke cirkel stelt één zetel voor. </p>

      <SeatChamber :data="results" :votesPerSeat="votesPerSeat" />
    </section>

    <SeatLegend :data="results" />

    <section
      class="relative z-10 pt-8 space-y-4
             border-t-2 border-double border-ink/40
             dark:border-[var(--border-soft)]">

      <h3 class="font-headline text-2xl font-bold">
        ⚖️ Hoe werkt de D’Hondt-methode?
      </h3>

      <p class="text-sm sm:text-base max-w-3xl text-graymain dark:text-[var(--muted)]">
        Zetels worden verdeeld via quotienten waarbij grotere partijen
        eerst profiteren maar daarna afvlakken.
      </p>

      <pre class="bg-paper dark:bg-[#161616]
               border border-ink/30 dark:border-[var(--border-soft)]
               text-xs sm:text-sm font-mono p-4">
        quotient = stemmen / (toegekende_zetels + 1)
      </pre>
    </section>
  </section>
</template>
