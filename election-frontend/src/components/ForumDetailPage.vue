<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getToken } from "@/services/authService";

interface User {
  name: string;
}

interface Topic {
  id: number;
  name: string;
}

interface ForumComment {
  id: number;
  comment: string;
  createdAt: string;
  user: User;
}

interface ForumPost {
  id: number;
  title: string;
  content: string;
  postedAt: string;
  user: User;
  topic?: Topic;
  comments?: ForumComment[];
  likeCount?: number;
  dislikeCount?: number;
}

const route = useRoute();
const router = useRouter();

const post = ref<ForumPost | null>(null);
const newComment = ref("");
const loading = ref(true);
const errorMsg = ref("");
const submitting = ref(false);

const API_URL = import.meta.env.VITE_API_URL

const showLoginPrompt = ref(false);

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

async function fetchPost() {
  loading.value = true;
  try {
    const id = route.params.id;
    const res = await fetch(`${API_URL}/forum/posts/${id}`);
    if (!res.ok) throw new Error("Kon vraag niet ophalen");
    post.value = await res.json() as ForumPost;
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

async function submitComment() {
  if (!requireLogin()) return;
  if (!newComment.value.trim()) return;

  submitting.value = true;
  const token = getToken();
  const id = route.params.id;

  try {
    const res = await fetch(`http://localhost:8080/api/forum/posts/${id}/comments`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify({ comment: newComment.value }),
    });

    if (!res.ok) throw new Error("Kon reactie niet plaatsen");

    newComment.value = "";
    await fetchPost();
  } catch (err: unknown) {
    if (err instanceof Error) alert(err.message);
  } finally {
    submitting.value = false;
  }
}

async function likePost() {
  if (!requireLogin()) return;

  const token = getToken();
  const id = route.params.id;

  const res = await fetch(`${API_URL}/forum/posts/${id}/like`, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });

  if (res.ok) await fetchPost();
}

async function dislikePost() {
  if (!requireLogin()) return;

  const token = getToken();
  const id = route.params.id;

  const res = await fetch(`${API_URL}/forum/posts/${id}/dislike`, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });

  if (res.ok) await fetchPost();
}

onMounted(fetchPost);
</script>
<template>
  <div v-if="showLoginPrompt" class="fixed inset-0 z-[9999] bg-black/50 backdrop-blur-sm flex items-center justify-center">
    <div class="bg-paper border-2 border-ink shadow-hard p-10 max-w-md w-full text-center">
      <h2 class="text-3xl font-serif font-black uppercase mb-4">
        Inloggen vereist
      </h2>
      <p class="italic text-graymain mb-8">
        Log in om deel te nemen aan het debat.
      </p>
      <div class="flex justify-center gap-6">
        <button @click="goToLogin" class="btn-primary px-6 py-2">
          Inloggen
        </button>
        <button
          @click="showLoginPrompt = false" class="border border-ink px-6 py-2 uppercase text-xs tracking-widest">
          Sluiten
        </button>
      </div>
    </div>
  </div>

  <div class="min-h-screen bg-paper text-ink font-sans px-6 py-12">
    <section class="max-w-3xl mx-auto border-2 border-ink shadow-press p-10">
      <div v-if="loading" class="text-center italic text-graymain">
        Artikel wordt geladen…
      </div>

      <div v-else-if="errorMsg" class="text-center text-red-600">
        {{ errorMsg }}
      </div>

      <article v-else class="space-y-10">
        <header>
          <p v-if="post?.topic" class="uppercase tracking-widest text-xs mb-3">
            {{ post.topic.name }}
          </p>

          <h1 class="text-4xl md:text-4xl font-serif font-black leading-tight">
            {{ post?.title }}
          </h1>

          <p class="text-xs text-graymain mt-4">
            door {{ post?.user?.name || "Onbekend" }} ·
            {{ new Date(post?.postedAt || "").toLocaleDateString("nl-NL") }}
          </p>
        </header>

        <div class="leading-relaxed text-lg whitespace-pre-line">
          {{ post?.content }}
        </div>

        <div class="space-y-3 max-w-xs select-none">
          <p class="text-xs uppercase tracking-widest text-graymain">
            Publieke stemming
          </p>

          <div class="relative h-3 border border-ink overflow-hidden">
            <div class="absolute left-0 top-0 h-full bg-ink/80 transition-all duration-300" :style="{
        width: `${Math.max(
          10, (post?.likeCount || 0) / ((post?.likeCount || 0) + (post?.dislikeCount || 0) || 1) * 100)}%`
      }" />

            <div class="absolute right-0 top-0 h-full bg-ink/20" />
          </div>

          <div class="flex items-center justify-between text-xs uppercase tracking-widest">

            <button @click="likePost" class="group flex items-center gap-2">
              <span class="w-8 h-8 flex items-center justify-center border border-ink group-hover:bg-ink group-hover:text-paper
               group-active:scale-95 transition">
                ✓ </span>

              <span class="tabular-nums">
                {{ post?.likeCount || 0 }}
              </span>
            </button>

            <button @click="dislikePost" class="group flex items-center gap-2">
              <span class="w-8 h-8 flex items-center justify-center border border-ink
               group-hover:bg-ink group-hover:text-paper group-active:scale-95 transition">
                ✕ </span>

              <span class="tabular-nums"> {{ post?.dislikeCount || 0 }} </span>
            </button>

          </div>
        </div>

        <!-- COMMENTS -->
        <section class="border-t-2 border-ink pt-8 space-y-6">
          <h2 class="text-xl font-serif font-bold uppercase">
            Reacties
          </h2>

          <!-- SCROLLABLE COMMENTS -->
          <div class="max-h-[40vh] overflow-y-auto pr-4 space-y-6 custom-scrollbar">
            <div v-for="comment in post?.comments" :key="comment.id" class="border-b border-ink/30 pb-4">
              <p class="leading-relaxed">
                {{ comment.comment }}
              </p>

              <p class="text-xs text-graymain mt-2 italic">
                {{ comment.user?.name || "Onbekend" }} ·
                {{ new Date(comment.createdAt).toLocaleString("nl-NL") }}
              </p>
            </div>

            <p v-if="!post?.comments?.length" class="italic text-graymain">
              Nog geen reacties — wees de eerste.
            </p>
          </div>

          <div class="pt-6 space-y-4">
            <textarea v-model="newComment" rows="4" placeholder="Schrijf een reactie…" class="w-full bg-transparent border border-ink p-4 resize-none outline-none" />

            <button @click="submitComment" :disabled="submitting" class="btn-primary px-6 py-2 disabled:opacity-50">
              {{ submitting ? "Plaatsen…" : "Plaats reactie" }}
            </button>
          </div>
        </section>
      </article>
    </section>
  </div>
</template>
