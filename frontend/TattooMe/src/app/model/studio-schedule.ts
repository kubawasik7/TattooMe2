export interface StudioSchedule {
  slotId: string;
  dateTime: string;
  available: boolean;
  artistId: string;
  artistNickname: string;
  artistName: string;
  artistSurname: string;
  studioRole: 'OWNER' | 'EDITOR' | 'MEMBER';
}
