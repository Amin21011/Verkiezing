<script setup lang="ts">
import NavBar from '../components/NavBar.vue'
import FooterComp from '../components/FooterComp.vue'
import RegisterForm from '../components/RegisterForm.vue'
import LoginPrompt from '../components/LoginPrompt.vue'
</script>

<template>
  <div class="register-page">
    <NavBar />

    <main class="register-section">
      <div class="register-wrap"></div>

      <div class="register-wrapper">
        <div class="register-left">
          <RegisterForm />
        </div>
        <div class="register-right">
          <LoginPrompt />
        </div>
      </div>
    </main>

    <FooterComp />
  </div>
</template>

<style scoped>
.register-page {
  background-color: #fdfcf7;
  font-family: 'Merriweather', serif;
  color: #1c1c1c;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.register-section {
  flex: 1;
  position: relative; /* voor absolute positioning van .register-wrap */
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 5rem 1rem;
}

.register-wrap {
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  width: 100%;
  height: 260px;
  background-color: #555555;
  z-index: 1;
}

.register-wrapper {
  position: relative;
  z-index: 2; /* bovenop de grijze balk */
  display: flex;
  width: 100%;
  max-width: 1200px;
  background: transparent;
}

.register-left {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-left > * {
  background: #ffffff;
  padding: 2.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  width: 340px;
  position: relative;
}

.register-right {
  flex: 1;
  background-color: transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  position: relative;
}

@media (max-width: 900px) {
  .register-wrapper {
    flex-direction: column;
    align-items: center;
  }

  .register-right {
    margin-top: 2rem;
  }

  .register-wrap {
    height: 400px;
  }
}
</style>
