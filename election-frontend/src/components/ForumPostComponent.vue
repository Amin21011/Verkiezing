<template>
  <div class="forum">
    <h2>Forum</h2>

    <!-- Nieuwe post -->
    <form @submit.prevent="submitPost" class="new-post">
      <select v-model="selectedTopicId" required>
        <option value="" disabled>-- Kies een topic --</option>
        <option v-for="topic in topics" :key="topic.id" :value="topic.id">
          {{ topic.name }}
        </option>
      </select>

      <input
        v-model="title"
        type="text"
        placeholder="Titel van je vraag"
        required
      />
      <textarea
        v-model="content"
        placeholder="Beschrijf je vraag..."
        required
      ></textarea>
      <button type="submit">Plaats bericht</button>
    </form>

    <hr />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getToken } from "@/services/authService";

const title = ref("");
const content = ref("");
const selectedTopicId = ref<number | null>(null);

// Type van topic
interface Topic {
  id: number;
  name: string;
}

const topics = ref<Topic[]>([]);

// Topics ophalen
const fetchTopics = async () => {
  try {
    const res = await fetch("http://localhost:8080/api/topics");
    if (!res.ok) throw new Error("Kon topics niet ophalen");
    topics.value = await res.json() as Topic[];
  } catch (err: unknown) {
    if (err instanceof Error) {
      alert(err.message);
    } else {
      alert("Er is een onbekende fout opgetreden bij het ophalen van topics");
    }
  }
};

onMounted(() => {
  fetchTopics();
});

// Nieuwe post plaatsen
const submitPost = async () => {
  const token = getToken();
  if (!token) {
    alert("Je moet ingelogd zijn om een post te plaatsen");
    return;
  }

  if (!selectedTopicId.value) {
    alert("Je moet een topic selecteren");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/api/forum", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        title: title.value,
        content: content.value,
        topicId: selectedTopicId.value
      }),
    });

    if (!res.ok) throw new Error("Kon post niet aanmaken");

    const newPost = await res.json();
    window.location.href = `/forum/${newPost.id}`;
  } catch (err: unknown) {
    if (err instanceof Error) {
      alert(err.message);
    } else {
      alert("Er is een onbekende fout opgetreden bij het plaatsen van de post");
    }
  }
};
</script>

<style scoped>
.forum {
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
}

.new-post {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.post {
  margin-bottom: 1.5rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}
</style>
