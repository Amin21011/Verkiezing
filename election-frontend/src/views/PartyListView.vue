<template>
  <div class="min-h-screen bg-[#d9d9d9] flex flex-col items-center font-sans">
    <!-- Header -->
    <header class="w-full bg-[#e5e5e5] py-6 shadow-sm text-center border-b border-gray-400">
      <h1 class="text-4xl font-serif text-gray-800 tracking-wide">
        VERKIEZINGEN 2025
      </h1>
      <p class="text-gray-700 text-sm mt-1 italic">
        Alles wat je moet weten voordat je stemt.
      </p>
    </header>

    <!-- Partijen Grid -->
    <main class="py-10 max-w-5xl w-full px-6">
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 place-items-center">
        <div
          v-for="party in parties"
          :key="party.id"
          class="bg-[#a0a0a0] border border-gray-600 rounded-md shadow-md flex flex-col items-center justify-between p-4 w-48 hover:shadow-lg transition-all"
        >
          <div class="flex items-center justify-center w-32 h-32 bg-white rounded-sm overflow-hidden">
            <img
              :src="party.imageUrl"
              :alt="party.name"
              class="object-contain w-28 h-28"
            />
          </div>
          <button
            @click="showDetail(party)"
            class="mt-4 bg-[#333] text-white py-1 px-4 text-sm rounded-md hover:bg-black transition"
          >
            More info
          </button>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer
      class="w-full border-t border-black mt-10 py-6 text-sm flex justify-center gap-10 text-gray-800"
    >
      <a href="#" class="hover:underline">Over Ons</a>
      <a href="#" class="hover:underline">Contact</a>
      <a href="#" class="hover:underline">FAQ</a>
    </footer>

    <!-- Popup -->
    <div
      v-if="selectedParty"
      class="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50"
    >
      <div
        class="bg-[#d9d9d9] rounded-lg shadow-2xl p-8 w-[90%] max-w-md text-center relative border border-gray-700"
      >
        <button
          @click="selectedParty = null"
          class="absolute top-2 right-3 text-gray-600 text-2xl hover:text-black"
        >
          ✕
        </button>
        <div class="flex justify-center mb-4">
          <div class="flex items-center justify-center w-32 h-32 bg-white rounded-sm overflow-hidden">
            <img
              :src="selectedParty.imageUrl"
              :alt="selectedParty.name"
              class="object-contain w-28 h-28"
            />
          </div>
        </div>
        <h3 class="text-2xl font-bold text-gray-800 mb-3">
          {{ selectedParty.name }}
        </h3>
        <p class="text-gray-700 leading-relaxed">
          {{ selectedParty.description }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  name: 'PartyListView',
  setup() {
    const parties = ref([])
    const selectedParty = ref(null)

    const fetchParties = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/parties')
        if (!response.ok) throw new Error('Failed to fetch parties')
        parties.value = await response.json()
      } catch (error) {
        console.error('Error loading parties:', error)
      }
    }

    const showDetail = (party) => {
      selectedParty.value = party
    }

    onMounted(fetchParties)

    return { parties, selectedParty, showDetail }
  },
}
</script>

<style scoped>
body {
  font-family: 'Inter', sans-serif;
}
</style>
