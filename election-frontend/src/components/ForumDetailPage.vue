<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";

interface ForumPost {
  id: number;
  title: string;
  content: string;
  postedAt: string;
  user: { name: string };
}

const route = useRoute();
const post = ref<ForumPost | null>(null);
const loading = ref(true);
const errorMsg = ref("");

async function fetchPost() {
  loading.value = true;
  try {
    const id = route.params.id;
    const res = await fetch(`http://localhost:8080/api/forum/posts/${id}`);
    if (!res.ok) throw new Error("Kon vraag niet ophalen");
    post.value = await res.json();
  } catch (err: any) {
    errorMsg.value = err.message;
  } finally {
    loading.value = false;
  }
}

onMounted(fetchPost);
</script>

<template>
  <div class="min-h-screen bg-[#F8F7F3] text-gray-900 font-sans py-10 px-6">
    <section
      class="max-w-3xl mx-auto bg-white rounded-2xl border border-gray-200 shadow-sm p-8"
    >
      <div v-if="loading" class="text-center text-gray-500">Laden...</div>
      <div v-else-if="errorMsg" class="text-center text-red-500">
        {{ errorMsg }}
      </div>

      <div v-else>
        <h1 class="text-2xl font-bold text-[#00712D] mb-2">{{ post?.title }}</h1>
        <p class="text-sm text-gray-500 mb-4">
          door {{ post?.user?.name || "Onbekend" }} •
          {{ new Date(post?.postedAt || "").toLocaleDateString("nl-NL") }}
        </p>
        <p class="text-gray-800 leading-relaxed whitespace-pre-line">
          {{ post?.content }}
        </p>
      </div>
    </section>
  </div>
</template>

<style scoped></style>
