const API_URL = import.meta.env.VITE_API_URL;

export async function getDailyFacts(): Promise<DailyFact[]> {
  const res = await fetch(`${API_URL}/statistics/daily-fact`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });
  return res.json();
}

export interface DailyFact {
  type: 'region' | 'candidate' | 'party'
  title: string
  description: string
  value: string
  link: string
}
