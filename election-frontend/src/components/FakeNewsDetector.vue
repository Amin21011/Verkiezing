<script setup lang="ts">
import { ref } from "vue";
import { analyzeFakeNews } from "@/services/FakeNewsService";

const textInput = ref("");
const urlInput = ref("");
const loading = ref(false);
const scanStep = ref("");

const result = ref<null | {
  score: number;
  sentiment: string;
  bias: number;
  keywords: string[];
  sourceReliability: number;
  fakeNewsScore: number;
  multiLabelScores: Record<string, number>;
  verdict: string;
}>(null);

async function runAnalysis() {
  loading.value = true;
  result.value = null;

  const steps = ["Metadata ophalen...", "Taalpatronen scannen...", "AI-modellen raadplegen...", "Bias berekenen..."];
  for (const step of steps) {
    scanStep.value = step;
    await new Promise(r => setTimeout(r, 600));
  }

  try {
    result.value = await analyzeFakeNews({
      text: textInput.value.trim(),
      url: urlInput.value.trim(),
    });
  } finally {
    loading.value = false;
  }
}

const getScoreColor = (score: number) => {
  if (score > 70) return 'text-red-500';
  if (score > 40) return 'text-orange-500';
  return 'text-green-500';
};
</script>
<template>
  <section class="relative min-h-screen bg-paper text-ink dark:bg-[var(--paper)] px-2 py-16 md:py-5">
    <div class="absolute inset-0 pointer-events-none opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')]"></div>
    <div class="relative z-10 max-w-6xl mx-auto space-y-16">

      <header class="text-center border-b-2 border-ink/30 pb-10">
        <p class="font-mono text-xs uppercase tracking-[0.35em] text-graymain mb-3">
          Factcheck • Redactionele Analyse </p>

        <h1 class="font-retroHead text-4xl md:text-5xl font-extrabold tracking-tight retro-title">
          LEUGEN<span class="text-[var(--accent)]">DETECTOR</span> </h1>

        <p class="mt-4 italic text-graymain max-w-xl mx-auto">
          Een forensische analyse van nieuws, claims en bronnen —
          ontwikkeld voor kritische lezers.
        </p>
      </header>

      <section class="grid grid-cols-1 lg:grid-cols-3 gap-12">
        <div class="lg:col-span-2 bg-paper/90 dark:bg-[var(--paper)]
         border-2 border-ink/30 dark:border-[var(--border-soft)] shadow-press dark:shadow-none p-8 md:p-10">

          <label class="block font-mono text-xs uppercase tracking-widest
           text-graymain dark:text-[var(--muted)] mb-4">
            Artikel of bron invoeren
          </label>

          <input v-model="urlInput" placeholder="Plak een artikel-URL…" class="w-full mb-4 px-4 py-3
           bg-transparent border border-ink/30 dark:border-[var(--border-soft)] text-ink dark:text-[var(--ink)]
           placeholder:text-graymain dark:placeholder:text-[var(--muted)] focus:border-ink/60 dark:focus:border-[var(--ink)] outline-none" />

          <textarea v-model="textInput" rows="7"
            placeholder="Of plak hier de volledige tekst…" class="w-full px-4 py-3 bg-transparent border
            border-ink/30 dark:border-[var(--border-soft)] text-ink dark:text-[var(--ink)]
           placeholder:text-graymain dark:placeholder:text-[var(--muted)] focus:border-ink/60 dark:focus:border-[var(--ink)] outline-none resize-none"
          ></textarea>

          <button @click="runAnalysis" :disabled="loading || (!textInput && !urlInput)" class="group mt-6 w-full py-4
           font-retroHead uppercase tracking-[0.25em] border-2 border-ink/60 dark:border-[var(--border-soft)] bg-ink text-paper dark:bg-[var(--ink)] dark:text-[var(--paper)]
           shadow-press dark:shadow-none hover:bg-paper hover:text-ink dark:hover:bg-[var(--paper)] dark:hover:text-[var(--ink)] transition-all duration-300 disabled:opacity-40 disabled:cursor-not-allowed">

            <span v-if="!loading">Start analyse</span>
            <span v-else class="flex items-center justify-center gap-3 font-mono text-xs text-paper dark:text-[var(--paper)]">
            <span class="animate-pulse">✦</span>
            {{ scanStep }}
            </span>
          </button>
        </div>

        <aside class="bg-paper border-2 border-ink/40 shadow-press p-8">
          <h3 class="font-retroHead uppercase tracking-wide mb-4">
            Waar kijkt deze analyse naar?
          </h3>

          <p class="text-sm text-graymain italic mb-6">
            Deze analyse geeft geen absoluut oordeel, maar combineert meerdere meetbare signalen
            om mogelijke desinformatie te signaleren.
          </p>

          <ul class="space-y-5 text-sm">
            <li>
              <p class="font-bold"> - Taalgebruik </p>
              <p class="text-graymain text-xs">
                Worden emotioneel geladen woorden, wij/zij-taal of overdreven claims gebruikt?
              </p>
            </li>

            <li>
              <p class="font-bold"> - Bron van herkomst</p>
              <p class="text-graymain text-xs">
                Is de bron bekend, consistent en vaker betrouwbaar gebleken?
              </p>
            </li>

            <li>
              <p class="font-bold">- Patronen & afwijkingen</p>
              <p class="text-graymain text-xs">
                Wijkt de inhoud statistisch af van reguliere berichtgeving over dit onderwerp?
              </p>
            </li>
          </ul>
        </aside>
      </section>

      <transition name="fade">
        <section v-if="result" class="grid grid-cols-1 md:grid-cols-4 gap-10 animate-slideUp">
          <div class="md:col-span-1 bg-paper border-2 border-ink/40 shadow-press p-8 text-center">
            <p class="font-mono text-xs uppercase tracking-widest mb-3 text-graymain">
              Samenvatting
            </p>

            <div :class="getScoreColor(result.score)" class="text-6xl font-black mb-2">
              {{ result.score }}%
            </div>

            <p class="text-xs italic text-graymain">
              {{ result.verdict }}
            </p>

            <p class="mt-4 text-[11px] text-graymain">
              Hoe hoger de score, hoe sterker de signalen van mogelijke misleiding.
            </p>
          </div>

          <div class="md:col-span-3 bg-paper border-2 border-ink/40 shadow-press p-10">
            <h3 class="font-retroHead text-2xl mb-6">
              Onderbouwing van deze analyse
            </h3>

            <div class="grid grid-cols-1 sm:grid-cols-3 gap-8 mb-10">
              <div>
                <p class="font-mono text-xs uppercase text-graymain">Sentiment</p>
                <p class="font-bold text-lg">{{ result.sentiment }}</p>
                <p class="text-xs text-graymain">
                  Emotionele lading van de tekst.
                </p>
              </div>

              <div>
                <p class="font-mono text-xs uppercase text-graymain">Bias-score</p>
                <p class="font-bold text-lg">{{ result.bias }}/100</p>
                <p class="text-xs text-graymain">
                  Mate van eenzijdige framing.
                </p>
              </div>

              <div>
                <p class="font-mono text-xs uppercase text-graymain">Bronbetrouwbaarheid</p>
                <p class="font-bold text-lg">{{ result.sourceReliability }}%</p>
                <p class="text-xs text-graymain">
                  Historische betrouwbaarheid van de bron.
                </p>
              </div>
            </div>

            <div class="border-t border-ink/30 pt-8">
              <p class="font-mono text-xs uppercase tracking-widest text-graymain mb-4">
                Bijdragende signalen
              </p>

              <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <div v-for="(val, key) in result.multiLabelScores" :key="key">
                  <div class="flex justify-between font-mono text-xs mb-1">
                    <span class="uppercase">{{ key }}</span>
                    <span>{{ (val * 100).toFixed(0) }}%</span>
                  </div>

                  <div class="h-1 bg-ink/20">
                    <div class="h-full bg-[var(--accent)] transition-all duration-700" :style="{ width: (val * 100) + '%' }"></div>
                  </div>

                  <p class="mt-1 text-[10px] text-graymain">
                    Relatieve bijdrage aan de eindscore
                  </p>
                </div>
              </div>
            </div>
          </div>
        </section>
      </transition>
    </div>
  </section>
</template>


<style scoped>
.animate-slideUp { animation: slideUp 0.6s ease-out; }
@keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
</style>
