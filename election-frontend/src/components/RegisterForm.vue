<script setup lang="ts">
import { ref } from 'vue';

const name = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');

const handleSubmit = () => {
  if (password.value !== confirmPassword.value) {
    alert('Wachtwoorden komen niet overeen!');
    return;
  }
  console.log('Registratie:', { name: name.value, email: email.value });
};
</script>

<template>
  <div class="register-form">
    <h2 class="title">Registreer Hier</h2>
    <form @submit.prevent="handleSubmit" class="form">
      <input type="text" v-model="name" placeholder="Name" required />
      <input type="email" v-model="email" placeholder="Email" required />
      <input type="password" v-model="password" placeholder="Password" required />
      <input type="password" v-model="confirmPassword" placeholder="Confirm password" required />
      <button type="submit" class="submit-btn">Submit</button>
    </form>
  </div>
</template>

<style scoped>
.register-form {
  background: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 350px;
}
.title {
  font-size: 1.5rem;
  font-family: 'Merriweather', serif;
  margin-bottom: 1.5rem;
  color: #1c1c1c;
}
.form input {
  width: 100%;
  padding: 0.6rem;
  margin-bottom: 1rem;
  border: none;
  background: #e6e6e6;
}
.submit-btn {
  background-color: #3b4c91;
  color: white;
  padding: 0.6rem 1.2rem;
  border-radius: 20px;
  border: none;
  font-weight: 600;
}
.submit-btn:hover {
  background-color: #2e3b73;
}
</style>
