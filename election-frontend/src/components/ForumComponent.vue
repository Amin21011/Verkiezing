<script setup lang="ts">
import { ref, onMounted } from "vue";

interface ForumPost {
  id: number;
  title: string;
  content: string;
  postedAt: string;
  user: { name: string };
}

const posts = ref<ForumPost[]>([]);
const loading = ref(true);
const errorMsg = ref("");

async function fetchRecentPosts() {
  loading.value = true;
  try {
    const res = await fetch("http://localhost:8080/api/forum/posts");
    if (!res.ok) throw new Error("Kon posts niet ophalen");
    const data = await res.json();
    posts.value = data.slice(0, 5); // alleen de 5 meest recente posts
  } catch (err: any) {
    errorMsg.value = err.message;
  } finally {
    loading.value = false;
  }
}

onMounted(fetchRecentPosts);
</script>

<template>
  <div class="min-h-screen bg-[#F8F7F3] text-gray-900 font-sans py-10 px-6">
    <section
      class="max-w-4xl mx-auto bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden"
    >
      <header class="px-6 pt-5 pb-3 border-b border-gray-200">
        <h2 class="text-center text-lg font-semibold text-gray-800">
          Recente Forumvragen
        </h2>
      </header>

      <div v-if="loading" class="py-10 text-center text-gray-500 text-lg">
        Bezig met laden…
      </div>
      <div v-else-if="errorMsg" class="py-4 text-red-500 text-center">
        Fout: {{ errorMsg }}
      </div>

      <div v-else class="divide-y divide-gray-200">
        <article
          v-for="post in posts"
          :key="post.id"
          class="p-6 hover:bg-gray-50 transition cursor-pointer"
          @click="$router.push(`/forum/${post.id}`)"
        >
          <h3 class="text-lg font-semibold text-[#00712D] mb-1">
            {{ post.title }}
          </h3>
          <p class="text-sm text-gray-500 mb-2">
            door {{ post.user?.name || "Onbekend" }} •
            {{ new Date(post.postedAt).toLocaleDateString("nl-NL") }}
          </p>
          <p class="text-gray-700 leading-relaxed line-clamp-3">
            {{ post.content }}
          </p>
        </article>

      </div>
    </section>
  </div>
</template>

<style scoped></style>
