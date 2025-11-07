import { ref, watch, computed, onMounted } from "vue";
import type { Candidate, Party } from "@/types/IParty";
import { getTopCandidatesByParty, getTopParties } from "@/services/electionService";

export function useElectionResults() {
  const topParties = ref<Party[]>([]);
  const topCandidates = ref<Candidate[]>([]);
  const selectedPartyId = ref<string>("");
  const selectedPartyName = ref<string>("Onbekende partij");
  const isLoading = ref(false);
  const message = ref("");

  const chartLabels = computed(() =>
    topCandidates.value.map((c) => `${c.firstName} ${c.lastName}`)
  );
  const chartValues = computed(() =>
    topCandidates.value.map((c) => Number(c.votes) || 0)
  );

  async function loadTopParties(limit = 8) {
    try {
      isLoading.value = true;
      const data = await getTopParties(limit);
      topParties.value = data;

      if (data.length > 0) {
        selectedPartyId.value = data[0].id;
        selectedPartyName.value = data[0].name;
        await loadTopCandidates(selectedPartyId.value);
      }
    } catch (err) {
      console.error("Fout bij laden partijen:", err);
      message.value = "Kon top partijen niet laden.";
    } finally {
      isLoading.value = false;
    }
  }

  async function loadTopCandidates(partyId: string, limit = 5) {
    if (!partyId) return;
    try {
      isLoading.value = true;
      const data = await getTopCandidatesByParty(partyId, limit);
      topCandidates.value = data;
      console.log("Top kandidaten geladen voor partij", partyId, data);
    } catch (err) {
      console.error("Fout bij laden kandidaten:", err);
      message.value = "Kon kandidaten niet laden.";
    } finally {
      isLoading.value = false;
    }
  }

  watch(selectedPartyId, async (newId) => {
    const found = topParties.value.find((p) => p.id === newId);
    selectedPartyName.value = found ? found.name : "Onbekende partij";
    await loadTopCandidates(newId);
  });

  onMounted(() => loadTopParties());

  return {
    topParties,
    topCandidates,
    selectedPartyId,
    selectedPartyName,
    isLoading,
    message,
    chartLabels,
    chartValues,
    loadTopParties,
    candidates: loadTopCandidates,
  }
}
