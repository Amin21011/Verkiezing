<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getToken } from "@/services/authService";

interface User {
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
    post.value = await res.json();
  } catch (err: any) {
    errorMsg.value = err.message;
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
        <p class="text-gray-800 leading-relaxed whitespace-pre-line mb-8">
          {{ post?.content }}
        </p>

        <div class="flex items-center gap-6 mt-4">
          <button
            @click="likePost"
            class="flex items-center gap-2 text-green-700 hover:text-green-900 transition"
          >
            👍 <span>{{ post?.likeCount || 0 }}</span>
          </button>

          <button
            @click="dislikePost"
            class="flex items-center gap-2 text-red-600 hover:text-red-800 transition"
          >
            👎 <span>{{ post?.dislikeCount || 0 }}</span>
          </button>
        </div>

        <div class="mt-8 border-t border-gray-200 pt-6">
          <h2 class="text-lg font-semibold mb-4">Reacties</h2>

          <div v-if="post?.comments?.length">
            <div
              v-for="comment in post.comments"
              :key="comment.id"
              class="mb-4 p-4 bg-gray-50 rounded-lg"
            >
              <p class="text-gray-700">{{ comment.comment }}</p>
              <p class="text-xs text-gray-500 mt-1">
                door {{ comment.user?.name || "Onbekend" }} •
                {{ new Date(comment.createdAt).toLocaleString("nl-NL") }}
              </p>
            </div>
          </div>
          <p v-else class="text-gray-400">Nog geen reacties — wees de eerste!</p>

          <div class="mt-6">
            <textarea
              v-model="newComment"
              placeholder="Schrijf een reactie..."
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[#00712D] resize-none"
              rows="3"
            ></textarea>
            <button
              @click="submitComment"
              :disabled="submitting"
              class="mt-3 bg-[#00712D] text-white px-4 py-2 rounded-lg hover:bg-green-700 transition disabled:opacity-50"
            >
              {{ submitting ? "Bezig..." : "Plaats reactie" }}
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>

</template>


<style scoped></style>
