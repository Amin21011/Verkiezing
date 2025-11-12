<template>
  <div class="forum">
    <h2>Forum</h2>

    <!-- Nieuwe post -->
    <form @submit.prevent="submitPost" class="new-post">
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
import { ref } from "vue";
import { getToken } from "@/services/authService";

const title = ref("");
const content = ref("");
const success = ref(false);
const error = ref("");

const submitPost = async () => {
  error.value = "";
  success.value = false;

  const token = getToken();
  if (!token) {
    error.value = "Je bent niet ingelogd!";
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
      }),
    });

    if (!res.ok) throw new Error("Fout bij verzenden van post");

    success.value = true;
    title.value = "";
    content.value = "";
    setTimeout(() => (success.value = false), 3000);
  } catch (err: any) {
    error.value = err.message || "Onbekende fout";
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
