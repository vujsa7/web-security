import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, Event } from '@angular/router';
import { AuthService } from 'src/app/core/authentication/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  isVisible: boolean = false;

  constructor(private router: Router, private authService: AuthService) { 
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

  logout(){
    this.authService.flushToken();
    this.router.navigate(['onboarding']);
  }

}
