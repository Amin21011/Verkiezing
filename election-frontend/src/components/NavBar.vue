<template>
  <header class="navbar">
    <!-- Top bar -->
    <div class="top-bar">
      <!-- Left buttons -->
      <div class="left-buttons">
        <button class="icon-btn">☰</button>
        <button class="icon-btn">🌙</button>
      </div>

      <!-- Search + profile -->
      <div class="right-section">
        <input
          type="text"
          placeholder="Zoeken..."
          class="search-input"
        />
        <button class="icon-btn">👤</button>
      </div>
    </div>

    <!-- Main header title -->
    <div class="title-section">
      <div class="divider"></div>

      <h1 class="main-title">
        VERKIEZINGEN 2025 <span>✏️</span>
      </h1>
      <p class="subtitle">
        Alles wat je moet weten voordat je stemt.
      </p>
    </div>
  </header>
</template>

<style scoped>
/* ===== STRUCTURE ===== */
.navbar {
  width: 100%;
  max-width: 1500px; /* past mooi bij HomePage-container */
  margin: 0 auto 2.5rem auto;
  padding: 0 1rem;
  box-sizing: border-box;
}

/* ===== TOP BAR ===== */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

/* Left buttons */
.left-buttons {
  display: flex;
  gap: 0.75rem;
}

/* Right section (search + profile) */
.right-section {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

/* Search input */
.search-input {
  border: 1px solid #d1d5db; /* gray-300 */
  border-radius: 9999px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  width: 160px;
  transition: all 0.2s ease;
}

@media (min-width: 768px) {
  .search-input {
    width: 260px;
  }
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6; /* blue-500 */
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

/* Icon buttons */
.icon-btn {
  padding: 0.5rem;
  border-radius: 9999px;
  transition: background-color 0.2s ease;
  font-size: 1rem;
}

.icon-btn:hover {
  background-color: #f3f4f6; /* gray-100 */
}

/* ===== TITLE SECTION ===== */
.title-section {
  text-align: center;
  color: #111827;
}

.main-title {
  font-family: 'Georgia', serif;
  font-weight: 700;
  font-size: 2.75rem;
  margin-bottom: 0.5rem;
}

.divider {
  width: 100%;
  height: 2px;
  background-color: black;
  margin: 2rem 0;
}

@media (min-width: 768px) {
  .main-title {
    font-size: 3.25rem;
  }
}

.subtitle {
  color: #4b5563; /* gray-600 */
  font-size: 1.125rem;
}

@media (min-width: 768px) {
  .subtitle {
    font-size: 1.25rem;
  }
}
</style>
<script setup lang="ts">
</script>
