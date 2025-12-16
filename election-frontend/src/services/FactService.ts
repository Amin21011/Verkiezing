export async function getDailyFacts(): Promise<Response> {
  const res = await fetch("http://localhost:8080/api/statistics/daily-fact", {
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
