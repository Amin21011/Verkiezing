export type SeatEntry = {
  party: string;
  seats: number;
  color?: string;
  delta?: number;
};

export type SeatPoint = {
  x: number;
  y: number;
  party: string;
};

export function normalizeParty(raw: string): string {
  const cleaned = raw
    .toUpperCase()
    .replace(/\(.*?\)/g, "")
    .replace(/\/.*$/, "")
    .replace(/[^A-Z0-9]/g, "")
    .trim();

  const map: Record<string, string> = {
    PVVPARTIJVOORDEVRIJHEID: "PVV",
    PVV: "PVV",

    GROENLINKSPARTIJVANDEARBEIDPVDA: "GLPVDA",
    GLPVDA: "GLPVDA",
    GROENLINKS: "GLPVDA",

    PARTIJVOORDEDIEREN: "PVDD",
    PVD: "PVDD",
    PVDD: "PVDD",

    CHRISTENUNIE: "CU",
    CU: "CU",

    FORUMVOORDEMOCRATIE: "FVD",
    FVD: "FVD",

    STATENKUNDIGEGEREFORMEERDEPARTIJSGP: "SGP",
    SGP: "SGP",

    PIRATENPARTIJDEGROENEN: "PIRATEN",
    PIRATEN: "PIRATEN",

    JA21: "JA21",
    DENK: "DENK",
    BIJ1: "BIJ1",

    VVD: "VVD",
    CDA: "CDA",
    BBB: "BBB",
    VOLT: "VOLT",
    D66: "D66",
    SP: "SP",
  };

  return map[cleaned] ?? cleaned;
}

export const retroColors: Record<string, string> = {
  VVD: "#5A7BA8",
  GLPVDA: "#C04A52",
  PVV: "#444444",
  D66: "#8CBF86",
  BBB: "#D1BB54",
  CDA: "#8AA58A",
  PVDD: "#84669F",
  SP: "#C85A5A",
  CU: "#6F8BBF",
  VOLT: "#7B66CC",
  JA21: "#A89463",
  DENK: "#48A7A7",
  FVD: "#996666",
  SGP: "#BFAE78",
  BIJ1: "#222222",
  PIRATEN: "#669999",
};

export function getPartyColor(name: string): string {
  return retroColors[normalizeParty(name)] ?? "#AAAAAA";
}

export function flattenSeats(data: SeatEntry[]): { party: string; color: string }[] {
  const arr: { party: string; color: string }[] = [];

  data.forEach(e => {
    const key = normalizeParty(e.party);
    const color = e.color ?? getPartyColor(key);

    for (let i = 0; i < e.seats; i++) {
      arr.push({
        party: key,
        color
      });
    }
  });

  return arr.slice(0, 150);
}

export function drawSeats(canvas: HTMLCanvasElement, data: SeatEntry[]): SeatPoint[] {
  const ctx = canvas.getContext("2d")!;
  const w = canvas.width;
  const h = canvas.height;

  ctx.clearRect(0, 0, w, h);

  const seats = flattenSeats(data);
  const positions: SeatPoint[] = [];

  const radii = [1.04, 1.29, 1.55];

  radii.forEach((r, i) => {
    const perArc = Math.ceil(seats.length / 3);
    const slice = seats.slice(i * perArc, (i + 1) * perArc);

    let angle = Math.PI;
    const step = Math.PI / (slice.length + 1);

    slice.forEach(seat => {
      const radius = (h * r) / 2;
      const x = w / 2 + Math.cos(angle) * radius;
      const y = h * 1.12 + Math.sin(angle) * radius;

      positions.push({ x, y, party: seat.party });

      ctx.beginPath();
      ctx.arc(x + 1.5, y + 1.5, 11, 0, Math.PI * 2);
      ctx.fillStyle = "rgba(0,0,0,0.15)";
      ctx.fill();

      ctx.beginPath();
      ctx.arc(x, y, 11, 0, Math.PI * 2);
      ctx.fillStyle = seat.color;
      ctx.fill();

      ctx.strokeStyle = "rgba(0,0,0,0.25)";
      ctx.lineWidth = 1.2;
      ctx.stroke();

      angle += step;
    });
  });

  return positions;
}

export function detectHover(mouseX: number, mouseY: number, seats: SeatPoint[]) {
  const hitSq = 15 * 15;

  for (const seat of seats) {
    const dx = seat.x - mouseX;
    const dy = seat.y - mouseY;

    if (dx * dx + dy * dy <= hitSq) {
      return seat.party;
    }
  }

  return null;
}
