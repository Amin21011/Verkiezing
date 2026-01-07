<template>
  <section class="max-w-4xl mx-auto space-y-16 pt-12">
    <header class="border-b-4 border-ink pb-6">
      <p class="font-mono text-[10px] uppercase tracking-[0.35em] text-muted">
        Community </p>
      <h2 class="text-4xl font-headline font-black">
        Forum </h2>
    </header>

    <!-- Nieuw topic -->
    <section class="bg-paper border-4 border-ink shadow-press p-8">
      <div class="space-y-6">

        <header class="space-y-1">
          <p class="font-mono text-[10px] uppercase tracking-widest text-muted">
            Beheer
          </p>
          <h3 class="text-2xl font-serif font-bold">
            Nieuw topic
          </h3>
        </header>

        <div class="grid grid-cols-1 md:grid-cols-[1fr_auto] gap-4 items-end">
          <input v-model="newTopicName" type="text"
            placeholder="Bijv. Verkiezingen 2025" class="input-field" />

          <button @click="createTopic" class="btn-primary-blue">
            Topic aanmaken
          </button>
        </div>

        <p v-if="topicError" class="text-red-600 text-sm italic">
          {{ topicError }}
        </p>
      </div>
    </section>

    <section class="bg-paper border-4 border-ink shadow-press p-8">
      <form @submit.prevent="submitPost" class="space-y-6">

        <header class="space-y-1">
          <p class="font-mono text-[10px] uppercase tracking-widest text-muted">
            Discussie
          </p>
          <h3 class="text-2xl font-serif font-bold">
            Nieuwe post
          </h3>
        </header>

        <div class="flex flex-col gap-4">
          <select v-model="selectedTopicId" required class="input-field">
            <option value="" disabled>— Kies een topic —</option>
            <option v-for="topic in topics" :key="topic.id" :value="topic.id">
              {{ topic.name }}
            </option>
          </select>

          <input v-model="title" type="text" placeholder="Titel van je vraag" required class="input-field" />

          <textarea v-model="content" placeholder="Beschrijf je vraag..." required rows="6" class="input-field resize-none"></textarea>
        </div>

        <div class="pt-2">
          <button type="submit" class="btn-primary-blue">
            Plaats bericht
          </button>
        </div>
      </form>
    </section>
  </section>
</template>


<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getToken } from "@/services/authService";

const API_URL = import.meta.env.VITE_API_URL;
const title = ref("");
const content = ref("");
const selectedTopicId = ref<number | null>(null);
const newTopicName = ref("");

const topicError = ref("");

// Type van topic
interface Topic {
  id: number;
  name: string;
}

const topics = ref<Topic[]>([]);

// Topics ophalen
const fetchTopics = async () => {
  try {
    const res = await fetch(`${API_URL}/topics`);
    if (!res.ok) throw new Error("Kon topics niet ophalen");
    topics.value = (await res.json()) as Topic[];
  } catch (err: unknown) {
    topicError.value = err instanceof Error ? err.message : "Er is een onbekende fout opgetreden";
  }
};

// Nieuwe topic aanmaken
const createTopic = async () => {
  topicError.value = ""; // reset error
  if (!newTopicName.value.trim()) return;

  try {
    const res = await fetch(`${API_URL}/topics`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newTopicName.value.trim() }),
    });

    if (!res.ok) {
      const text = await res.text();
      topicError.value = text || "Kon topic niet aanmaken";
      return;
    }

    const created: Topic = await res.json();
    topics.value.push(created); // Voeg toe aan dropdown
    selectedTopicId.value = created.id; // automatisch selecteren
    newTopicName.value = ""; // reset input
  } catch (err) {
    topicError.value = err instanceof Error ? err.message : "Er is iets misgegaan";
  }
};

// Nieuwe post plaatsen
const submitPost = async () => {
  const token = getToken();
  if (!token) {
    topicError.value = "Je moet ingelogd zijn om een post te plaatsen";
    return;
  }

  if (!selectedTopicId.value) {
    topicError.value = "Je moet een topic selecteren";
    return;
  }

  try {
    const res = await fetch(`${API_URL}/forum`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        title: title.value,
        content: content.value,
        topicId: selectedTopicId.value,
      }),
    });

    if (!res.ok) {
      const text = await res.text();
      topicError.value = text || "Kon post niet aanmaken";
      return;
    }

    const newPost = await res.json();
    window.location.href = `/forum/${newPost.id}`;
  } catch (err: unknown) {
    topicError.value = err instanceof Error ? err.message : "Er is een onbekende fout opgetreden";
  }
};

onMounted(fetchTopics);
</script>

<style scoped>
.forum {
  max-width: 700px;
  margin: 2rem auto;
  padding: 1rem;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

.forum-title {
  text-align: center;
  font-size: 2rem;
  font-weight: bold;
  color: #00712d;
  margin-bottom: 2rem;
}

.card {
  background-color: #ffffff;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.card h3 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
  color: #00712d;
}

input,
textarea,
select {
  padding: 0.75rem;
  border-radius: 10px;
  border: 1px solid #ccc;
  font-size: 1rem;
  outline: none;
  transition: border 0.2s, box-shadow 0.2s;
}

input:focus,
textarea:focus,
select:focus {
  border-color: #00712d;
  box-shadow: 0 0 5px rgba(0, 113, 45, 0.3);
}

textarea {
  resize: vertical;
  min-height: 80px;
}

button {
  padding: 0.75rem 1.5rem;
  background-color: #476a8a;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover {
  background-color: midnightblue;
}

/* Error message styling */
.error-msg {
  color: #d32f2f;
  font-weight: 600;
  margin-top: 0.5rem;
}
</style>
