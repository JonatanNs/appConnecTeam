import {Component, inject} from '@angular/core';
import {FlashMessageService} from '../../../core/services/flashMessage/flash-message.service';
import {
  faCircleCheck,
  faCircleInfo,
  faCircleXmark,
  faTriangleExclamation,
  faXmark
} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-flash-message',
  imports: [
    FontAwesomeModule
  ],
  templateUrl: './flash-message.html',
  styleUrl: './flash-message.css',
})
export class FlashMessage {
  protected flashService = inject(FlashMessageService);

  protected readonly faXmark = faXmark;

  icons = {
    success: faCircleCheck,
    error: faCircleXmark,
    info: faCircleInfo,
    warning: faTriangleExclamation,
  };
}
