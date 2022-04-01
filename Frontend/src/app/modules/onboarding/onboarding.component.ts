import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthService } from 'src/app/core/authentication/auth.service';

@Component({
  selector: 'app-onboarding',
  templateUrl: './onboarding.component.html',
  styleUrls: ['./onboarding.component.scss']
})
export class OnboardingComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router, private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.spinner.show();

    setTimeout(() => {
      if(this.authService.hasValidToken()){
        console.log("Session token has expired. Flushing...")
        this.router.navigate(['certificates']);
      } else {
        this.router.navigate(['onboarding/login']);
      }

      this.spinner.hide();
    }, 1000);
    
  }

}
