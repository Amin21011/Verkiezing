export interface Party {
  id: string;
  name: string;
  voteCount: number;
  leaderName?: string;
  website?: string;
}

export interface Candidate {
  id: string;
  shortCode: string;
  firstName: string;
  lastName: string;
  partyId: string;
  votes: number;
}
