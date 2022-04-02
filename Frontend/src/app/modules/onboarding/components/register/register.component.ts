import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/authentication/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;

  constructor(private toastr: ToastrService, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm() {
    this.registerForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    });
  }

  onSubmit() : void{
    if(this.registerForm.valid){
      this.authService.register(this.registerForm.value).subscribe(
        data => {
          this.toastr.success('Go ahead and login now.', 'User successfully registered')
          this.router.navigate(['/onboarding/login']);
        },
        error => {
          if(error.status == 422)
            this.toastr.error("This account is already in use. Please try with different email address.", "Account registered");
        }
      );
    }
  }

}
