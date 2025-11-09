<script setup lang="ts">
import { ref, onMounted } from 'vue';

interface NewsItem {
  title: string;
  link: string;
  description: string;
  publishedAt: string | null;
}

const API_URL = `${import.meta.env.VITE_API_URL}news/rijksoverheid?limit=6`;

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
    if (!res.ok) new Error(`HTTP fout: ${res.status}`);

    const raw: NewsItem[] = await res.json();

    newsItems.value = raw.map((it) => ({
      ...it,
      publishedAt: formatDate(it.publishedAt)
    }));
  } catch (err) {
    errorMsg.value = err instanceof Error ? err.message : "onbekende fout";
    console.error("Fout bij het laden van nieuws:", err);
  } finally {
    loading.value = false;
  }
}

onMounted(loadNews)
</script>

<template>
  <div>
    <div v-if="loading" class="text-center text-gray-500 text-lg">
      Bezig met laden...
    </div>

    <div v-else>
      <div v-if="errorMsg" class="text-red-500 text-center mb-4">
        Fout: {{ errorMsg }}
      </div>

      <div
        v-else
        class="max-w-[32rem] mx-auto overflow-x-auto pb-3 scrollbar-thin scrollbar-thumb-gray-300"
      >
        <div class="flex gap-4">
          <article
            v-for="(item, idx) in newsItems"
            :key="idx"
            class="bg-white rounded-xl border border-gray-200 p-4 shadow-sm hover:shadow-md transition flex-shrink-0 w-64"
          >
            <a
              :href="item.link"
              target="_blank"
              class="block text-base font-semibold text-[#00712D] hover:underline"
            >
              {{ item.title }}
            </a>
            <p class="text-xs text-gray-500 mt-1">
              {{ item.publishedAt }}
            </p>
            <p class="text-sm text-gray-700 mt-2 leading-snug line-clamp-4">
              {{ item.description }}
            </p>
          </article>
        </div>
      </div>
    </div>
  </div>


</template>

<style scoped></style>
