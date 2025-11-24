<script setup lang="ts">
import { ref, onMounted } from "vue";
import {getToken} from "@/services/authService.ts";
import router from "@/router";

interface ForumPost {
  id: number;
  title: string;
  content: string;
  postedAt: string;
  likeCount: number;
  user: { name: string };
}

const posts = ref<ForumPost[]>([]);
const trending = ref<ForumPost[]>([]);

const loading = ref(true);
const errorMsg = ref("");

const showLoginPrompt = ref(false);

async function fetchPosts() {
  loading.value = true;
  try {
    const res = await fetch("http://localhost:8080/api/forum/posts");
    if (!res.ok) throw new Error("Kon posts niet ophalen");
    const data = await res.json();

    posts.value = data;


    trending.value = [...data]
      .sort((a, b) => b.likeCount - a.likeCount)
      .slice(0, 5);

  } catch (err: any) {
    errorMsg.value = err.message;
  } finally {
    loading.value = false;
  }
}

function requireLogin() {
  if (!getToken()) {
    showLoginPrompt.value = true;
    return false;
  }
  return true;
}

function goToLogin() {
  showLoginPrompt.value = false;
  router.push("/login");
}

onMounted(fetchPosts);
</script>


<template>

  <div
    v-if="showLoginPrompt"
    class="fixed inset-0 bg-black/50 flex items-center justify-center z-[999]"
  >
    <div class="bg-white border-4 border-ink shadow-press p-8 rounded-3xl max-w-sm w-full text-center">
      <h2 class="text-2xl font-headline font-bold uppercase mb-4 text-ink">
        Inloggen vereist
      </h2>

      <p class="text-graymain mb-6 font-semibold">
        Je moet ingelogd zijn om deze actie uit te voeren.
      </p>

      <div class="flex justify-center gap-4">
        <button
          @click="goToLogin"
          class="btn-primary px-6 py-2"
        >
          Inloggen
        </button>

        <button
          @click="showLoginPrompt = false"
          class="px-6 py-2 border-2 border-ink bg-white hover:bg-gray-100 uppercase font-bold tracking-wider"
        >
          Sluiten
        </button>
      </div>
    </div>
  </div>

  <div class="min-h-screen bg-[#F8F7F3] text-gray-900 font-sans py-10 px-6">
    <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-4 gap-8">

      <aside class="md:col-span-1">
        <div class="bg-white rounded-2xl border border-gray-200 shadow-sm p-6">
          <h3 class="text-lg font-semibold text-[#00712D] mb-4">🔥 Trending Posts</h3>

          <ul class="space-y-4">
            <li
              v-for="post in trending"
              :key="post.id"
              class="cursor-pointer group"
              @click="$router.push(`/forum/${post.id}`)"
            >
              <p class="font-medium text-gray-800 group-hover:text-[#00712D] transition">
                {{ post.title }}
              </p>
              <p class="text-sm text-gray-500">
                ❤️ {{ post.likeCount }} likes
              </p>
            </li>
          </ul>
        </div>
      </aside>

      <main class="md:col-span-2 max-h-[600px] overflow-y-auto pr-2 custom-scrollbar">
        <section class="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
          <header class="px-6 pt-5 pb-3 border-b border-gray-200">
            <h2 class="text-center text-lg font-semibold text-gray-800">Alle Forum Posts</h2>
          </header>

          <div v-if="loading" class="py-10 text-center text-gray-500 text-lg">Bezig met laden…</div>
          <div v-else-if="errorMsg" class="py-4 text-red-500 text-center">{{ errorMsg }}</div>

          <div v-else class="divide-y divide-gray-200">
            <article
              v-for="post in posts"
              :key="post.id"
              class="p-6 hover:bg-gray-50 transition cursor-pointer"
              @click="$router.push(`/forum/${post.id}`)"
            >
              <h3 class="text-lg font-semibold text-[#00712D] mb-1">{{ post.title }}</h3>
              <p class="text-sm text-gray-500 mb-2">
                door {{ post.user?.name || 'Onbekend' }} •
                {{ new Date(post.postedAt).toLocaleDateString('nl-NL') }} •
                ❤️ {{ post.likeCount }}
              </p>
              <p class="text-gray-700 leading-relaxed line-clamp-3">
                {{ post.content }}
              </p>
            </article>
          </div>
        </section>
      </main>

      <aside class="md:col-span-1 flex justify-center md:justify-end items-start h-auto">
        <div class="bg-[#00712D] text-white rounded-3xl px-8 py-6 shadow-xl w-[300px] flex flex-col items-center">
          <p class="text-xl font-semibold mb-4 text-center">
            Deel nu ook jouw<br />mening!
          </p>

          <button
            @click="requireLogin() ? $router.push('/post') : null"
            class="bg-white text-[#00712D] font-semibold px-5 py-2 rounded-xl shadow-md hover:bg-gray-200 transition"
          >
            Maak een nieuwe post
          </button>
        </div>
      </aside>

    </div>
  </div>

</template>



<style scoped></style>
