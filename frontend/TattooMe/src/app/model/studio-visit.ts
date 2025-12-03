export interface StudioVisit {
  id: string;
  startDate: string;
  endDate: string;
  comment?: string;
  artistId: string;
  studioId: string;
  status: string;
  artistName?: string;
  studioName?: string;
}