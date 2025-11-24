import { ref, computed } from 'vue'

export interface ProvinceResult {
  provinceNaam: string
  stemmenPerPartij: Record<string, number>
}

export function useProvinceCompare() {
  const API_COMPARE = `${import.meta.env.VITE_API_URL}/provinces/compare`

  const selectedYear = ref(2025)
  const selectedProvinces = ref<string[]>([])
  const compareResults = ref<ProvinceResult[]>([])

  // ⬇️ True wanneer je precies 2 provincies geselecteerd hebt
  const isComparing = computed(() => selectedProvinces.value.length === 2)

  // ⬇️ Toevoegen/verwijderen + reset logica
  function toggleProvince(name: string) {
    // Als provincie al gekozen was → deselecteren
    if (selectedProvinces.value.includes(name)) {
      selectedProvinces.value = selectedProvinces.value.filter((p) => p !== name)
      compareResults.value = [] // reset vergelijking
      return
    }

    // Max 2 provincies selecteren
    if (selectedProvinces.value.length >= 2) return

    // Nieuwe provincie selecteren
    selectedProvinces.value.push(name)

    // Zodra er 2 gekozen zijn → data ophalen
    if (selectedProvinces.value.length === 2) {
      compareTwoProvinces()
    }
  }

  // ⬇️ Backend vergelijking ophalen
  async function compareTwoProvinces() {
    const body = {
      year: selectedYear.value,
      provinces: selectedProvinces.value,
    }

    const res = await fetch(API_COMPARE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })

    compareResults.value = await res.json()
  }

  // ⬇️ Sorteren + percentages berekenen
  function sortedVotes(province: ProvinceResult) {
    const votes = province.stemmenPerPartij
    const maxCount = Math.max(...Object.values(votes))

    return Object.entries(votes)
      .map(([party, count]) => ({
        party,
        count,
        percentage: maxCount > 0 ? (count / maxCount) * 100 : 0,
      }))
      .sort((a, b) => b.count - a.count)
  }

  function setYear(year: number) {
    selectedYear.value = year

    // als er al 2 provincies zijn → opnieuw vergelijken
    if (selectedProvinces.value.length === 2) {
      compareTwoProvinces()
    }
  }


  return {
    selectedYear,
    selectedProvinces,
    compareResults,
    isComparing,
    setYear,
    toggleProvince,
    sortedVotes,
  }
}
