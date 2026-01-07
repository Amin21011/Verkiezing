const API_URL = import.meta.env.VITE_API_URL;

export async function simulateSeats(turnout: number, threshold: number) {
  const res = await fetch(`${API_URL}/simulation/seats`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ turnout, threshold }),
  });
  return res.json();
}
