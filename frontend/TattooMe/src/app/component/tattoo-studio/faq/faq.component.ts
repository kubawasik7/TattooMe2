import { Component, Input, OnInit } from '@angular/core';
import { Faq } from '../../../model/faq';
import { FaqService } from '../../../service/faq.service';

@Component({
  selector: 'app-faq',
  standalone: false,
  templateUrl: './faq.component.html',
  styleUrl: './faq.component.css'
})
export class FaqComponent implements OnInit {

  faqs: Faq[] = [];
  newQuestion = '';
  newAnswer = '';
  @Input() studioId!: string;
  @Input() currentUserRole: string | null = null;

  faqStates: Map<string, boolean> = new Map();

  constructor(private faqService: FaqService) { }

  ngOnInit(): void {
    this.loadFaqs();
  }

  loadFaqs(): void {
    this.faqService.getFaqs(this.studioId).subscribe(data => {
      this.faqs = data;
      this.faqs.forEach(f => this.faqStates.set(f.id, false));
    });
  }

  toggleFaq(faqId: string): void {
    const current = this.faqStates.get(faqId) || false;
    this.faqStates.set(faqId, !current);
  }

  isOpen(faqId: string): boolean {
    return this.faqStates.get(faqId) || false;
  }

  get canEdit(): boolean {
    return this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR';
  }

  addFaq(): void {
    if (!this.newQuestion || !this.newAnswer) return;

    this.faqService.addFaq(this.studioId, { question: this.newQuestion, answer: this.newAnswer })
      .subscribe(faq => {
        this.faqs.push(faq);
        this.faqStates.set(faq.id, false);
        this.newQuestion = '';
        this.newAnswer = '';
      });
  }

  deleteFaq(faqId: string): void {
    this.faqService.deleteFaq(this.studioId, faqId).subscribe(() => {
      this.faqs = this.faqs.filter(f => f.id !== faqId);
      this.faqStates.delete(faqId);
    });
  }

}