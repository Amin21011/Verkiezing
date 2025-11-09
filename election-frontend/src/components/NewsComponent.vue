<script setup lang="ts">
import { ref, onMounted } from "vue";

interface NewsItem {
  title: string;
  link: string;
  description: string;
  publishedAt: string | null;
}

const API_URL = `${import.meta.env.VITE_API_URL}/news/rijksoverheid?limit=6`;


const newsItems = ref<NewsItem[]>([]);
const loading = ref(true);
const errorMsg = ref("");


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
    const res = await fetch(API_URL);
    if (!res.ok)  new Error(`HTTP fout: ${res.status}`);

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
    <!-- Nieuws sectie -->
    <section
      class="max-w-5xl mx-auto bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden"
    >
      <header class="px-6 pt-5 pb-3">
        <h2 class="text-center text-lg font-semibold text-gray-800">
          Laatste Nieuws
        </h2>
      </header>
      <div class="h-px bg-gray-200 mx-6"></div>

      <div v-if="loading" class="py-10 text-center text-gray-500 text-lg">
        Bezig met laden…
      </div>
      <div v-else-if="errorMsg" class="py-4 text-red-500 text-center">
        Fout: {{ errorMsg }}
      </div>

      <!-- 3x2 grid -->
      <div v-else class="p-6">
        <div
          class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 justify-items-center"
        >
          <article
            v-for="(item, idx) in newsItems.slice(0, 6)"
            :key="idx"
            class="bg-white rounded-xl border border-gray-200 p-5 shadow-sm hover:shadow-md transition w-full max-w-xs"
          >
            <a
              :href="item.link"
              target="_blank"
              class="block text-base font-semibold text-[#00712D] hover:underline"
            >
              {{ item.title }}
            </a>
            <p class="text-xs text-gray-500 mt-1">{{ item.publishedAt }}</p>
            <p class="text-gray-700 mt-3 leading-relaxed line-clamp-5">
              {{ item.description }}
            </p>
          </article>
        </div>
      </div>
    </section>

  </div>
</template>

<style scoped>
</style>

