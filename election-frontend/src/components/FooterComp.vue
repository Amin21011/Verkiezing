<script setup lang="ts"></script>

<template>
  <footer class="footer">
    <div class="footer-links">
      <span class="footer-link">Over Ons</span>
      <span class="footer-link">Contact</span>
      <span class="footer-link">FAQ</span>
    </div>

    <div class="social-links">
      <a href="#" class="social-icon">📘</a>
      <a href="#" class="social-icon">📷</a>
      <a href="#" class="social-icon">🐦</a>
      <a href="#" class="social-icon">💼</a>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  margin-top: 2rem;
  padding-top: 1rem;
  border-top: 1px solid #d1d5db;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  text-align: center;
  color: #4b5563;
  gap: 0.75rem;
}

@media (min-width: 640px) {
  .footer {
    flex-direction: row;
  }
}

.footer-links {
  display: flex;
  gap: 1rem;
}

.footer-link {
  cursor: pointer;
  transition: color 0.2s ease;
}

.footer-link:hover {
  color: #111827;
}

.social-links {
  display: flex;
  gap: 0.75rem;
}

.social-icon {
  color: #4b5563;
  font-size: 1.1rem;
  transition: color 0.2s ease;
}

.social-icon:hover {
  color: #111827;
}
</style>
