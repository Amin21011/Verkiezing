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
  const token = getToken();
  if (!token) {
    alert("Je moet ingelogd zijn om een post te plaatsen");
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

    if (!res.ok) throw new Error("Kon post niet aanmaken");

    const newPost = await res.json();
    window.location.href = `/forum/${newPost.id}`;

  } catch (err: any) {
    alert(err.message);
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
