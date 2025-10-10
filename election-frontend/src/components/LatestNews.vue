<script setup lang="ts">
import { ref, onMounted } from "vue";

interface NewsItem {
  title: string;
  link: string;
  description: string;
  publishedAt: string | null;
}

const API_URL = `${import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080"}/api/news/rijksoverheid?limit=6`;

const newsItems = ref<NewsItem[]>([]);
const loading = ref(true);
const errorMsg = ref("");

// Datum formatteren
function formatDate(iso: string | null): string {
  if (!iso) return "";
  try {
    return new Date(iso).toLocaleString("nl-NL", {
      day: "numeric",
      month: "long",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch {
    return "";
  }
}

async function loadNews() {
  loading.value = true;
  errorMsg.value = "";

  try {
    const res = await fetch(API_URL, { credentials: "include" });
    if (!res.ok) throw new Error(`HTTP fout: ${res.status}`);

    const raw: NewsItem[] = await res.json();

    newsItems.value = raw.map((it) => ({
      ...it,
      publishedAt: formatDate(it.publishedAt),
    }));
  } catch (err) {
    errorMsg.value = err instanceof Error ? err.message : "Onbekende fout";
    console.error("Fout bij het laden van nieuws:", err);
  } finally {
    loading.value = false;
  }
}

onMounted(loadNews);
</script>



<template>
  <div class="min-h-screen bg-[#F8F7F3] py-10 px-6">
    <h1 class="text-3xl font-bold text-center mb-8 text-gray-800">
       Laatste nieuws
    </h1>

    <div v-if="loading" class="text-center text-gray-500 text-lg">
      Bezig met laden...
    </div>

    <div v-else>
      <div v-if="errorMsg" class="text-red-500 text-center mb-4">
        Fout: {{ errorMsg }}
      </div>

      <div v-else class="max-w-4xl mx-auto grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <article
            v-for="(item, idx) in newsItems"
            :key="idx"
            class="bg-white rounded-2xl border border-gray-200 p-6 shadow-sm hover:shadow-md transition"
        >
          <a :href="item.link" target="_blank" class="block text-lg font-semibold text-[#00712D] hover:underline">
            {{ item.title }}
          </a>
          <p class="text-sm text-gray-500 mt-1">
            {{ item.publishedAt }}
          </p>
          <p class="text-gray-700 mt-3 leading-relaxed line-clamp-6">
            {{ item.description }}
          </p>
        </article>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>

