<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import { simulateSeats } from "@/services/SimulationService.ts";
import { normalizeParty, getPartyColor } from "@/helpers/SimulatorHelper";

import SeatChart from "@/components/SeatChart.vue";
import SeatChamber from "@/components/SeatChamber.vue";
import SeatLegend from "@/components/SeatLegend.vue";

const turnout = ref(80);
const threshold = ref(0.67);

const results = ref<{ party: string; seats: number; color: string; delta: number }[]>([]);
const votesPerSeat = ref(0);

const baseline = ref<Record<string, number>>({});

async function load() {
  const data = await simulateSeats(turnout.value, threshold.value);

  // baseline eenmalig opslaan
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
  <section class="relative mx-auto max-w-[1150px] bg-paper border border-ink/40 rounded-2xl
           shadow-[6px_6px_0_#1a1a1a] p-10 space-y-16">

    <div class="absolute inset-0 opacity-[0.06] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')] pointer-events-none"></div>

    <header class="relative z-10 space-y-4">
      <h2 class="text-4xl sm:text-5xl font-headline font-bold text-ink leading-tight">
        Zetelsimulator – Speel met de uitslag
      </h2>

      <p class="text-base sm:text-lg text-graymain max-w-2xl leading-relaxed">
        De Nederlandse Tweede Kamer heeft <strong>150 zetels</strong>.
        Met deze simulator zie je hoe veranderingen in
        <strong>opkomst</strong> en <strong>kiesdrempel</strong>
        het zetelaantal van partijen beïnvloeden.
        Handig als je wilt snappen wat er gebeurt achter de schermen van een verkiezingsuitslag.
      </p>
    </header>

    <div class="relative z-10 p-6 bg-white/60 border border-ink/20 rounded-xl shadow-[2px_2px_0_#1a1a1a] space-y-3">
      <h3 class="font-headline text-xl font-bold text-ink">🗳️ Wat je hier ziet</h3>
      <p class="text-sm sm:text-base text-ink leading-relaxed">
        Deze simulator gebruikt dezelfde rekenregels die ook bij echte verkiezingen worden toegepast.
        De berekening gebeurt via de <strong>D’Hondt-methode</strong>, die bepaalt welke partij hoeveel zetels krijgt
        op basis van het aantal stemmen.
      </p>

      <ul class="list-disc ml-6 text-sm text-graymain leading-relaxed">
        <li><strong>Opkomst</strong>: hoeveel kiezers stemmen daadwerkelijk?</li>
        <li><strong>Kiesdrempel</strong>: het minimale percentage stemmen dat een partij nodig heeft om in de Kamer te komen.</li>
        <li><strong>Δ (delta)</strong>: verandering in zetels t.o.v. de werkelijke uitslag.</li>
      </ul>
    </div>

    <div class="relative z-10 grid grid-cols-1 md:grid-cols-2 gap-12">
      <div class="space-y-4">
        <label class="font-semibold text-ink text-lg">
          Opkomst: {{ turnout }}%
        </label>

        <input type="range" min="10"
          max="100" v-model="turnout"
          class="w-full accent-ink cursor-pointer" />

        <p class="text-xs sm:text-sm text-graymain leading-relaxed max-w-sm">
          De opkomst bepaalt het totaal aantal uitgebrachte stemmen.
          <strong>Hoe hoger de opkomst</strong>, hoe meer stemmen er te verdelen zijn,
          en hoe kleiner de invloed van kleine fluctuaties.
        </p>
      </div>

      <div class="space-y-4">
        <label class="font-semibold text-ink text-lg">
          Kiesdrempel: {{ threshold }}%
        </label>

        <input type="range"
          min="0"
          max="5"
          step="0.1"
          v-model="threshold"
          class="w-full accent-ink cursor-pointer" />

        <p class="text-xs sm:text-sm text-graymain leading-relaxed max-w-sm">
          De kiesdrempel bepaalt welke partijen überhaupt zetels mogen krijgen.
          In Nederland is er officieel <strong>geen harde drempel</strong>,
          maar de natuurlijke kiesdrempel is <strong>0.67%</strong> (≈ één zetel).
        </p>
      </div>
    </div>

    <div class="relative z-10 p-5 bg-paper border border-ink/20 rounded-xl shadow-sm">
      <p class="text-sm sm:text-base text-graymain">
        <strong class="text-ink">Stemmen per zetel:</strong>
        Gemiddeld zijn er momenteel
        <strong>{{ votesPerSeat.toLocaleString() }}</strong>
        stemmen nodig voor één zetel.
      </p>
    </div>

    <div class="relative z-10 space-y-4">
      <h3 class="text-lg font-headline font-bold text-ink">📊 Aandeel per partij</h3>
      <SeatChart :data="results" />
    </div>

    <div class="relative z-10 space-y-4">
      <h3 class="text-lg font-headline font-bold text-ink">
        Tweede Kamer — 150 zetels 🪑
      </h3>

      <p class="text-sm text-graymain max-w-xl">
        De halfronde kamer hieronder toont elke zetel als één cirkel.
        De kleur geeft aan bij welke partij de zetel hoort.
        Beweeg je muis over een zetel voor details.
      </p>

      <SeatChamber :data="results" :votesPerSeat="votesPerSeat" />
    </div>

    <div class="relative z-10">
      <SeatLegend :data="results" />
    </div>
    <div class="relative z-10 p-6 bg-paper border border-ink/30 rounded-xl
         shadow-[3px_3px_0_#1a1a1a] space-y-4">
      <h3 class="font-headline text-2xl font-bold text-ink flex items-center gap-2">
        Hoe werkt de D’Hondt-methode? ⚖️
      </h3>

      <p class="text-sm sm:text-base text-graymain leading-relaxed">
        De <strong>D’Hondt-methode</strong> is de officiële manier waarop Nederland zetels verdeelt.
        Het systeem is ontworpen om zo eerlijk mogelijk te verdelen tussen grote én kleine partijen,
        zonder dat micro-partijen met slechts enkele stemmen direct een zetel krijgen.
      </p>

      <div class="bg-white/70 border border-ink/20 rounded-xl p-5 shadow-inner backdrop-blur-sm space-y-3">
        <p class="text-sm sm:text-base text-ink font-semibold">
          Voor elke partij worden steeds opnieuw “quotients” berekend:
        </p>

        <pre class="bg-[#faf7f2] border border-ink/20 text-ink rounded-lg p-4 text-xs sm:text-sm font-mono shadow-[2px_2px_0_#1a1a1a] leading-relaxed">
          quotient = stemmen / (aantal_reeds_toegekende_zetels + 1)
        </pre>

        <p class="text-sm sm:text-base text-graymain leading-relaxed">
          In elke ronde krijgt de partij met het <strong>hoogste quotient</strong> een zetel.
          Daarna wordt voor die partij opnieuw een quotient berekend, met één zetel extra,
          waardoor de kans “eerlijker” wordt verdeeld over de resterende partijen.
        </p>
      </div>


      <ul class="list-disc ml-5 text-sm sm:text-base text-graymain space-y-1">
        <li>
          Grote partijen krijgen sneller zetels, maar verliezen daarna aan quotient → kleinere partijen komen alsnog aan bod.
        </li>
        <li>
          Er worden in totaal altijd precies <strong>150 zetels</strong> verdeeld.
        </li>
        <li>
          Partijen onder de <strong>(natuurlijke) kiesdrempel</strong> van ± 0.67% krijgen geen zetel.
        </li>
      </ul>

      <p class="text-sm sm:text-base text-ink leading-relaxed pt-2">
        Dankzij deze methode zie je in de simulator waarom sommige partijen
        <strong>net één zetel verliezen of winnen</strong> wanneer je de opkomst of kiesdrempel verandert.
      </p>
    </div>

  </section>
</template>
