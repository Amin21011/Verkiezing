<template>
  <div class="forum">
    <h2 class="forum-title">Forum</h2>

    <!-- Nieuwe topic aanmaken -->
    <div class="new-topic card">
      <h3>Nieuw topic aanmaken</h3>
      <input
        v-model="newTopicName"
        type="text"
        placeholder="Nieuw topic aanmaken"
      />
      <button @click="createTopic">Topic aanmaken</button>
    </div>

    <hr />

    <!-- Nieuwe post -->
    <form @submit.prevent="submitPost" class="new-post card">
      <h3>Nieuwe post</h3>

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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getToken } from "@/services/authService";

const title = ref("");
const content = ref("");
const selectedTopicId = ref<number | null>(null);
const newTopicName = ref("");

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
    topics.value = (await res.json()) as Topic[];
  } catch (err: unknown) {
    if (err instanceof Error) {
      alert(err.message);
    } else {
      alert("Er is een onbekende fout opgetreden bij het ophalen van topics");
    }
  }
};

// Nieuwe topic aanmaken
const createTopic = async () => {
  if (!newTopicName.value.trim()) return;

  try {
    const res = await fetch("http://localhost:8080/api/topics", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newTopicName.value.trim() }),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Kon topic niet aanmaken");
    }

    const created: Topic = await res.json();
    topics.value.push(created); // Voeg toe aan dropdown
    selectedTopicId.value = created.id; // automatisch selecteren
    newTopicName.value = ""; // reset input
  } catch (err) {
    console.error(err);
    alert(err instanceof Error ? err.message : "Er is iets misgegaan");
  }
};

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
        topicId: selectedTopicId.value,
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

onMounted(() => {
  fetchTopics();
});
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
  background-color: #00712d;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #00591a;
}
</style>
