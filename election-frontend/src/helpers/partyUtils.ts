export const PARTY_NAMES: Record<string, string> = {
  "50plus": "50PLUS",
  "bbb": "BBB",
  "bij1": "BIJ1",
  "bvnl___groep_van_haga": "BVNL / Groep Van Haga",
  "cda": "CDA",
  "christenunie": "ChristenUnie",
  "d66": "D66",
  "denk": "DENK",
  "forum_voor_democratie": "Forum voor Democratie",
  "groenlinks___partij_van_de_arbeid": "GroenLinks / PvdA",
  "ja21": "JA21",
  "lef___voor_de_nieuwe_generatie": "LEF – Voor de Nieuwe Generatie",
  "lp_(libertaire_partij)": "LP (Libertaire Partij)",
  "nederland_met_een_plan": "Nederland met een PLAN",
};

export function getPartyLabel(id: string): string {
  return PARTY_NAMES[id] ?? id.replace("_", " ");
}
