<template>
  <div class="login-prompt">
    <p>Heb je al een account?</p>
    <button class="login-btn">Log in</button>
  </div>
</template>

<style scoped>
.login-prompt {
  color: white;
  padding: 2rem;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.login-prompt p {
  margin-bottom: 1rem;
  font-size: 1.1rem;
}
.login-btn {
  background: #d9d9d9;
  color: #1c1c1c;
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 6px;
  font-weight: 500;
}
.login-btn:hover {
  background: #cfcfcf;
}
</style>
