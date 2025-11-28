import { Component, Input, OnInit } from '@angular/core';
import { Faq } from '../../../model/faq';
import { FaqService } from '../../../service/faq.service';
import { NotificationService } from '../../../service/notification.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-faq',
  standalone: false,
  templateUrl: './faq.component.html',
  styleUrl: './faq.component.css'
})
export class FaqComponent implements OnInit {
  @Input() studioId!: string;
  @Input() currentUserRole: string | null = null;
  faqs: Faq[] = [];
  faqForm!: FormGroup;
  showAddForm = false;
  faqStates: Map<string, boolean> = new Map();

  constructor(private fb: FormBuilder, private faqService: FaqService) { }

  ngOnInit(): void {
    this.faqForm = this.fb.group({
      question: ['', [Validators.required, Validators.maxLength(150)]],
      answer: ['', [Validators.required, Validators.maxLength(1000)]]
    });
    this.loadFaqs();
  }

  get canEdit(): boolean {
    return this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR';
  }

  toggleAddFaq() {
    this.showAddForm = !this.showAddForm;
  }

  loadFaqs(): void {
    this.faqService.getFaqs(this.studioId).subscribe(data => {
      this.faqs = data;
      this.faqs.forEach(f => this.faqStates.set(f.id, false));
    });
  }

  addFaq(): void {
    if (this.faqForm.invalid) return;

    const { question, answer } = this.faqForm.value;

    this.faqService.addFaq(this.studioId, { question, answer }).subscribe(faq => {
      this.faqs.unshift(faq);
      this.faqStates.set(faq.id, false);
      this.faqForm.reset();
      this.showAddForm = false;
    });
  }

  toggleFaq(faqId: string): void {
    const current = this.faqStates.get(faqId) || false;
    this.faqStates.set(faqId, !current);
  }

  isOpen(faqId: string): boolean {
    return this.faqStates.get(faqId) || false;
  }

  deleteFaq(faqId: string): void {
    this.faqService.deleteFaq(this.studioId, faqId).subscribe(() => {
      this.faqs = this.faqs.filter(f => f.id !== faqId);
      this.faqStates.delete(faqId);
    });
  }
}
