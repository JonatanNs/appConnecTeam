import { Service, signal } from '@angular/core';

export type FlashType = 'success' | 'error' | 'info' | 'warning';

@Service()
export class FlashMessageService {
  message = signal<{ type: FlashType; text: string } | null>(null);

  show(text: string, type: FlashType = 'info', duration = 4000) {
    this.message.set({ type, text });
    setTimeout(() => this.message.set(null), duration);
  }
  success(text: string) { this.show(text, 'success'); }
  error(text: string) { this.show(text, 'error'); }
}
