<script setup lang="ts">
import { ref, onMounted } from "vue";
import {getToken} from "@/services/authService.ts";
import router from "@/router";

const API_URL = import.meta.env.VITE_API_URL;

interface Topic {
  id: number;
  name: string;
}

interface ForumPost {
  id: number;
  title: string;
  content: string;
  postedAt: string;
  likeCount: number;
  user: { name: string };
  topic?: Topic;
}

const posts = ref<ForumPost[]>([]);
const trending = ref<ForumPost[]>([]);

const loading = ref(true);
const errorMsg = ref("");

const showLoginPrompt = ref(false);

async function fetchPosts() {
  loading.value = true;
  try {
    const res = await fetch(`${API_URL}/forum/posts`);
    if (!res.ok) throw new Error("Kon posts niet ophalen");
    const data = await res.json();

    posts.value = data;


    trending.value = [...data]
      .sort((a, b) => b.likeCount - a.likeCount)
      .slice(0, 5);

  } catch (err: unknown) {
    if (err instanceof Error) {
      errorMsg.value = err.message;
    } else {
      errorMsg.value = "Er is een onbekende fout opgetreden";
    }
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
  <div v-if="showLoginPrompt" class="fixed inset-0 z-[9999] bg-black/50 backdrop-blur-sm flex items-center justify-center">
    <div class="bg-paper border-2 border-ink shadow-hard p-10 max-w-md w-full text-center">
      <h2 class="text-3xl font-serif font-black uppercase mb-4">
        Editorial Access
      </h2>
      <p class="italic text-graymain mb-8">
        Log in om deel te nemen aan het publieke debat.
      </p>

      <div class="flex justify-center gap-6">
        <button @click="goToLogin" class="btn-primary px-6 py-2">
          Inloggen
        </button>
        <button @click="showLoginPrompt = false" class="border border-ink px-6 py-2 uppercase text-xs tracking-widest">
          Sluiten
        </button>
      </div>
    </div>
  </div>

  <div class="min-h-screen bg-paper text-ink font-sans px-6 py-12">
    <div class="max-w-7xl mx-auto space-y-8">

      <header class="text-center border-ink pb-2">
        <p class="uppercase tracking-widest text-xs mb-3">
          Publiek Debat · Opinie · Analyse
        </p>

        <h1 class="text-6xl md:text-4xl font-serif font-black uppercase leading-tight">
          Het Forum</h1>

        <p class="max-w-xl mx-auto mt-6 italic text-graymain text-md">
          Waar meningen botsen, ideeën ontstaan en het publieke gesprek vorm krijgt.</p>
      </header>

      <div class="grid grid-cols-1 md:grid-cols-4 gap-10">

        <aside class="md:col-span-1 border-r-2 border-ink pr-6">
          <h3 class="font-serif font-bold uppercase mb-6 text-lg">
            🔥 Trending
          </h3>

          <ul class="space-y-6">
            <li v-for="post in trending" :key="post.id" class="cursor-pointer group" @click="$router.push(`/forum/${post.id}`)">
              <p class="font-semibold group-hover:underline">
                {{ post.title }}
              </p>
              <p class="text-xs text-graymain">
                ❤️ {{ post.likeCount }} waarderingen
              </p>
            </li>
          </ul>
        </aside>

        <main class="md:col-span-2 max-h-[65vh] overflow-y-auto pr-4 space-y-8 custom-scrollbar">
          <div v-if="loading" class="text-center italic text-graymain">
            Artikelen worden gedrukt…
          </div>

          <div v-else-if="errorMsg" class="text-center text-red-600">
            {{ errorMsg }}
          </div>

          <article v-for="(post, index) in posts" :key="post.id" class="pb-8 border-b border-ink/30 cursor-pointer hover:bg-black/5 transition px-2" @click="$router.push(`/forum/${post.id}`)">

            <h2 :class="index === 0 ? 'text-2xl font-serif font-black' : 'text-xl font-serif font-bold'">
              {{ post.title }}
            </h2>

            <p class="text-xs text-graymain mt-2 mb-3">
              door {{ post.user?.name || 'Onbekend' }} ·
              {{ new Date(post.postedAt).toLocaleDateString('nl-NL') }} ·
              ❤️ {{ post.likeCount }}
            </p>

            <p v-if="post.topic" class="italic text-xs text-graymain mb-3">
              Rubriek: {{ post.topic.name }}
            </p>

            <p class="leading-relaxed line-clamp-4">
              {{ post.content }}
            </p>
          </article>
        </main>

        <aside class="md:col-span-1 flex items-start justify-center">
          <div class="border-2 border-ink shadow-hard p-8 text-center max-w-xs">
            <p class="font-serif font-bold uppercase text-xl mb-4">
              Schrijf mee
            </p>

            <p class="text-sm italic text-graymain mb-6">
              Jouw stem hoort ook in deze krant.
            </p>

            <button @click="requireLogin() ? $router.push('/post') : null" class="btn-primary px-6 py-2">
              Nieuw artikel
            </button>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>
