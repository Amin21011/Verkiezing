<template>
<div class="min-h-screen w-full bg-[#d9d9d9] flex flex-col font-sans">

    <!-- PARTIJEN GRID -->
<main class="w-full max-w-none flex-1 py-10 px-0 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8 place-items-center">
      <div v-for="party in parties" :key="party.id" class="party-card w-40">
        <div class="flex items-center justify-center w-32 h-32 bg-white overflow-hidden mb-2">
          <img :src="party.imageUrl" :alt="party.name" class="object-contain w-28 h-28" />
        </div>
        <button @click="showDetail(party)" class="mt-2 bg-gray-800 text-white py-1 px-4 text-sm rounded-md hover:bg-black transition">
          More info
        </button>
      </div>
    </main>

    <!-- FOOTER -->
    <footer class="w-full border-t border-black mt-10 py-6 text-sm flex justify-center gap-10 text-gray-800">
      <a href="#" class="hover:underline">Over Ons</a>
      <a href="#" class="hover:underline">Contact</a>
      <a href="#" class="hover:underline">FAQ</a>
    </footer>

    <!-- POPUP -->
    <div v-if="selectedParty" class="popup-overlay fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div class="popup-content relative bg-white p-6 rounded-md w-11/12 max-w-md">
        <button @click="selectedParty = null" class="absolute top-2 right-3 text-gray-600 text-2xl hover:text-black">✕</button>
        <div class="flex justify-center mb-4">
          <div class="flex items-center justify-center w-32 h-32 bg-white overflow-hidden">
            <img :src="selectedParty.imageUrl" :alt="selectedParty.name" class="object-contain w-28 h-28" />
          </div>
        </div>
        <h3 class="text-2xl font-bold text-gray-800 mb-3">{{ selectedParty.name }}</h3>
        <p class="text-gray-700 leading-relaxed">{{ selectedParty.description }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

// Party interface
interface Party {
  id: number
  name: string
  imageUrl: string
  description: string
}

// State
const parties = ref<Party[]>([])
const selectedParty = ref<Party | null>(null)

const API_URL = import.meta.env.VITE_API_URL as string

// Data ophalen
const fetchParties = async () => {
  try {
    const response = await fetch(`${API_URL}/parties`)
    if (!response.ok) throw new Error('Failed to fetch parties')
    parties.value = await response.json() as Party[]
  } catch (error) {
    console.error('Error loading parties:', error)
  }
}

// Show popup
const showDetail = (party: Party) => {
  selectedParty.value = party
}

onMounted(fetchParties)
</script>

<style scoped>
@import '@/assets/base.css';
@import "tailwindcss";

/* Root app container */
#app {
  width: 100vw;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

/* Navbar buttons */
.icon-btn {
  padding: 0.5rem;
  border-radius: 9999px;
  transition: background-color 0.2s ease;
  font-size: 1rem;
}

.icon-btn:hover {
  background-color: #f3f4f6;
}

.search-input {
  border: 1px solid #d1d5db;
  border-radius: 9999px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  width: 160px;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

/* Titles & dividers */
.main-title {
  font-family: Kailasa, sans-serif;
}

.divider {
  width: 100%;
  height: 2px;
  background-color: black;
}

/* Party cards */
.party-card {
  background-color: #a0a0a0;
  border: 1px solid #666;
  border-radius: 0.5rem;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s ease;
}

.party-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}

.party-card img {
  width: 7rem;
  height: 7rem;
  object-fit: contain;
}

/* Popup overlay */
.popup-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0,0,0,0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 50;
}

.popup-content {
  background-color: #d9d9d9;
  border-radius: 0.5rem;
  max-width: 90%;
  width: 28rem;
  padding: 2rem;
  text-align: center;
  border: 1px solid #666;
  box-shadow: 0 6px 20px rgba(0,0,0,0.3);
}

/* Safe fixes */
* {
  -webkit-overflow-scrolling: touch;
}
</style>
