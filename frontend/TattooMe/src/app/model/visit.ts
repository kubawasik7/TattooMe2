export interface Visit {
  id: string;
  status: string;
  date: string;
  artistName: string;
  clientName: string;
  comment?: string;
  flashDescription?: string;
  flashImage?: string;
  tattooStudioName?: string;
  allergies?: string;
  chronicDiseases?: string;
  medicines?: string;
  experiences?: string;
}
