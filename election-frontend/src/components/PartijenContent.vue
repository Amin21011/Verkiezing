<script setup lang="ts">
import { onMounted } from "vue";
import { useElectionResults } from "../helpers/ElectionResultsHelper";

const { topParties, message, loadTopParties } = useElectionResults();

onMounted(() => {
  loadTopParties(3);
});
</script>

<template>

    <p v-if="message" class="message">{{ message }}</p>

    <ul v-else class="party-list">
      <li
        v-for="(party, index) in topParties"
        :key="party.id"
        class="party-item"
      >
        <span class="rank">{{ index + 1 }}.</span>
        <span class="name">{{ party.name }}</span>
        <span class="votes">{{ party.voteCount.toLocaleString() }} stemmen</span>
      </li>
    </ul>

</template>

<style scoped>
.party-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.party-item {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
  border-bottom: 1px solid #ddd;
  font-size: 0.95rem;
}

.party-item:last-child {
  border-bottom: none;
}

.rank {
  font-weight: bold;
  width: 20px;
}

.name {
  flex: 1;
  text-align: left;
  margin-left: 4px;
}

.votes {
  font-size: 0.85rem;
  color: #444;
}
</style>
