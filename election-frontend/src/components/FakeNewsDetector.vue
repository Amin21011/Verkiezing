<script setup lang="ts">
import { ref } from "vue";
import { analyzeFakeNews } from "@/services/FakeNewsService.ts";

const textInput = ref("");
const urlInput = ref("");
const loading = ref(false);

const result = ref<null | {
  score: number;
  sentiment: string;
  bias: number;
  keywords: string[];
  sourceReliability: number;
  fakeNewsScore: number;
  multiLabelScores: Record<string, number>;
}>(null);

async function runAnalysis() {
  loading.value = true;
  result.value = null;

  try {
    result.value = await analyzeFakeNews({
      text: textInput.value.trim(),
      url: urlInput.value.trim(),
    });
  } catch (err) {
    console.error("Fake news analysis failed:", err);
  } finally {
    loading.value = false;
  }
}

function formatLabel(key: string) {
  return key.replace(/_/g, " ").replace(/\b\w/g, (c) => c.toUpperCase());
}
</script>

<template>
  <section class="relative mx-auto max-w-[1150px] bg-paper border border-ink/40 rounded-2xl
           shadow-[6px_6px_0_#1a1a1a] p-10 space-y-16 animate-fadeIn">

    <div class="absolute inset-0 opacity-[0.06]
        bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')]
        pointer-events-none"></div>

    <header class="relative z-10 space-y-6">
      <h2 class="text-4xl sm:text-5xl font-headline font-bold text-ink leading-tight">
        📰 Nep Nieuws Detector
      </h2>

      <p class="text-base sm:text-lg text-graymain max-w-3xl leading-relaxed">
        Deze scanner combineert <strong>NLP-regels</strong>,
        <strong>AI-classificatie</strong> en <strong>broncontrole</strong> om te bepalen
        hoe waarschijnlijk het is dat een artikel misleidend of nep is.
      </p>

      <div class="p-4 bg-white/60 border border-ink/20 rounded-xl text-graymain text-sm leading-relaxed">
        <strong>Hoe werkt het?</strong><br>
        • De tekst wordt geanalyseerd op toon, keywords, hoofdletters en bias.<br>
        • Een AI-model beoordeelt of de inhoud lijkt op eerdere nepnieuwsvoorbeelden.<br>
        • Een tweede AI-model kijkt naar labels zoals “disinformation”, “real” en “clickbait”.<br>
        • Alle resultaten worden gecombineerd tot één duidelijke eindscore.
      </div>
    </header>

    <div class="relative z-10 p-6 bg-white/60 border border-ink/20 rounded-xl shadow-[2px_2px_0_#1a1a1a] space-y-6">
      <div class="space-y-2">
        <label class="font-headline text-lg text-ink">🔗 Artikel URL</label>
        <input v-model="urlInput" class="w-full bg-white border border-ink/30 rounded-lg p-3 text-sm shadow-inner
                 focus:outline-none focus:ring-2 focus:ring-ink"
               placeholder="https://www.nieuwssite.nl/artikel..." />
      </div>

      <div class="space-y-2">
        <label class="font-headline text-lg text-ink">✍️ Artikeltekst</label>
        <textarea v-model="textInput" rows="7"
                  class="w-full bg-white border border-ink/30 rounded-lg p-3 text-sm shadow-inner
                 focus:outline-none focus:ring-2 focus:ring-ink"
                  placeholder="Plak hier nieuwsartikel tekst..."></textarea>
      </div>

      <button @click="runAnalysis" :disabled="loading"
              class="bg-gray-300 text-black px-6 py-3 text-lg rounded-xl shadow-[3px_3px_0_#1a1a1a]
               hover:translate-y-[1px] hover:shadow-[2px_2px_0_#1a1a1a]
               disabled:opacity-40 disabled:cursor-not-allowed transition">
        {{ loading ? "Analyseren..." : "Analyseer artikel" }}
      </button>
    </div>

    <div v-if="result" class="relative z-10 p-8 bg-paper border border-ink/40 rounded-xl shadow-[4px_4px_0_#1a1a1a]
             space-y-10 animate-fadeIn">

      <h3 class="text-3xl font-headline font-bold text-ink">📉 Analyse Resultaat</h3>

      <div>
        <p class="text-xl font-semibold text-ink">🔍 Eindscores</p>
        <p class="text-graymain text-sm mb-2">
          Combinatie van NLP-signalen, broncontrole en AI-modellen.</p>

        <div class="text-6xl font-bold font-headline">{{ result.score }}%</div>

        <div class="h-4 w-full mt-3 bg-white border border-ink rounded-full overflow-hidden">
          <div class="h-full bg-red-500 transition-all duration-500"
               :style="{ width: result.score + '%' }"></div>
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 gap-10 pt-6">
        <div class="space-y-4">
          <p><strong>📌 Sentiment:</strong> {{ result.sentiment }}</p>
          <p><strong>📌 NLP Bias Score:</strong> {{ result.bias }}/100</p>
          <p><strong>📌 Bronbetrouwbaarheid:</strong> {{ result.sourceReliability }}/100</p>

          <div>
            <p class="font-semibold text-ink">⚠️ Gevonden Keywords:</p>
            <ul class="list-disc ml-5 text-sm text-graymain">
              <li v-for="k in result.keywords" :key="k">{{ k }}</li>
            </ul>
          </div>
        </div>

        <div class="space-y-6">
          <div>
            <p class="text-lg font-semibold text-ink">🤖 AI Fake News Score:</p>
            <p class="text-3xl font-bold">{{ result.fakeNewsScore }}%</p>
          </div>

          <div>
            <p class="text-lg font-semibold text-ink">🧠 Multi-Label Analyse:</p>
            <p class="text-graymain text-sm">Hoe AI het artikel classificeert.</p>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 pt-2">
              <div v-for="(val, key) in result.multiLabelScores" :key="key" class="space-y-1">
                <p class="text-sm font-semibold text-ink">
                  {{ formatLabel(key) }} — {{ (val * 100).toFixed(0) }}%
                </p>

                <div class="h-3 w-full bg-white border border-ink rounded-full overflow-hidden">
                  <div class="h-full bg-gray-800 transition-all duration-500"
                       :style="{ width: (val * 100) + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <p v-else-if="!loading" class="relative z-10 text-graymain text-sm">
      Vul een URL of tekst in om te beginnen met analyseren.
    </p>

  </section>
</template>
