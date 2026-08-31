import { Component, inject, Signal, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { Router, RouterLink } from '@angular/router';
import { faChevronDown, faCircleQuestion, faGear, faRightFromBracket, faShieldHalved, faUser } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../../../../../../features/auth/service/auth.service';
import { ICurrentUser } from '../../../../../../shared/interfaces/current-user.interface';

@Component({
  selector: 'app-user-profil',
  imports: [FaIconComponent, RouterLink],
  templateUrl: './user-profil.html',
  styleUrl: './user-profil.css',
})
export class UserProfil {

  private authService = inject(AuthService);
  private router = inject(Router);

  currentUser: Signal<ICurrentUser | null> = this.authService.currentUser;
  isProfileMenuOpen = signal(false);

  openProfileMenu(): void {
    this.isProfileMenuOpen.set(true);
  }
  closeProfileMenu(): void {
    this.isProfileMenuOpen.set(false);
  }

  toggleProfileMenu(): void {
    this.isProfileMenuOpen.update((open) => !open);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  protected readonly faRightFromBracket = faRightFromBracket;
  protected readonly faUser = faUser;
  protected readonly faGear = faGear;
  protected readonly faShieldHalved = faShieldHalved;
  protected readonly faCircleQuestion = faCircleQuestion;
  protected readonly faChevronDown = faChevronDown;
}
