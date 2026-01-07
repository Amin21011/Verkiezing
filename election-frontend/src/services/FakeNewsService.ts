import { publicFetch } from "@/helpers/publicFetch";

export const API_URL = `${import.meta.env.VITE_API_URL}`

export async function analyzeFakeNews(payload: { text?: string; url?: string }) {
  const res = await publicFetch(
    `${API_URL}/analysis/fake-news`,
    {
      method: "POST",
      body: JSON.stringify(payload),
    }
  );

  if (!res.ok) {
    console.error(await res.text());
    throw new Error("AI analysis failed");
  }

  return res.json();
}
