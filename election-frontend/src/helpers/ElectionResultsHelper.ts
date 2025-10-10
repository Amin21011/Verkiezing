import { ref } from "vue";
import type { Party } from "@/types/IParty";
import { getTopParties } from "@/services/electionService";

export function useElectionResults() {
    const topParties = ref<Party[]>([]);
    const message = ref("");

    const loadTopParties = async (limit = 3) => {
        try {
            const data = await getTopParties(limit);
            topParties.value = data;
            console.log("Top partijen geladen (service):", data);
        } catch (error) {
            console.error("Fout bij laden van top partijen:", error);
            message.value = "Kon top partijen niet laden.";
        }
    };

    return { topParties, message, loadTopParties };
}
