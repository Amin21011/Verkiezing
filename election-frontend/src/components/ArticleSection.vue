<script setup lang="ts">
import { ref, onMounted, computed } from "vue"
import ArticleGridCard from "./ArticleGridCard.vue"

interface NewsItem {
  title: string
  link: string
  description: string
  publishedAt: string | null
}

const API_URL = `${import.meta.env.VITE_API_URL}/news/rijksoverheid?limit=4`
const newsItems = ref<NewsItem[]>([])
const loading = ref(true)
const errorMsg = ref("")

function formatDate(iso: string | null): string {
  if (!iso) return ""
  try {
    return new Date(iso).toLocaleDateString("nl-NL", {
      day: "numeric",
      month: "long",
      year: "numeric",
    })
  } catch {
    return ""
  }
}

async function loadNews() {
  try {
    const res = await fetch(API_URL)
    if (!res.ok) throw new Error(`HTTP fout: ${res.status}`)
    const raw: NewsItem[] = await res.json()

    newsItems.value = raw.map((it) => ({
      ...it,
      publishedAt: formatDate(it.publishedAt),
    }))
  } catch (err) {
    errorMsg.value = err instanceof Error ? err.message : "Onbekende fout"
  } finally {
    loading.value = false
  }
}

onMounted(loadNews)

const featured = computed(() => newsItems.value[0])
const rest = computed(() => newsItems.value.slice(1))
</script>

<template>
  <section class="bg-paper text-ink py-20 px-4 md:px-8">
    <div class="max-w-7xl mx-auto">
      <header class="text-center mb-16 max-w-3xl mx-auto">
      <span class="inline-block font-mono uppercase tracking-widest text-sm text-graymain border-b-4 border-[var(--accent)] pb-1 dark:text-[#b9b2a5] dark:border-[#b9b2a5]">
        Actueel
      </span>

        <h2 class="mt-5 font-headline text-3xl md:text-5xl font-extrabold uppercase tracking-tight text-ink">
          Laatste Overheidsnieuws </h2>

        <p class="mt-4 text-sm text-graymain dark:text-[#c9c2b4]">
          Betrouwbare updates en officiële aankondigingen die invloed hebben op jouw stem. </p>
      </header>

      <article v-if="featured" class="group relative mb-20 pb-8 border-y-2 border-ink bg-paper hover:bg-white/60 dark:hover:bg-black/20 transition-colors">
        <div class="max-w-4xl mx-auto px-1">
          <div class="flex items-center gap-3 pt-6 mb-4 text-xs font-mono uppercase tracking-widest text-graymain dark:text-[#b9b2a5]">
            <span>Rijksoverheid</span>
            <span>—</span>
            <time>{{ featured.publishedAt }}</time>
          </div>

          <h3 class="font-headline text-2xl md:text-3xl font-extrabold leading-snug text-ink mb-3 group-hover:underline underline-offset-4">
            {{ featured.title }} </h3>

          <p class="font-body text-base md:text-lg leading-relaxed max-w-prose line-clamp-3 text-graymain dark:text-[#c9c2b4]">
            {{ featured.description }} </p>

          <div class="mt-4">
            <a :href="featured.link" target="_blank" rel="noopener" class="inline-flex items-center gap-1 font-mono text-xs uppercase tracking-widest text-ink dark:text-[#e6e0d6] hover:underline underline-offset-4">
              Lees artikel
              <span class="transition-transform group-hover:translate-x-1">→</span>
            </a>
          </div>
        </div>
        <a :href="featured.link" target="_blank" rel="noopener" class="absolute inset-0" aria-label="Lees featured artikel"></a>
      </article>

      <div class="grid gap-10 sm:grid-cols-1 md:grid-cols-3">
        <ArticleGridCard v-for="(item, i) in rest" :key="i" :title="item.title" :summary="item.description" :date="item.publishedAt" category="Rijksoverheid" :link="item.link" />
      </div>
      <div class="mt-20 h-[6px] bg-gradient-to-r from-[var(--accent)] via-[var(--highlight)] to-[var(--secondary)] dark:from-[#b9b2a5] dark:via-[#c9c2b4] dark:to-[#e6e0d6]"></div>
    </div>
  </section>
</template>
