import { Component, HostListener } from '@angular/core';
import { NavigationEnd, Router, Event } from '@angular/router';
import { AuthService } from 'src/app/core/authentication/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  email: String = "";
  isVisible: boolean = false;

  constructor(private router: Router, private authService: AuthService) {
    this.authService.userEmailObservable.subscribe(email => this.email = email); 
    router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        if(this.router.url.includes("onboarding")){
          this.isVisible = false;
        } else {
          this.isVisible = true;
        }
      }
    });
  }

  isRevokeHidden(): boolean{
    if(this.authService.getTokenRole() != "ROLE_ADMIN")
      return true;
    return false;
  }

  logout(): void{
    this.authService.flushToken();
    this.router.navigate(['onboarding']);
  }


}
