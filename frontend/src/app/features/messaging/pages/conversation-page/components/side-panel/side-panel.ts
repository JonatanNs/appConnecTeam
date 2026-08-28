import { Component, input } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { IConversation } from '../../../../interfaces/conversation.interface';

@Component({
  selector: 'app-side-panel',
  imports: [FaIconComponent],
  templateUrl: './side-panel.html',
  styleUrl: './side-panel.css',
})
export class SidePanel {
  conversation = input.required<IConversation | null>();

  protected readonly faTimes = faTimes;
}
