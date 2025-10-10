<script setup lang="ts">
import { ref, onMounted } from 'vue';

const isScrolled = ref(false);

const handleScroll = () => {
  isScrolled.value = window.scrollY > 50;
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

const ticker = [
  'Laatste peilingen tonen verrassende wending',
  'Nieuwe partij groeit explosief in stedelijke gebieden',
  'Quiz: Hoe goed ken jij de top-partijen?'
];

</script>

<template>
  <header :class="['navbar', { scrolled: isScrolled }]">

    <div class="top-bar">
      <div class="left-buttons">
        <button class="icon-btn" title="Menu">☰</button>
        <button class="icon-btn" title="Dark Mode">🌙</button>
      </div>

      <div class="right-section">
        <div class="search-wrapper">
          <input type="text" placeholder="Zoeken..." class="search-input" />
          <span class="search-icon">🔍</span>
        </div>
        <button class="icon-btn" title="Images">
          <img src="../assets/img/images.png" class="navIcon" />
        </button>
      </div>
    </div>

    <div class="title-section">
      <h1 class="main-title">VERKIEZINGEN 2025 <span>✏️</span></h1>
      <div class="subtitle-banner">Alles wat je moet weten voordat je stemt.</div>
      <div class="divider"></div>
    </div>

    <section class="ticker">
      <div class="ticker-track">
        <span v-for="(item, i) in ticker" :key="i" class="ticker-item">{{ item }}</span>
      </div>
    </section>
  </header>
</template>

<style scoped>

.navbar {
  width: 100%;
  max-width: 1600px;
  margin: 0 auto;
  padding: 1rem 2rem 3rem 2rem;
  box-sizing: border-box;
  position: sticky;
  top: 0;
  backdrop-filter: blur(6px);
  background-color: #fdfcf7;
  transition: all 0.3s ease;
  z-index: 50;
}

.navbar.scrolled {
  padding: 0.5rem 2rem 2rem 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 1rem;
}

.left-buttons {
  display: flex;
  gap: 0.75rem;
}

/* Right section */
.right-section {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

/* Icon buttons */
.icon-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.25rem;
  padding: 0.25rem 0.5rem;
  transition: transform 0.2s ease, color 0.2s ease;
}

.icon-btn:hover {
  transform: scale(1.2);
  color: darkslateblue;
}

.navIcon {
  width: 32px;
  height: 32px;
  object-fit: contain;
  transition: transform 0.2s ease;
}

.navIcon:hover {
  transform: scale(1.1);
}

.search-wrapper {
  position: relative;
}

.search-input {
  border: none;
  border-radius: 9999px;
  padding: 0.5rem 2rem 0.5rem 1rem;
  font-size: 0.875rem;
  background-color: #f5f5f5;
  transition: all 0.2s ease;
  width: 160px;
}

.search-input:focus {
  outline: none;
  background-color: #fff;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

.search-icon {
  position: absolute;
  right: 0.5rem;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  color: #888;
  font-size: 1rem;
}

.title-section {
  text-align: center;
  color: #111827;
  margin-top: 1rem;
}

.main-title {
  font-family: 'Playfair Display', serif;
  font-weight: 900;
  font-size: 3rem;
  letter-spacing: 2px;
  background: linear-gradient(90deg, #1c1c1c 0%, #4b5563 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0.5rem 0;
}

@media (min-width: 768px) {
  .main-title {
    font-size: 3.5rem;
  }
}

.subtitle-banner {
  display: inline-block;
  background-color: #fffae5;
  color: #111827;
  font-weight: 600;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  box-shadow: 2px 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 1rem;
}

.divider {
  width: 100%;
  height: 3px;
  background-image: repeating-linear-gradient(
    45deg,
    #1c1c1c,
    #1c1c1c 5px,
    #4b5563 5px,
    #4b5563 10px
  );
  margin-top: 1rem;
}

.ticker { background-color: #e5e5e5; overflow: hidden; white-space: nowrap; border-top: 2px solid #1c1c1c; border-bottom: 2px solid #1c1c1c; width: 100%; }
.ticker-track { display: inline-block; padding: 0.5rem 0; animation: tickerScroll 20s linear infinite; }
.ticker-item { margin-right: 4rem; font-weight: 700; text-transform: uppercase; }
@keyframes tickerScroll { 0% { transform: translateX(100%); } 100% { transform: translateX(-100%); } }

</style>
